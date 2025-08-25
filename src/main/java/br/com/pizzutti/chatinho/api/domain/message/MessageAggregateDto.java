package br.com.pizzutti.chatinho.api.domain.message;

import br.com.pizzutti.chatinho.api.domain.room.RoomDto;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageAggregateDto(
        String content,
        String type,
        LocalDateTime createdAt,
        RoomDto room,
        UserDto user
) { }
