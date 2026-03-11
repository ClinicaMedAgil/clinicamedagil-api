package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * EspecialidadeDTO.record
 *
 * DTO resposavel pelo Especialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record EspecialidadeDTO(
        Long id,

        @NotBlank(message = "Nome da especialidade é obrigatório")
        String nomeEspecialidade,

        String descricao
) {}
