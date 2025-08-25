package br.com.pizzutti.chatinho.api.infra.config.communication;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AdviceDto(
        LocalDateTime timestamp,
        Integer status,
        List<String> errors,
        AdviceEnum code,
        String path) {
}
