package br.com.pizzutti.chatws.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenDto (
        String accessToken,
        String refreshToken,
        String tokenType,
        LocalDateTime expiresAt){
}
