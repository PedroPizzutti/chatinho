package br.com.pizzutti.chatinho.api.domain.user;

import br.com.pizzutti.chatinho.api.domain.token.TokenDto;
import lombok.Builder;

@Builder
public record UserTokenDto(UserDto user, TokenDto token) {}
