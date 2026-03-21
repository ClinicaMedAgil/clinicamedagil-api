package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.MedicoEspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.MedicoEspecialidadeMapper;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import br.com.clinicamedagil_backend.demo.service.MedicoEspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UsuarioController.java
 *
 * Classe resposavel pela API de servicos da Especialidade do Médico
 *
 * @author João Paulo - back-end team
 *
 * <pre>
 *     History:
 *         15/03/2026 - feature/controller -  João Paulo
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedagil-service/medicoespecialidade")
@RequiredArgsConstructor
@Tag(name="MedicosEspecialidades", description="Gerenciamentos de Medicos Especialistas")
public class MedicoEspecialidadeController {

    private final MedicoEspecialidadeService service;
    private final MedicoEspecialidadeMapper mapper;

    @GetMapping
    @Operation(summary="Relação de MedicosEspecialistas", description="Lista Todos Medicos Especialistas")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ATENDENTE') or hasRole('PACIENTE')")
    public ResponseEntity<List<MedicoEspecialidadeDTO>> listarTodosMedicoEspecialidades() {
        List<MedicoEspecialidadeDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{medicoId}/{especialidadeId}")
    @Operation(summary="Pesquisa MedicoEspecialista por Id", description="Localizar Médico Especialista por Id")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ATENDENTE') or hasRole('PACIENTE')")
    public ResponseEntity<MedicoEspecialidadeDTO> buscarMedicoEspecialidadePorId(
            @PathVariable Long medicoId,
            @PathVariable Long especialidadeId) {
        MedicoEspecialidadeId id = new MedicoEspecialidadeId(medicoId, especialidadeId);
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Cadastra MedicoEspecialista", description="Cadastrar Novo Médico Especialista")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoEspecialidadeDTO> salvarMedicoEspecialidade(@RequestBody @Valid MedicoEspecialidadeDTO dto) {
        MedicoEspecialidade salvo = service.salvarPorIds(dto.medicoIdResolvido(), dto.especialidadeIdResolvido());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }


    @DeleteMapping("/{medicoId}/{especialidadeId}")
    @Operation(summary="Deleta MedicoEspecialista", description="Deletar Médico Especialista Pesquisado por ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarMedicoEspecialidade(
            @PathVariable Long medicoId,
            @PathVariable Long especialidadeId) {
        MedicoEspecialidadeId id = new MedicoEspecialidadeId(medicoId, especialidadeId);
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
