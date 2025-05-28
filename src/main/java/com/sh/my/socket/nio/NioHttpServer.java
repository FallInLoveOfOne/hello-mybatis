package com.sh.my.socket.nio;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Component
public class NioHttpServer {

    @PostConstruct
    public void start() {
        new Thread(this::runServer, "NioHttpServer-Thread").start();
    }

    private void runServer() {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(8080));
            serverChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("🚀 JSON HTTP Server running at http://localhost:8080 ...");

            while (true) {
                selector.select();

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    try {
                        if (key.isAcceptable()) {
                            SocketChannel client = serverChannel.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(4096));
                        } else if (key.isReadable()) {
                            handleRead(key);
                        }
                    } catch (IOException e) {
                        System.err.println("⚠️ 连接处理出错：" + e.getMessage());
                        key.channel().close();
                        key.cancel();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("❌ 服务器启动失败：" + e.getMessage());
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        int readBytes = client.read(buffer);
        if (readBytes == -1) {
            client.close();
            return;
        }

        String request = extractRequest(buffer);
        if (request == null) return;

        System.out.println("📥 请求内容：\n" + request);

        String path = getRequestPath(request);
        String responseBody;
        String contentType;

        if ("/api".equals(path)) {
            contentType = "application/json; charset=UTF-8";
            responseBody = "{ \"message\": \"Hello from Spring Bean NIO Server!\", \"code\": 200 }";
        } else {
            contentType = "text/html; charset=UTF-8";
            responseBody = "<h1>Hello from Spring NIO Server!</h1>";
        }

        byte[] responseBytes = buildHttpResponse(responseBody, contentType);
        client.write(ByteBuffer.wrap(responseBytes));
        client.close();
    }

    private String extractRequest(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        String request = new String(bytes, StandardCharsets.UTF_8);

        if (request.contains("\r\n\r\n")) {
            return request;
        }

        buffer.position(buffer.limit());
        buffer.limit(buffer.capacity());
        return null;
    }

    private byte[] buildHttpResponse(String body, String contentType) {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + bodyBytes.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

        byte[] headerBytes = headers.getBytes(StandardCharsets.UTF_8);

        byte[] response = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, response, headerBytes.length, bodyBytes.length);

        return response;
    }

    private String getRequestPath(String request) {
        if (request == null || request.isEmpty()) return "/";
        String[] lines = request.split("\r\n");
        if (lines.length > 0) {
            String[] parts = lines[0].split(" ");
            if (parts.length > 1) {
                return parts[1];
            }
        }
        return "/";
    }
}

