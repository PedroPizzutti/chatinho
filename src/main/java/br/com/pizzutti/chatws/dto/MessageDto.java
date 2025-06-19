package br.com.pizzutti.chatws.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
        String user,
        String nick,
        String content,
        LocalDateTime createdAt
) {}
