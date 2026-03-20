package br.com.clinicamedagil_backend.demo.controller.dto.auth;

/**
 * AuthUserContextDTO.record
 *
 * DTO com dados de contexto do usuário autenticado.
 */
public record AuthUserContextDTO(
        String nome,
        String cpf,
        String email,
        String role
) {
}
