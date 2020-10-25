package com.xiaozhameng.netty.nio;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝-使用 transferFrom
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws IOException {
        File target = new File("d:\\temp\\temp-copy.txt");
        File source = new File("d:\\temp\\temp.txt");

        FileInputStream fileInputStream = new FileInputStream(source);
        FileChannel fileChannel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(target);
        FileChannel fileChannel2 = fileOutputStream.getChannel();

        fileChannel2.transferFrom(fileChannel1,0,fileChannel1.size());

        fileChannel1.close();
        fileChannel2.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
