package br.com.pizzutti.chatinho.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketFacade webSocketFacade;

    public WebSocketHandler(WebSocketFacade webSocketFacade) {
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
