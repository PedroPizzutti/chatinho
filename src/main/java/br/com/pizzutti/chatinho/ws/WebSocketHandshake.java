package br.com.pizzutti.chatinho.ws;

import br.com.pizzutti.chatinho.api.domain.auth.AuthFacade;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketHandshake implements HandshakeInterceptor {

    private final AuthFacade authFacade;

    public WebSocketHandshake(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) return false;
        var httpServletRequest = servletRequest.getServletRequest();
        var token = httpServletRequest.getParameter("token");
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            var user = this.authFacade.loginWebSocket(token);
            attributes.put("user", user.getId().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }
}
