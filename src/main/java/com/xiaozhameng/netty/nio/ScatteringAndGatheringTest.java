package com.xiaozhameng.netty.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {

        //使用 ServerSocketChannel 和 SocketChannel 网络

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到 socket ，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建 buffer 数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;    //假定从客户端接收 8 个字节
        //循环的读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l; //累计读取的字节数
                System.out.println("byteRead=" + byteRead);
                // 使 用 流 打 印 , 看 看 当 前 的 这 个 buffer 的 position 和 limit
                Arrays.stream(byteBuffers).map(buffer -> "postion=" + buffer.position() + ", limit=" + buffer.limit()).forEach(System.out::println);
            }

            // 将 所 有 的 buffer 进 行 flip
            Arrays.asList(byteBuffers).forEach(Buffer::flip);

            //将数据读出显示到客户端
            long byteWirte = 0;
            while (byteWirte < messageLength) {
                long l = socketChannel.write(byteBuffers); //
                byteWirte += l;
            }
            //将所有的 buffer 进行 clear
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });
        }
    }
}
