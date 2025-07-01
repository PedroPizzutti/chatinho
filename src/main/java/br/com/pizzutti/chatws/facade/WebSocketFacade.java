package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.enums.MessageEnum;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketFacade {

    private final ObjectMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final Map<Long, Set<WebSocketSession>> rooms;

    public WebSocketFacade(ObjectMapper mapper,
                           RabbitTemplate rabbitTemplate) {
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
        this.rooms = new ConcurrentHashMap<>();
    }

    public void connect(WebSocketSession session) {
        var roomId = this.getRoomFromSession(session);
        this.rooms.computeIfAbsent(roomId, r -> Collections.synchronizedSet(new HashSet<>())).add(session);
        this.sendMessage(this.prepareMessageDto(session, "", MessageEnum.LOG_IN));
    }

    public void disconnect(WebSocketSession session, CloseStatus status) {
        var roomId = this.getRoomFromSession(session);
        var sessions = this.rooms.get(roomId);
        this.sendMessage(this.prepareMessageDto(session, "", MessageEnum.LOG_OUT));
        sessions.remove(session);
        if (sessions.isEmpty()) {
            this.rooms.remove(roomId);
        };
    }

    public void handleMsg(WebSocketSession session, TextMessage message) {
        this.sendMessage(this.prepareMessageDto(session, message.getPayload(), MessageEnum.MSG));
    }

    private void sendMessage(MessageDto messageDto) {
        this.rabbitTemplate.convertAndSend(messageDto);
        var textMessage = this.prepareTextMessage(messageDto);
        var sessions = this.rooms.get(messageDto.room());
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

    private MessageDto prepareMessageDto(WebSocketSession session, String message, MessageEnum type) {
        return MessageDto.builder()
                .room(this.getRoomFromSession(session))
                .user(this.getUserFromSession(session))
                .nick(this.getNickFromSession(session))
                .type(type.asString())
                .content(message)
                .createdAt(TimeComponent.getInstance().now())
                .build();
    }

    private Long getUserFromSession(WebSocketSession session) {
        return Long.parseLong((String) session.getAttributes().get("user"));
    }

    private String getNickFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("nick");
    }

    private Long getRoomFromSession(WebSocketSession session) {
        return Long.parseLong((String) session.getAttributes().get("room"));
    }

}
