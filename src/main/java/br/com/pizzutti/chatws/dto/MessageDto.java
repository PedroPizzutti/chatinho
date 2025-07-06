package br.com.pizzutti.chatws.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
        Long idRoom,
        Long idUser,
        String nick,
        String content,
        String type,
        LocalDateTime createdAt
) {}
