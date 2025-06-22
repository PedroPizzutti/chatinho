package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.model.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserCreatedDto(String login, String nickname, LocalDateTime createdAt) {
    public static UserCreatedDto fromUser(User user) {
        return UserCreatedDto.builder()
                .login(user.getLogin())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
