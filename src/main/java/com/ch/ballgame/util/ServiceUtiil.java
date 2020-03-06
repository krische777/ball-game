package com.ch.ballgame.util;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class ServiceUtiil {
    public static void sendMessage(WebSocketSession webSocketSession, String data) {
        if (webSocketSession != null) {
            synchronized (webSocketSession) {
                try {
                    if (webSocketSession.isOpen()) {
                        webSocketSession.sendMessage(new TextMessage(data));
                    }
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
