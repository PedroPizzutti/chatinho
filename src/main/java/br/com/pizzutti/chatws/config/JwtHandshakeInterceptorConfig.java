package br.com.pizzutti.chatws.config;

import br.com.pizzutti.chatws.service.TokenService;
import br.com.pizzutti.chatws.service.UserServiceFacade;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptorConfig implements HandshakeInterceptor {

    private final UserServiceFacade userServiceFacade;

    public JwtHandshakeInterceptorConfig(UserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) return false;
        var httpServletRequest = servletRequest.getServletRequest();
        var user = this.userServiceFacade.login(httpServletRequest.getParameter("token"));
        attributes.put("user", user.getLogin());
        attributes.put("nick", user.getNickname());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }
}
