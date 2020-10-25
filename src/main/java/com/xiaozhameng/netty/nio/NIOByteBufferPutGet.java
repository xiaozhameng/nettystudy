package com.xiaozhameng.netty.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer 支持类型化的 put 和 get, put 放入的是什么数据类型，get 就应该使用相应的数据类型来取出，否则可能有 BufferUnderflowException 异常。
 *
 * [举例说明]
 */
public class NIOByteBufferPutGet {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(10);
        buffer.putLong(2L);
        buffer.putChar('A');
        buffer.putShort((short) 1);

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }
}
