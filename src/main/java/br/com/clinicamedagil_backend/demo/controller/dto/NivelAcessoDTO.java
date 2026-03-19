package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * NivelAcessoDTO.record
 *
 * DTO resposavel pelo NivelAcesso
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record NivelAcessoDTO(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String descricao
) {}
