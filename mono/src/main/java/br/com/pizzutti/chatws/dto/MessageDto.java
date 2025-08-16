package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.model.Message;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageDto(
        Long idRoom,
        Long idUser,
        String content,
        String type,
        LocalDateTime createdAt
) {
    public static MessageDto fromMessage(Message message) {
        return MessageDto.builder()
                .content(message.getContent())
                .type(message.getType())
                .idRoom(message.getIdRoom())
                .idUser(message.getIdUser())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
