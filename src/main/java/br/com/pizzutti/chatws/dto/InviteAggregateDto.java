package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.enums.InviteStatusEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InviteAggregateDto(
        Long id,
        LocalDateTime createdAt,
        InviteStatusEnum status,
        UserDto from,
        UserDto to,
        RoomDto room
) {}
