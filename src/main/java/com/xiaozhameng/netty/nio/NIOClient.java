package com.xiaozhameng.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO 客户端代码
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 获取一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞方式
        socketChannel.configureBlocking(false);

        // 服务端Ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他事情");
            }
        }

        // 如果连接成功了就发送数据
        String str = "hello , Word";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
