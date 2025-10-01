package br.com.pizzutti.chatinho.api.domain.user;

import br.com.pizzutti.chatinho.api.domain.token.TokenGetDto;
import lombok.Builder;

@Builder
public record UserGetAggregateDto(UserGetDto user, TokenGetDto token) {}
