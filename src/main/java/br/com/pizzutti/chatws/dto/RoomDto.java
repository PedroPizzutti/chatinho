package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.model.Room;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoomDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        Long idOwner
) {
    public static RoomDto fromRoom(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .idOwner(room.getIdOwner())
                .createdAt(room.getCreatedAt())
                .build();
    }
}
