package com.xiaozhameng.netty.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存数据写入到文件
 */
public class NIOFileChannel01 {

    public static void main(String[] args) throws IOException {
        String sourceData = "hello NIO";
        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\temp\\temp.txt");

        //通过 fileOutputStream 获取 对应的 FileChannel
        //这个 fileChannel 真实 类型是	FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(sourceData.getBytes());

        buffer.flip();

        //将 byteBuffer 数据写入到 fileChannel
        fileChannel.write(buffer);
        fileOutputStream.close();
    }

}
