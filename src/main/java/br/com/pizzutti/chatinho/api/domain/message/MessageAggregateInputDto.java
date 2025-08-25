package br.com.pizzutti.chatinho.api.domain.message;

import br.com.pizzutti.chatinho.api.domain.room.RoomDto;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
import lombok.Builder;

@Builder
public record MessageAggregateInputDto(
        String content,
        String type,
        RoomDto room,
        UserDto user
) {}
