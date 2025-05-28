package com.sh.my.socket.bean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Component
public class TcpServer {

    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private volatile boolean running = true;
    private final int port = 8888;

    @PostConstruct
    public void start() {
        executor.submit(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("TCP Server started on port " + port);

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // 用线程池处理客户端
                    executor.submit(() -> handleClient(clientSocket));
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleClient(Socket clientSocket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String msg = reader.readLine();
            System.out.println("Received from client: " + msg);
            writer.println("Hello from server!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignore) {
            }
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        executor.shutdownNow();
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("TCP Server stopped");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

