package br.com.pizzutti.chatinho.api.domain.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDto(
        Long id,
        String login,
        String nickname,
        LocalDateTime createdAt
) {
    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
