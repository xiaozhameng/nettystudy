package com.xiaozhameng.netty.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 聊天机器人客户端
 */
public class GroupChatClient {
    /**
     * 服务端地址和端口
     */
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6667;

    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    /**
     * 构造函数，初始化数据
     */
    public GroupChatClient() throws IOException {
        // 获取selector对象
        this.selector = Selector.open();
        // 连接服务器
        this.socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 将channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 获取userName
        this.userName = socketChannel.getLocalAddress().toString();
        System.out.println("客户端" + userName + " 准备就绪！");
    }

    /**
     * 发送消息到群聊， 其实就是把message 写入到通道
     */
    public void sendInfo2Server(String msg) {
        msg = this.userName + "说 ：" + msg;

        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器读取消息，就是从通道channel中读取数据
     */
    public void readMessageFromServer() throws IOException {
        int select = selector.select();
        if (select <= 0){
            System.out.println("没有可用通道...");
            return;
        }

        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();

            if (key.isReadable()) {
                // 反向获取通道
                SocketChannel channel = (SocketChannel) key.channel();
                // 读取数据
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                try {
                    channel.read(buffer);
                    String message = new String(buffer.array());

                    System.out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 消息处理完之后，将key 从集合中移除
            iterator.remove();
        }
    }

    /**
     * 客户端
     */
    public static void main(String[] args) throws IOException {
        GroupChatClient client = new GroupChatClient();
        Scanner scanner = new Scanner(System.in);

        // 单独的线程处理服务端发过来的消息
        new Thread(() -> {
            while (true) {
                try {
                    client.readMessageFromServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 从键盘发送消息到群聊系统
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            client.sendInfo2Server(line);
        }
    }
}
