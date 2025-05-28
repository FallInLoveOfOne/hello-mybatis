package com.sh.my.socket.nio;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
//@Component
public class NioTcpServer implements Runnable {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private Thread serverThread;
    private volatile boolean running = true;

    private final int PORT = 8888;
    private final int BUFFER_SIZE = 1024;

    @PostConstruct
    public void start() throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(PORT));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        serverThread = new Thread(this, "NioTcpServerThread");
        serverThread.start();

        log.info("NIO TCP Server started on port " + PORT);
    }

    @PreDestroy
    public void stop() throws IOException {
        running = false;
        selector.wakeup(); // 打断 select 阻塞
        serverThread.interrupt();

        if (serverChannel != null && serverChannel.isOpen()) {
            serverChannel.close();
        }
        if (selector != null && selector.isOpen()) {
            selector.close();
        }

        log.info("NIO TCP Server stopped");
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try {
            while (running) {
                selector.select(); // 阻塞直到有事件
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove(); // 避免重复处理

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key, buffer);
                    }
                }
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        log.info("Accepted connection from " + client.getRemoteAddress());
    }

    private void handleRead(SelectionKey key, ByteBuffer buffer) {
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        int bytesRead;

        try {
            bytesRead = client.read(buffer);
            if (bytesRead == -1) {
                client.close();
                return;
            }

            buffer.flip();
            String msg = new String(buffer.array(), 0, buffer.limit()).trim();
            log.info("Received: " + msg);

            // 响应客户端
            buffer.clear();
            buffer.put(("Echo: " + msg + "\n").getBytes());
            buffer.flip();
            client.write(buffer);
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

