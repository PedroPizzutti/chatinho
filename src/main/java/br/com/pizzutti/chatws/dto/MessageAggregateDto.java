package br.com.pizzutti.chatws.dto;

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
