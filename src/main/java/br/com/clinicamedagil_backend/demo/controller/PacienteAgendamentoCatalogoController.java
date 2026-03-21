package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.AgendaComHorariosDisponiveisDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.EspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.MedicoEspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.EspecialidadeMapper;
import br.com.clinicamedagil_backend.demo.controller.mapper.MedicoEspecialidadeMapper;
import br.com.clinicamedagil_backend.demo.service.PacienteAgendamentoCatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Catálogo de especialidades, médicos e horários para o paciente agendar consulta.
 * Exige apenas usuário autenticado (JWT), sem restrição de perfil.
 */
@RestController
@RequestMapping("/clinicamedagil-service/paciente/agendamento-catalogo")
@RequiredArgsConstructor
@Tag(name = "Paciente — catálogo de agendamento", description = "Dados para o paciente montar uma consulta")
public class PacienteAgendamentoCatalogoController {

    private final PacienteAgendamentoCatalogoService catalogoService;
    private final EspecialidadeMapper especialidadeMapper;
    private final MedicoEspecialidadeMapper medicoEspecialidadeMapper;

    @GetMapping("/especialidades")
    @Operation(summary = "Listar especialidades", description = "Lista todas as especialidades (usuário logado).")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EspecialidadeDTO>> listarEspecialidades() {
        List<EspecialidadeDTO> lista = catalogoService.listarEspecialidades().stream()
                .map(especialidadeMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/especialidades/{id}")
    @Operation(summary = "Buscar especialidade por id", description = "Detalhe de uma especialidade.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EspecialidadeDTO> buscarEspecialidade(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeMapper.toDTO(catalogoService.buscarEspecialidade(id)));
    }

    @GetMapping("/especialidades/{especialidadeId}/medicos")
    @Operation(summary = "Médicos da especialidade", description = "Médicos vinculados à especialidade (por id).")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MedicoEspecialidadeDTO>> listarMedicosPorEspecialidade(
            @PathVariable Long especialidadeId) {
        List<MedicoEspecialidadeDTO> lista = catalogoService.listarMedicosPorEspecialidade(especialidadeId).stream()
                .map(medicoEspecialidadeMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/medicos-por-nome-especialidade")
    @Operation(
            summary = "Médicos por nome da especialidade",
            description = "Busca a especialidade pelo nome (exato, ignorando maiúsculas; ou parcial se for único) e lista os médicos vinculados.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MedicoEspecialidadeDTO>> listarMedicosPorNomeEspecialidade(
            @RequestParam("nome") String nomeEspecialidade) {
        List<MedicoEspecialidadeDTO> lista = catalogoService.listarMedicosPorNomeEspecialidade(nomeEspecialidade).stream()
                .map(medicoEspecialidadeMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/medicos/{medicoId}/horarios-disponiveis")
    @Operation(
            summary = "Quadro de horários por médico",
            description = "Agendas ativas (ATIVA ou DISPONIVEL legado), data a partir de hoje, com horários DISPONIVEL.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AgendaComHorariosDisponiveisDTO>> listarHorariosDisponiveisPorMedico(
            @PathVariable Long medicoId) {
        return ResponseEntity.ok(catalogoService.listarAgendasComHorariosDisponiveisPorMedico(medicoId));
    }

    @GetMapping("/especialidades/{especialidadeId}/medicos/{medicoId}/horarios-disponiveis")
    @Operation(
            summary = "Agendas e horários disponíveis",
            description = "Agendas ativas do médico na especialidade (data >= hoje) e horários DISPONIVEL.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AgendaComHorariosDisponiveisDTO>> listarHorariosDisponiveis(
            @PathVariable Long especialidadeId,
            @PathVariable Long medicoId) {
        return ResponseEntity.ok(catalogoService.listarAgendasComHorariosDisponiveis(especialidadeId, medicoId));
    }
}
