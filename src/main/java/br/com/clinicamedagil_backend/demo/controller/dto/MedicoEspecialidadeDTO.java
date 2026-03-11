package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotNull;

/**
 * MedicoEspecialidadeDTO.record
 *
 * DTO resposavel pelo MedicoEspecialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record MedicoEspecialidadeDTO(

        @NotNull
        Long medicoId,

        @NotNull
        Long especialidadeId

) {}
