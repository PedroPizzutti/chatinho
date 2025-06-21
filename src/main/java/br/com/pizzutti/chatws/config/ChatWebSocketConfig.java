package br.com.pizzutti.chatws.config;

import br.com.pizzutti.chatws.component.JwtHandshakeInterceptorComponent;
import br.com.pizzutti.chatws.component.ChatWebSocketHandlerComponent;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class ChatWebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandlerComponent chatWebSocketHandlerComponent;
    private final JwtHandshakeInterceptorComponent jwtHandshakeInterceptorComponent;

    public ChatWebSocketConfig(ChatWebSocketHandlerComponent chatWebSocketHandlerComponent,
                               JwtHandshakeInterceptorComponent jwtHandshakeInterceptorComponent) {
        this.chatWebSocketHandlerComponent = chatWebSocketHandlerComponent;
        this.jwtHandshakeInterceptorComponent = jwtHandshakeInterceptorComponent;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.chatWebSocketHandlerComponent, "v1/ws/chat")
                .addInterceptors(this.jwtHandshakeInterceptorComponent)
                .setAllowedOrigins("*");
    }
}
