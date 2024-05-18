package cn.phorcys.io.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BIOServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket();
            ss.bind(new InetSocketAddress("127.0.0.1", 8000));
            System.out.println("Socket Server has been established now at 127.0.0.1:8000");
            while (true) {
                Socket accept = ss.accept();

                new Thread(() -> {
                    handle(accept);
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handle(Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            int len;
            while ((len = socket.getInputStream().read(bytes)) != -1) {
                String received = new String(bytes, 0, len, StandardCharsets.UTF_8);
                String response = "返回: " + received;

                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(response.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
