package br.com.pizzutti.chatinho.api.domain.room;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoomGetDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        Long idOwner
) {
    public static RoomGetDto fromRoom(Room room) {
        return RoomGetDto.builder()
                .id(room.getId())
                .name(room.getName())
                .idOwner(room.getIdOwner())
                .createdAt(room.getCreatedAt())
                .build();
    }
}
