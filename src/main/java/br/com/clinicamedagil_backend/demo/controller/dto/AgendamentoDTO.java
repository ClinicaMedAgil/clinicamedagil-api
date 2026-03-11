package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * AgendamentoDTO.record
 *
 * DTO resposavel pelo Agendamento
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record AgendamentoDTO(

        Long id,

        @NotNull
        Long horarioId,

        @NotNull
        Long pacienteId,

        LocalDateTime dataMarcacao,

        String statusAgendamento

) {}
