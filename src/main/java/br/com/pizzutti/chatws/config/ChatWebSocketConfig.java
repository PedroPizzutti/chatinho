package br.com.pizzutti.chatws.config;

import br.com.pizzutti.chatws.websocket.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class ChatWebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final JwtHandshakeInterceptorConfig jwtHandshakeInterceptorConfig;

    public ChatWebSocketConfig(ChatWebSocketHandler chatWebSocketHandler,
                               JwtHandshakeInterceptorConfig jwtHandshakeInterceptorConfig) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.jwtHandshakeInterceptorConfig = jwtHandshakeInterceptorConfig;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.chatWebSocketHandler, "v1/ws/chat")
                .addInterceptors(this.jwtHandshakeInterceptorConfig)
                .setAllowedOrigins("*");
    }
}
