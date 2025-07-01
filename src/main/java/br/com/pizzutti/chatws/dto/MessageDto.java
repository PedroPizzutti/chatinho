package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.enums.MessageEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
        Long room,
        Long user,
        String nick,
        String content,
        String type,
        LocalDateTime createdAt
) {}
