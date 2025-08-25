package dev.pizzutti.chatinho.worker.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
        Long idRoom,
        Long idUser,
        String content,
        String type,
        LocalDateTime createdAt
) {}
