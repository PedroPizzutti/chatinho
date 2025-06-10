package br.com.pizzutti.chatws.dto;

public record TokenDto (String accessToken, String tokenType, Integer expiresIn){
}
