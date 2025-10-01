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

    public static MessageDto fromMessageAggregateDto(MessageGetAggregateDto messageGetAggregateDto) {
        return MessageDto.builder()
                .content(messageGetAggregateDto.content())
                .type(messageGetAggregateDto.type())
                .idRoom(messageGetAggregateDto.room().id())
                .idUser(messageGetAggregateDto.user().id())
                .createdAt(messageGetAggregateDto.createdAt())
                .build();
    }
}
