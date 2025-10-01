package br.com.pizzutti.chatinho.api.domain.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserGetDto(
        Long id,
        String login,
        String nickname,
        LocalDateTime createdAt
) {
    public static UserGetDto fromUser(User user) {
        return UserGetDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
