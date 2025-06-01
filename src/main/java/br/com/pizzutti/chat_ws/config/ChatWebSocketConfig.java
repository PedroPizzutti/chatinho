package br.com.pizzutti.chat_ws.config;

import br.com.pizzutti.chat_ws.websocket.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class ChatWebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatHandlerWebSocket;

    public ChatWebSocketConfig(ChatWebSocketHandler chatHandlerWebSocket) {
        this.chatHandlerWebSocket = chatHandlerWebSocket;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandlerWebSocket, "/ws/chat").setAllowedOrigins("*");
    }
}
