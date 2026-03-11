package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * AgendaMedicoDTO.record
 *
 * DTO resposavel pelo AgendaMedico
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record AgendaMedicoDTO(

        Long id,

        @NotNull
        Long medicoId,

        @NotNull
        Long especialidadeId,

        @NotNull
        LocalDate dataAgenda,

        String statusAgenda

) {}
