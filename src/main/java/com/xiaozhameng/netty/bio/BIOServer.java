package com.xiaozhameng.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws IOException {
        // 创建一个ServerSocket ，用来接收客户端请求
        ServerSocket serverSocket = new ServerSocket(6666);

        // 使用线程池的方式优化
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        while (true) {
            // 监听，等待客户端请求
            System.out.println("等待客户端连接………… 线程信息 id = " + Thread.currentThread().getId() + "线程名字 = " + Thread.currentThread().getName());
            Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端,线程信息 id = " + Thread.currentThread().getId() + "线程名字 = " + Thread.currentThread().getName());

            // 创建一个线程，与之进行通信
            executorService.submit(() -> {
                try {
                    handler(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void handler(Socket socket) throws IOException {
        System.out.println("handler 入口 线程信息 id = " + Thread.currentThread().getId() + "线程名字 = " + Thread.currentThread().getName());
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];

        while (true) {
            System.out.println("数据read....... 线程信息 id = " + Thread.currentThread().getId() + "线程名字 = " + Thread.currentThread().getName());
            int read = inputStream.read(bytes);
            if (read != -1) {
                // 输出到客户端
                System.out.println(new String(bytes, 0, read));
            } else {
                break;
            }
        }
    }

}
