package br.com.pizzutti.chatws.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserCreatedDto(String login, String nickname, LocalDateTime createdAt) {}
