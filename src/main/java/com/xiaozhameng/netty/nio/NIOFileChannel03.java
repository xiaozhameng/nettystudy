package com.xiaozhameng.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用一个Buffer完成文件的拷贝
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        File source = new File("d:\\temp\\temp.txt");
        File target = new File("d:\\temp\\temp-copy.txt");

        FileInputStream fileInputStream = new FileInputStream(source);
        FileChannel fileChannel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(target);
        FileChannel fileChannel2 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) { //循环读取

            //这里有一个重要的操作，一定不要忘了
            /*
            public final Buffer clear() { position = 0;
            limit = capacity; mark = -1; return this;
            }
            */

            byteBuffer.clear(); //清空 buffer
            int read = fileChannel1.read(byteBuffer);
            System.out.println("read =" + read);
            if (read == -1) { //表示读完
                break;

            }
            //将 buffer 中的数据写入到 fileChannel02 -- 2.txt
            byteBuffer.flip();
            fileChannel2.write(byteBuffer);
        }

        //关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
