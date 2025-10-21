package br.com.pizzutti.chatinho.api.domain.invite;

import jakarta.validation.constraints.NotNull;

public record InvitePostDto(
        @NotNull Long idUserTo,
        @NotNull Long idRoom) {
}
