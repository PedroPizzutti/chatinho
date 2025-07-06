package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.dto.MessageInputDto;
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
        this.sessionRooms.forEach((idRoom, setSession) -> {
            if (setSession.remove(session)) {
                if (setSession.isEmpty()) {
                    this.sessionRooms.remove(idRoom);
                }
            }
        });
    }

    public void handleMsg(WebSocketSession session, TextMessage message) {
        var messageDto = this.getMessageDto(message.getPayload());
        this.propagateMessage(messageDto);
    }

    private void propagateMessage(MessageDto messageDto) {
        this.rabbitTemplate.convertAndSend(messageDto);
        var textMessage = this.prepareTextMessage(messageDto);
        var sessions = this.sessionRooms.get(messageDto.idRoom());
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) continue;
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                System.err.println("Falha ao enviar mensagem para sessão " + session.getId());
            }
        }
    }

    private TextMessage prepareTextMessage(MessageDto messageDto) {
        try {
            return new TextMessage(objectMapper.writeValueAsString(messageDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageDto getMessageDto(String payload) {
        try {
            var messageInputDto = objectMapper.readValue(payload, MessageInputDto.class);
            return MessageDto.builder()
                    .createdAt(TimeComponent.getInstance().now())
                    .type(messageInputDto.type())
                    .idRoom(messageInputDto.idRoom())
                    .idUser(messageInputDto.idUser())
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
