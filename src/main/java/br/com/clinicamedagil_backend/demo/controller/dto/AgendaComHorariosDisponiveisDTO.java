package br.com.clinicamedagil_backend.demo.controller.dto;

import java.util.List;

/**
 * Agenda do médico com os horários livres para exibição no fluxo de agendamento do paciente.
 */
public record AgendaComHorariosDisponiveisDTO(
        AgendaMedicoDTO agenda,
        List<HorarioAgendaDTO> horariosDisponiveis
) {
}
