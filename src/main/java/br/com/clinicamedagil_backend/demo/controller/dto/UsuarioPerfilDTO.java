package br.com.clinicamedagil_backend.demo.controller.dto;

/**
 * UsuarioPerfilDTO.record
 *
 * DTO responsável pelo retorno de perfil do usuário autenticado
 */
public record UsuarioPerfilDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        String status,
        Long tipoUsuarioId,
        String tipoUsuarioNome,
        Long perfilId,
        String perfilNome,
        Long nivelAcessoId,
        String nivelAcessoNome
) {
}
