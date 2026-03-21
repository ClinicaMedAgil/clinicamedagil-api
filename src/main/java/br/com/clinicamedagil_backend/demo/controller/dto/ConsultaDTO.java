package br.com.clinicamedagil_backend.demo.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

        /** Nome do paciente (tb_usuario.nome). */
        String nomePaciente,

        LocalDateTime dataConsulta,

        String queixaPrincipal,

        String historiaDoencaAtual,

        String diagnostico,

        String prescricao,

        String observacoes,

        /** AGENDADA | FINALIZADA — regra de cancelamento/edição baseada neste campo. */
        String statusConsulta,

        /** Id do horário da agenda (tb_horario_agenda). */
        Long horarioId,

        /** Id da agenda do dia (tb_agenda_medico). */
        Long agendaId,

        /** Início do slot reservado. */
        LocalTime horaInicio,

        /** Fim do slot reservado. */
        LocalTime horaFim,

        /** Data do dia de agenda (calendário). */
        LocalDate dataAgenda,

        /** Nome do médico (tb_usuario.nome). */
        String nomeMedico,

        /** Nome da especialidade da agenda. */
        String nomeEspecialidade,

        /** Id da especialidade da agenda. */
        Long especialidadeId

) {}
