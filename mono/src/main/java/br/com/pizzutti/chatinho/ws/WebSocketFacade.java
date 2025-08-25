package br.com.pizzutti.chatinho.ws;

import br.com.pizzutti.chatinho.api.domain.message.MessageAggregateDto;
import br.com.pizzutti.chatinho.api.domain.message.MessageDto;
import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import br.com.pizzutti.chatinho.api.domain.message.MessageAggregateInputDto;
import br.com.pizzutti.chatinho.api.domain.room.RoomFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketFacade {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final Map<Long, Set<WebSocketSession>> sessionRooms;
    private final RoomFacade roomFacade;

    public WebSocketFacade(ObjectMapper objectMapper,
                           RabbitTemplate rabbitTemplate,
                           RoomFacade roomFacade) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.sessionRooms = new ConcurrentHashMap<>();
        this.roomFacade = roomFacade;
    }

    public void connect(WebSocketSession session) {
        var userRooms = this.roomFacade.findAllByUser(this.getUserFromSession(session));
        userRooms.forEach(userRoom -> {
            this.sessionRooms.computeIfAbsent(userRoom.id(), r -> Collections.synchronizedSet(new HashSet<>())).add(session);
        });
    }

    public void disconnect(WebSocketSession session, CloseStatus status) {
        List<Long> roomsToRemove = new ArrayList<>();
        this.sessionRooms.forEach((idRoom, setSession) -> {
            if (setSession.remove(session) && setSession.isEmpty()) {
                roomsToRemove.add(idRoom);
            }
        });
        roomsToRemove.forEach(sessionRooms::remove);
    }

    public void handleMsg(WebSocketSession session, TextMessage message) {
        var messageAggregateDto = this.getMessageAggregateDto(message.getPayload());
        this.propagateMessage(messageAggregateDto);
    }

    private void propagateMessage(MessageAggregateDto messageAggregateDto) {
        this.rabbitTemplate.convertAndSend(MessageDto.fromMessageAggregateDto(messageAggregateDto));
        var textMessage = this.prepareTextMessage(messageAggregateDto);
        var sessions = this.sessionRooms.get(messageAggregateDto.room().id());
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) continue;
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                System.err.println("Falha ao enviar mensagem para sessão " + session.getId());
            }
        }
    }

    private TextMessage prepareTextMessage(MessageAggregateDto messageAggregateDto) {
        try {
            return new TextMessage(objectMapper.writeValueAsString(messageAggregateDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageAggregateDto getMessageAggregateDto(String payload) {
        try {
            var messageInputDto = objectMapper.readValue(payload, MessageAggregateInputDto.class);
            return MessageAggregateDto.builder()
                    .createdAt(TimeService.now())
                    .type(messageInputDto.type())
                    .room(messageInputDto.room())
                    .user(messageInputDto.user())
                    .content(messageInputDto.content())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Long getUserFromSession(WebSocketSession session) {
        return Long.parseLong((String) session.getAttributes().get("user"));
    }
}
