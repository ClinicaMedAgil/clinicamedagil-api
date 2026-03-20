package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarMeuPerfilDTO(
        @NotBlank(message = "Nome obrigatório")
        @Size(min = 3, max = 150)
        String nome,

        @NotBlank(message = "CPF obrigatório")
        String cpf,

        @Email(message = "Email inválido")
        @NotBlank(message = "Email obrigatório")
        String email,

        String telefone
) {
}
