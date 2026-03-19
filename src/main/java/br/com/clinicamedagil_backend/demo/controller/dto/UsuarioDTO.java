package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * UsuarioDTO.record
 *
 * DTO resposavel pelo Usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record UsuarioDTO(

        Long id,

        @NotBlank(message = "Nome obrigatório")
        @Size(min = 3, max = 150)
        String nome,

        @NotBlank(message = "CPF obrigatório")
        String cpf,

        @Email(message = "Email inválido")
        @NotBlank(message = "Email obrigatório")
        String email,

        @NotBlank(message = "Senha obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        String telefone,

        String status,

        Long tipoUsuarioId,

        Long perfilId,

        Long nivelAcessoId

) {}
