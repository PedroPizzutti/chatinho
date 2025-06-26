package br.com.pizzutti.chatws.dto;

import lombok.Builder;

@Builder
public record TokenDto (
        String accessToken,
        String refreshToken,
        String tokenType,
        Integer expiresIn){
}
