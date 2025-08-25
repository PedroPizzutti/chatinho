package br.com.pizzutti.chatinho.api.domain.token;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenDto (
        String accessToken,
        String refreshToken,
        String tokenType,
        LocalDateTime expiresAt){
}
