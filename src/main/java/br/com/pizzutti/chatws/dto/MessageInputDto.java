package br.com.pizzutti.chatws.dto;

import lombok.Builder;

@Builder
public record MessageInputDto(
        Long idRoom,
        Long idUser,
        String content,
        String type
) {}
