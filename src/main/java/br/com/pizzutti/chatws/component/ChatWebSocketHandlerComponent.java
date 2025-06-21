package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.service.TimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public class ChatWebSocketHandlerComponent extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final TimeService timeService;
    private final RabbitTemplate rabbitTemplate;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public ChatWebSocketHandlerComponent(ObjectMapper mapper,
                                         TimeService timeService,
                                         RabbitTemplate rabbitTemplate) {
        this.mapper = mapper;
        this.timeService = timeService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.sessions.add(session);
        this.sendMessage(this.prepareMessageDto(session, "se conectou"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        this.sendMessage(this.prepareMessageDto(session, message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.sessions.remove(session);
        this.sendMessage(this.prepareMessageDto(session, "se desconectou"));
    }

    private void sendMessage(MessageDto messageDto) {
        this.rabbitTemplate.convertAndSend(messageDto);
        var textMessage = this.prepareTextMessage(messageDto);
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) continue;
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao enviar msg: " + e.getMessage());
            }
        }
    }

    private TextMessage prepareTextMessage(MessageDto messageDto) {
        try {
            return new TextMessage(mapper.writeValueAsString(messageDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageDto prepareMessageDto(WebSocketSession session, String message) {
        return MessageDto.builder()
                .user(this.getUserFromSession(session))
                .nick(this.getNickFromSession(session))
                .content(message)
                .createdAt(this.timeService.now())
                .build();
    }

    private String getUserFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("user");
    }

    private String getNickFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("nick");
    }
}
