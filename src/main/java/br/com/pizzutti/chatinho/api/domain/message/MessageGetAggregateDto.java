package br.com.pizzutti.chatinho.api.domain.message;

import br.com.pizzutti.chatinho.api.domain.room.RoomGetDto;
import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageGetAggregateDto(
        String content,
        String type,
        LocalDateTime createdAt,
        RoomGetDto room,
        UserGetDto user
) { }
