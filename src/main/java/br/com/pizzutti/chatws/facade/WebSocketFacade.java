package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.component.TimeComponent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class WebSocketFacade {

    private final ObjectMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public WebSocketFacade(ObjectMapper mapper,
                           RabbitTemplate rabbitTemplate) {
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void connect(WebSocketSession session) {
        this.sessions.add(session);
        this.sendMessage(this.prepareMessageDto(session, "se conectou"));
    }

    public void disconnect(WebSocketSession session, CloseStatus status) {
        this.sessions.remove(session);
        this.sendMessage(this.prepareMessageDto(session, "se desconectou"));
    }

    public void handleMsg(WebSocketSession session, TextMessage message) {
        this.sendMessage(this.prepareMessageDto(session, message.getPayload()));
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
                .createdAt(TimeComponent.getInstance().now())
                .build();
    }

    private String getUserFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("user");
    }

    private String getNickFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("nick");
    }

}
