package br.com.pizzutti.chatws.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RoomAggregateDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        UserDto owner,
        List<UserDto> members
) {}
