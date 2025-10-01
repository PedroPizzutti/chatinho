package br.com.pizzutti.chatinho.api.domain.token;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenGetDto(
        String accessToken,
        String refreshToken,
        String tokenType,
        LocalDateTime expiresAt){
}
