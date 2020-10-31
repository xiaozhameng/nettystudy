package com.xiaozhameng.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * 群聊系统服务端
 */
public class GroupChatServer {

    private ServerSocketChannel listenChannel;
    private Selector selector;
    private static final int PORT = 6667;

    /**
     * 初始化构造器
     */
    public GroupChatServer() {
        try {
            this.selector = Selector.open();
            this.listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞方式
            listenChannel.configureBlocking(false);
            // 将channel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环监听的方法
     */
    public void listen() {
        try {
            // 循环监听
            while (true) {
                int select = selector.select();
                if (select <= 0) {
                    System.out.println("暂时没有可以处理的事件");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 有连接
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        SocketChannel sc = listenChannel.accept();
                        sc.configureBlocking(false);
                        //将该 sc 注册到 seletor
                        sc.register(selector, SelectionKey.OP_READ);

                        //提示
                        System.out.println(sc.getRemoteAddress() + "  上线 ");
                    }

                    if (key.isReadable()) { //通道发送 read 事件，即通道是可读的状态
                        //处理读 (专门写方法..)
                        readData(key);
                    }

                    //当前的 key  删除，防止重复处理
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据的方法
     */
    private void readData(SelectionKey key) {
        // 反向获取channel
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            // 创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int read = channel.read(byteBuffer);
            if (read > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("from 客户端 ：" + msg);

                // 向其他客户端转发消息
                sendInfo2Other(msg, channel);
            }
        } catch (IOException e) {
            // xxx 离线了
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                // 取消注册，关闭通道
                key.cancel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 转发消息
     */
    private void sendInfo2Other(String msg, SocketChannel channel) {
        System.out.println("服务器转发消息中。。。");
        Iterator<SelectionKey> iterator = selector.keys().iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            // 取出channel
            SelectableChannel target = key.channel();

            // 排除自己
            if (target instanceof SocketChannel && target != channel) {
                SocketChannel targetChannel = (SocketChannel) target;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());

                try {
                    // 从缓冲区读取数据，写入到通道
                    targetChannel.write(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 服务端入口
     */
    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
