package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.AgendaComHorariosDisponiveisDTO;
import br.com.clinicamedagil_backend.demo.service.PacienteAgendamentoCatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Agenda e horários para o paciente marcar consulta: somente agendas ativas (publicadas) e horários livres.
 */
@RestController
@RequestMapping("/clinicamedagil-service/paciente/agendas")
@RequiredArgsConstructor
@Tag(name = "Paciente — agenda médica", description = "Consulta de agendas ativas para agendamento")
public class PacienteAgendaController {

    private final PacienteAgendamentoCatalogoService catalogoService;

    @GetMapping("/medicos/{medicoId}/horarios-disponiveis")
    @Operation(
            summary = "Quadro de horários (agenda ativa)",
            description = "Agendas com status ATIVA (ou DISPONIVEL legado), data a partir de hoje, com horários DISPONIVEL.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('USUARIO') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<List<AgendaComHorariosDisponiveisDTO>> horariosPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(catalogoService.listarAgendasComHorariosDisponiveisPorMedico(medicoId));
    }

    @GetMapping("/especialidades/{especialidadeId}/medicos/{medicoId}/horarios-disponiveis")
    @Operation(
            summary = "Agendas ativas por especialidade e médico",
            description = "Mesmo critério do endpoint por médico, restrito à especialidade e ao vínculo médico–especialidade.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('USUARIO') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<List<AgendaComHorariosDisponiveisDTO>> horariosPorEspecialidadeEMedico(
            @PathVariable Long especialidadeId,
            @PathVariable Long medicoId) {
        return ResponseEntity.ok(catalogoService.listarAgendasComHorariosDisponiveis(especialidadeId, medicoId));
    }
}
