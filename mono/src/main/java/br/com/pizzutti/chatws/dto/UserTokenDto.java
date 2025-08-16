package br.com.pizzutti.chatws.dto;

import lombok.Builder;

@Builder
public record UserTokenDto(UserDto user, TokenDto token) {}
