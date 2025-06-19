package br.com.pizzutti.chatws.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotBlank
        @Size(min = 36, max = 36, message = "O totem deve ter exatamente 36 caracteres")
        String totem,

        @NotBlank
        @Email(message = "E-mail inválido!")
        String login,

        @NotBlank
        @Size(min = 3, max = 20, message = "O nick deve ter entre 3 e 20 caracteres")
        String nickname,

        @NotBlank(message = "Senha é obrigatória")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&^_-])[A-Za-z\\d@$!%*#?&^_-]{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, com letras maiúsculas, minúsculas, números e símbolos"
        )
        String password
) {
}
