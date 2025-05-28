package com.sh.my.socket.nio;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Component
public class NioThreadedTcpServer {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private Thread serverThread;
    private volatile boolean running = false;

    private final int PORT = 8888;
    private final int BUFFER_SIZE = 1024;
    private final ExecutorService clientHandlerPool = Executors.newFixedThreadPool(10); // 可配置

    @PostConstruct
    public synchronized void start() throws IOException {
        if (running) return;

        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(PORT));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        running = true;
        serverThread = new Thread(this::runServerLoop, "NioTcpServer-Main");
        serverThread.start();

        System.out.println("[Server] Started on port " + PORT);
    }

    public synchronized void stop() {
        if (!running) return;

        running = false;
        if (selector != null) selector.wakeup(); // ✅ 先打断 select

        try {
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close();
            }
            if (selector != null && selector.isOpen()) {
                selector.close(); // ✅ 最后关闭 selector
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        clientHandlerPool.shutdownNow();
        System.out.println("[Server] Stopped");
    }

    @PreDestroy
    public void onDestroy() {
        stop();
    }

    private void runServerLoop() {
        try {
            while (running && selector.isOpen()) {
                selector.select(); // 被 wakeup() 打断时会继续

                if (!running || !selector.isOpen()) break;

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    try {
                        if (key.isValid() && key.isAcceptable()) {
                            handleAccept(key);
                        } else if (key.isValid() && key.isReadable()) {
                            key.interestOps(0);
                            clientHandlerPool.execute(() -> handleRead(key));
                        }
                    } catch (CancelledKeyException e) {
                        // 可以忽略
                    }
                }
            }
        } catch (ClosedSelectorException e) {
            System.out.println("[Server] Selector closed gracefully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("[Server] Event loop stopped.");
        }
    }


    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        System.out.println("[Server] Accepted: " + client.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try {
            int bytesRead = client.read(buffer);
            if (bytesRead == -1) {
                client.close();
                return;
            }

            buffer.flip();
            String message = new String(buffer.array(), 0, buffer.limit()).trim();
            System.out.println("[Server] Received: " + message);

            // 响应客户端
            ByteBuffer response = ByteBuffer.wrap(("Echo: " + message + "\n").getBytes());
            client.write(response);

            // 重新注册读事件
            key.interestOps(SelectionKey.OP_READ);
            selector.wakeup(); // 防止阻塞
        } catch (IOException e) {
            try {
                System.out.println("[Server] Closing client: " + client.getRemoteAddress());
                client.close();
            } catch (IOException ignored) {
            }
        }
    }
}
