package br.com.pizzutti.chatws.websocket;

import br.com.pizzutti.chatws.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.sessions.add(session);
        this.sendMessage(this.prepareMessage(session, "se conectou"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        this.sendMessage(this.prepareMessage(session, message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.sessions.remove(session);
        this.sendMessage(this.prepareMessage(session, "se desconectou"));
    }

    private void sendMessage(TextMessage message) {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) continue;
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao enviar msg: " + e.getMessage());
            }
        }
    }

    private TextMessage prepareMessage(WebSocketSession session, String message) {
        try {
            var payload = objectMapper.writeValueAsString(new MessageDto(session.getId(), message));
            return new TextMessage(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter preparar mensagem: " + e.getMessage());
        }
    }
}
