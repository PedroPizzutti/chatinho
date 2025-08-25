package br.com.pizzutti.chatinho.api.infra.config.security;

import java.util.List;

public class PublicRoutes {
    public static final List<String> PATHS = List.of(
            "/v1/status",
            "/v1/user/new",
            "/v1/auth/**",
            "/v1/ws/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    );
}
