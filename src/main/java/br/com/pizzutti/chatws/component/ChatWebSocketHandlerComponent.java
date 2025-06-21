package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.facade.WebSocketFacade;
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

    private final WebSocketFacade webSocketFacade;

    public ChatWebSocketHandlerComponent(WebSocketFacade webSocketFacade) {
        this.webSocketFacade = webSocketFacade;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.webSocketFacade.connect(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        this.webSocketFacade.handleMsg(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.webSocketFacade.disconnect(session, status);
    }
}
