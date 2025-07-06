package br.com.pizzutti.chatws.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageAggregateDto(
        RoomDto room,
        UserDto user,
        String content,
        String type,
        LocalDateTime createdAt
) { }
