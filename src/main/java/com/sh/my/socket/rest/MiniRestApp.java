package com.sh.my.socket.rest;

// === Spring Bean风格的NIO HTTP微框架 ===

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// === 注解定义 ===
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface RestController {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface GetMapping {
    String value();
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface PostMapping {
    String value();
}

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface RequestBody {
}

// === Request 封装 ===
class HttpRequest {
    private final String method;
    private final String path;
    private final String body;

    public HttpRequest(String method, String path, String body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }
}

class RouteHandler {
    private final Object controller;
    private final Method method;

    public RouteHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object invoke(HttpRequest request) throws Exception {
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(RequestBody.class)) {
                args[i] = request.getBody();
            } else {
                args[i] = null;
            }
        }
        return method.invoke(controller, args);
    }
}

class Router {
    private static final Map<String, RouteHandler> routes = new HashMap<>();

    public static void register(Object controller) {
        Class<?> clazz = controller.getClass();
        if (!clazz.isAnnotationPresent(RestController.class)) return;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                String path = "GET " + method.getAnnotation(GetMapping.class).value();
                routes.put(path, new RouteHandler(controller, method));
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                String path = "POST " + method.getAnnotation(PostMapping.class).value();
                routes.put(path, new RouteHandler(controller, method));
            }
        }
    }

    public static RouteHandler getHandler(String method, String path) {
        return routes.get(method + " " + path);
    }
}

@RestController
class TestApi {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Bean NIO Server!";
    }

    @PostMapping("/echo")
    public String echo(@RequestBody String body) {
        return "Echo: " + body;
    }
}

@Component
public class MiniRestApp {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @PostConstruct
    public void start() {
        Router.register(new TestApi());
        new Thread(this::runServer, "MiniRestApp-Thread").start();
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
                            key.interestOps(0); // 取消监听，防止重复触发
                            executor.submit(() -> handleRead(key));
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

    private void handleRead(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();

            int readBytes = client.read(buffer);
            if (readBytes == -1) {
                client.close();
                return;
            }

            String requestText = extractRequest(buffer);
            if (requestText == null) return;

            System.out.println("📥 请求内容：\n" + requestText);

            String[] requestLine = requestText.split("\\r\\n")[0].split(" ");
            String method = requestLine[0];
            String path = requestLine[1];

            String body = "";
            int idx = requestText.indexOf("\r\n\r\n");
            if (idx >= 0) body = requestText.substring(idx + 4);

            HttpRequest req = new HttpRequest(method, path, body);
            RouteHandler handler = Router.getHandler(method, path);

            String responseBody;
            if (handler != null) {
                Object result = handler.invoke(req);
                responseBody = result.toString();
            } else {
                responseBody = "404 Not Found";
            }

            byte[] responseBytes = buildHttpResponse(responseBody, "application/json; charset=UTF-8");
            client.write(ByteBuffer.wrap(responseBytes));
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
}

