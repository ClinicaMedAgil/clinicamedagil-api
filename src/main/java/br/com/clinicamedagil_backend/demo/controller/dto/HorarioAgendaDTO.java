package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

/**
 * HorarioAgendaDTO.record
 *
 * DTO resposavel pelo HorarioAgenda
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record HorarioAgendaDTO(

        Long id,

        @NotNull
        Long agendaId,

        @NotNull
        LocalTime horaInicio,

        @NotNull
        LocalTime horaFim,

        String statusHorario

) {}
