package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * ConsultaDTO.record
 *
 * DTO resposavel pelo Consulta
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record ConsultaDTO(

        Long id,

        @NotNull
        Long agendamentoId,

        @NotNull
        Long medicoId,

        @NotNull
        Long pacienteId,

        LocalDateTime dataConsulta,

        String queixaPrincipal,

        String historiaDoencaAtual,

        String diagnostico,

        String prescricao,

        String observacoes

) {}
