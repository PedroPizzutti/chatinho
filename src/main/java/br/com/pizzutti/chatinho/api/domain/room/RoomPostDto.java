package br.com.pizzutti.chatinho.api.domain.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoomPostDto(
        @NotBlank(message = "nome da sala não pode ser vazio")
        @Size(max = 30, min = 3, message = "nome da sala deve ter entre 3 e 30 caracteres")
        String name
) {
}
