package br.com.pizzutti.chatws.dto;

public record TokenDto (
        String accessToken,
        String refreshToken,
        String tokenType,
        Integer expiresIn){
}
