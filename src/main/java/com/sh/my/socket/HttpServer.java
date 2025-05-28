package com.sh.my.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 存在问题
 * http请求无法响应
 */
public class HttpServer {
    public static void main(String[] args) throws IOException {
        int port = 6666;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                // 读取请求行
                String requestLine = reader.readLine();
                System.out.println("Request: " + requestLine);

                // 跳过 HTTP 请求头，直到空行
                String line;
                while (!(line = reader.readLine()).isEmpty()) {
                    // System.out.println("Header: " + line);
                }

                // 构造 HTTP 响应
                String responseBody = "Hello from server!";
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/plain; charset=utf-8");
                writer.println("Content-Length: " + responseBody.getBytes().length);
                writer.println(); // 空行表示响应头结束
                writer.println(responseBody);

                socket.close();
            }
        }
    }
}


/*public class HttpServer {
    public static void main(String[] args) throws IOException {
        int port = 6666;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String line = reader.readLine();
                System.out.println("Request: " + line);

                while (!(line = reader.readLine()).isEmpty()) {
                    // consume headers
                }

                String body = "Hello from server!";
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/plain");
                writer.println("Content-Length: " + body.length());
                writer.println(); // blank line
                writer.println(body);

                socket.close();
            }
        }
    }
}*/
