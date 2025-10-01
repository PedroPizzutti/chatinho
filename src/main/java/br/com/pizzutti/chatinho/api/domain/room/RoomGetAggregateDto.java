package br.com.pizzutti.chatinho.api.domain.room;

import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RoomGetAggregateDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        UserGetDto owner,
        List<UserGetDto> members
) {}
