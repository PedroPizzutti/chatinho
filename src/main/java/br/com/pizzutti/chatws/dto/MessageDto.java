package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.enums.MessageEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
        String room,
        String user,
        String nick,
        String content,
        MessageEnum type,
        LocalDateTime createdAt
) {}
