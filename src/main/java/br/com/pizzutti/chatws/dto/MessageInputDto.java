package br.com.pizzutti.chatws.dto;

import lombok.Builder;

@Builder
public record MessageInputDto(
        RoomDto room,
        UserDto user,
        String content,
        String type
) {}
