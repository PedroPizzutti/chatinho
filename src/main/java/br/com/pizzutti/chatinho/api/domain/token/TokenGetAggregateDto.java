package br.com.pizzutti.chatinho.api.domain.token;

import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import lombok.Builder;

@Builder
public record TokenGetAggregateDto(UserGetDto user, TokenGetDto token) {}
