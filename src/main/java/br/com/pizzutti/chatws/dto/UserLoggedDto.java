package br.com.pizzutti.chatws.dto;

import lombok.Builder;

@Builder
public record UserLoggedDto(UserCreatedDto user, TokenDto token) {}
