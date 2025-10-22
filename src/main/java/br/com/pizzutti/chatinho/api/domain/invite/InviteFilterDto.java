package br.com.pizzutti.chatinho.api.domain.invite;

public record InviteFilterDto(Long idUserTo, Long idUserFrom, InviteStatusEnum status) {
}
