package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.facade.UserFacade;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptorComponent implements HandshakeInterceptor {

    private final UserFacade userServiceFacade;

    public JwtHandshakeInterceptorComponent(UserFacade userFacade) {
        this.userServiceFacade = userFacade;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) return false;
        var httpServletRequest = servletRequest.getServletRequest();
        var user = this.userServiceFacade.loginWebSocket(httpServletRequest.getParameter("token"));
        var room = "1";
        attributes.put("user", user.getLogin());
        attributes.put("nick", user.getNickname());
        attributes.put("room", room);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }
}
