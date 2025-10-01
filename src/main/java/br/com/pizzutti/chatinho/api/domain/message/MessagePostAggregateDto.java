package br.com.pizzutti.chatinho.api.domain.message;

import br.com.pizzutti.chatinho.api.domain.room.RoomGetDto;
import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import lombok.Builder;

@Builder
public record MessagePostAggregateDto(
        String content,
        String type,
        RoomGetDto room,
        UserGetDto user
) {}
