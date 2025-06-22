package br.com.pizzutti.chatws.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotBlank(message = "totem não pode estar vazio")
        @Size(min = 36, max = 36, message = "totem deve ter exatamente 36 caracteres")
        String totem,

        @NotBlank(message = "e-mail não pode estar vazio")
        @Email(message = "E-mail inválido!")
        String login,

        @NotBlank(message = "nick não pode estar vazio")
        @Size(min = 3, max = 20, message = "nick deve ter entre 3 e 20 caracteres")
        String nickname,

        @NotBlank(message = "senha não pode estar vazia")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&^_-])[A-Za-z\\d@$!%*#?&^_-]{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, com letras maiúsculas, minúsculas, números e símbolos"
        )
        String password
) {
}
