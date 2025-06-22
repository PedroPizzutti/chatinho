package br.com.pizzutti.chatws.dto;

import br.com.pizzutti.chatws.enums.AdviceEnum;
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
