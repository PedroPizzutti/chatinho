package br.com.pizzutti.chatinho.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;
    private final WebSocketHandshake webSocketHandshake;

    public WebSocketConfig(WebSocketHandler webSocketHandler,
                           WebSocketHandshake webSocketHandshake) {
        this.webSocketHandler = webSocketHandler;
        this.webSocketHandshake = webSocketHandshake;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.webSocketHandler, "v1/ws/chat")
                .addInterceptors(this.webSocketHandshake)
                .setAllowedOrigins("*");
    }
}
