package br.com.pizzutti.chatws.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdviceDto(LocalDateTime timestamp, Integer status, String error, String path) {
}
