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

        String telefone,

        String status,

        Long tipoUsuarioId,

        Long perfilId,

        Long nivelAcessoId

) {}
