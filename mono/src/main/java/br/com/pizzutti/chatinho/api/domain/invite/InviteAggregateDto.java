package br.com.pizzutti.chatinho.api.domain.invite;

import br.com.pizzutti.chatinho.api.domain.room.RoomDto;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
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
