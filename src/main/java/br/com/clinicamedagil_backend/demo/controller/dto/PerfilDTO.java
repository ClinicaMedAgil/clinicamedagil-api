package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * PerfilDTO.record
 *
 * DTO resposavel pelo Perfil
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record PerfilDTO(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String descricao

) {}
