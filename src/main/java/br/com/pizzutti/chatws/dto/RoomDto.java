package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.model.Room;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoomDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        Long owner
) {
    public static RoomDto fromRoom(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(room.getOwner())
                .createdAt(room.getCreatedAt())
                .build();
    }
}
