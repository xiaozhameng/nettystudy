package com.xiaozhameng.netty.nio;

import java.nio.ByteBuffer;

/**
 * 2)可以将一个普通 Buffer 转成只读 Buffer [举例说明]
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.put("hello,java".getBytes());

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(new String(readOnlyBuffer.array()));

        readOnlyBuffer.clear();
        readOnlyBuffer.put("hello,NIO".getBytes());
    }
}
