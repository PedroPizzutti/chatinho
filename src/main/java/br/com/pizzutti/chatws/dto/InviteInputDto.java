package br.com.pizzutti.chatws.dto;

import jakarta.validation.constraints.NotNull;

public record InviteInputDto(
        @NotNull Long idUserFrom,
        @NotNull Long idUserTo,
        @NotNull Long idRoom) {
}
