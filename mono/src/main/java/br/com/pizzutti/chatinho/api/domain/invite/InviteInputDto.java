package br.com.pizzutti.chatinho.api.domain.invite;

import jakarta.validation.constraints.NotNull;

public record InviteInputDto(
        @NotNull Long idUserFrom,
        @NotNull Long idUserTo,
        @NotNull Long idRoom) {
}
