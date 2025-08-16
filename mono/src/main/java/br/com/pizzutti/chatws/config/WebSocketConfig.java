package br.com.pizzutti.chatws.config;

import br.com.pizzutti.chatws.component.JwtHandshakeInterceptorComponent;
import br.com.pizzutti.chatws.component.WebSocketHandlerComponent;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandlerComponent webSocketHandlerComponent;
    private final JwtHandshakeInterceptorComponent jwtHandshakeInterceptorComponent;

    public WebSocketConfig(WebSocketHandlerComponent webSocketHandlerComponent,
                           JwtHandshakeInterceptorComponent jwtHandshakeInterceptorComponent) {
        this.webSocketHandlerComponent = webSocketHandlerComponent;
        this.jwtHandshakeInterceptorComponent = jwtHandshakeInterceptorComponent;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.webSocketHandlerComponent, "v1/ws/chat")
                .addInterceptors(this.jwtHandshakeInterceptorComponent)
                .setAllowedOrigins("*");
    }
}
