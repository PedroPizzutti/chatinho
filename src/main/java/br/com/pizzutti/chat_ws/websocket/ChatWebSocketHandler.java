package br.com.pizzutti.chat_ws.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.sessions.add(session);
        this.sendMessage(new TextMessage(session.getId() + " se conectou"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        this.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.sessions.remove(session);
        this.sendMessage(new TextMessage(session.getId() + " se desconectou"));
    }

    private void sendMessage(TextMessage message) {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(message);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao enviar msg: " + e.getMessage());
                }
            }
        }
    }
}
