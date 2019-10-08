package com.xiaozhameng.nettystudy.niodemo;

import com.xiaozhameng.nettystudy.utils.SocketUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author xiaozhameng
 * 基于Bio实现的客户端代码
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = SocketUtil.getPort(args);

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            // 开始输出
            out.println("QUERY TIME ORDER");
            System.out.println("send order 2 server succeed.");

            // 接收响应
            String resp = in.readLine();
            System.out.println("Now is :" + resp);
        }catch (Exception e){
            System.out.println("客户端连接异常：" + e);
        }finally {
            if (out != null){
                out.close();
            }

            if (in != null){
                try{
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            if (socket != null){
                try{
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
