package com.sh.my.socket.bean;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TcpClientRunner {

    private final TcpClient tcpClient;

    public TcpClientRunner(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        String response = tcpClient.sendMessage("Hello from Spring TCP client!");
        System.out.println("Server response: " + response);
    }

    public void sendMessage(String message) {
        String response = tcpClient.sendMessage(message);
        System.out.println("Server response: " + response);
    }

}


