package br.com.pizzutti.chatinho.api.domain.message;

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

    public static MessageDto fromMessageAggregateDto(MessageAggregateDto messageAggregateDto) {
        return MessageDto.builder()
                .content(messageAggregateDto.content())
                .type(messageAggregateDto.type())
                .idRoom(messageAggregateDto.room().id())
                .idUser(messageAggregateDto.user().id())
                .createdAt(messageAggregateDto.createdAt())
                .build();
    }
}
