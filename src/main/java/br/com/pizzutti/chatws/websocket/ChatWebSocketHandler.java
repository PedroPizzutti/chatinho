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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public ChatWebSocketHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

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
            var msgDto = MessageDto.builder()
                    .sender(this.getNickFromSession(session))
                    .content(message)
                    .timeStamp(LocalDateTime.now())
                    .build();
            return new TextMessage(mapper.writeValueAsString(msgDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter preparar mensagem: " + e.getMessage());
        }
    }

    private String getUserFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("user");
    }

    private String getNickFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("nick");
    }
}
