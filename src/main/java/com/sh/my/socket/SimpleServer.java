package com.sh.my.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
        int port = 6666;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress());

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String clientMessage = reader.readLine();
            System.out.println("Received from client: " + clientMessage);

            writer.println("Hello from server!");

            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

