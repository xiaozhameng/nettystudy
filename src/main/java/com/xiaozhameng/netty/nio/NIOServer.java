package com.xiaozhameng.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO 编码完成服务端代码
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 获取一个Selector对象
        Selector selector = Selector.open();

        // 绑定一个端口，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将ServerSocketChannel 注册到Selector 上，并关心OP_ACCEPT 事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 等待1秒，如果没有事件发生，就返回
            if (selector.select(1000) == 0) {
                System.out.println("服务端等待1秒，未发现有客户端连接");
                continue;
            }

            // 如果返回大于0，就获取相应的selectKey集合 ，如果大于0，表示已获取到关注的时间
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 通过selectKey反向获取通道
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectKey = iterator.next();

                // 根据key通道对应的事件，做相应的处理
                if (selectKey.isAcceptable()) {
                    // 表示，有新的客户端请求
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("有新的客户端请求，产生了一个新的的socketChannel + " + socketChannel.hashCode());

                    // 将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将socketChannel注册的selector ，关注OP_READ 事件,同时，给绑定一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 发生了读事件
                if (selectKey.isReadable()) {
                    // 通过key反向获取channel
                    SocketChannel channel = (SocketChannel) selectKey.channel();
                    // 获取关联的buffer
                    ByteBuffer buffer = (ByteBuffer) selectKey.attachment();

                    channel.read(buffer);
                    System.out.println("来自客户端消息 = " + new String(buffer.array()));
                }

                // 手动从集合中清除selectKey，防止重复操作
                iterator.remove();
            }
        }
    }
}
