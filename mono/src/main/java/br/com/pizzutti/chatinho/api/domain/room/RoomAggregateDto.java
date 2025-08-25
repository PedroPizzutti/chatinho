package br.com.pizzutti.chatinho.api.domain.room;

import br.com.pizzutti.chatinho.api.domain.user.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RoomAggregateDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        UserDto owner,
        List<UserDto> members
) {}
