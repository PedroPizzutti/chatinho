package br.com.pizzutti.chatinho.api.domain.invite;

import br.com.pizzutti.chatinho.api.domain.room.RoomGetDto;
import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InviteGetAggregateDto(
        Long id,
        LocalDateTime createdAt,
        InviteStatusEnum status,
        UserGetDto from,
        UserGetDto to,
        RoomGetDto room
) {}
