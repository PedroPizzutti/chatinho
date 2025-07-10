package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.facade.AuthFacade;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptorComponent implements HandshakeInterceptor {

    private final AuthFacade authFacade;

    public JwtHandshakeInterceptorComponent(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) return false;
        var httpServletRequest = servletRequest.getServletRequest();
        var user = this.authFacade.loginWebSocket(httpServletRequest.getParameter("token"));
        attributes.put("user", user.getId().toString());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }
}
