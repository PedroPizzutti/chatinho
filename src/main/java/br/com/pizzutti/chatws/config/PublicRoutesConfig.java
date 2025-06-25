package br.com.pizzutti.chatws.config;

import java.util.List;

public class PublicRoutesConfig {
    public static final List<String> PATHS = List.of(
            "/v1/status",
            "/v1/auth/**",
            "/v1/ws/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    );
}
