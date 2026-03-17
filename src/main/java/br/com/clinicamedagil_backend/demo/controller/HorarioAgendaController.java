package br.com.clinicamedagil_backend.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clinicamedagil_backend.demo.controller.dto.HorarioAgendaDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.HorarioAgendaMapper;
import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import br.com.clinicamedagil_backend.demo.service.HorarioAgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AgendaMedicoController.java
 *
 * Classe responsavel pela API de controller do HorariosAgendas
 *
 * @author Dailes Santos - back-end team
 *
 * <pre>
 *     History:
 *         14/03/2026 - feature/controller -  Dailes Santos
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedadil-service/horariosagendas")
@RequiredArgsConstructor
@Tag(name="HorariosAgendas", description="Gerenciamentos de Horários Agendados")
public class HorarioAgendaController {

    private final HorarioAgendaService service;
    private final HorarioAgendaMapper mapper;

    @GetMapping
    @Operation(summary="Relação de HorarioAgenda", description="Lista Todos Horários Agendados")
    public ResponseEntity<List<HorarioAgendaDTO>> listarTodosHorariosAgendas() {
        List<HorarioAgendaDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary="Pesquisa HorarioAgenda por Id", description="Localizar Horário Agendado por Id")
    public ResponseEntity<HorarioAgendaDTO> buscarHorarioAgendaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Cadastra HorarioAgenda", description="Cadastrar Novo Horário Agendado")
    public ResponseEntity<HorarioAgendaDTO> salvarHorarioAgenda(@RequestBody @Valid HorarioAgendaDTO dto) {
        HorarioAgenda salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary="Atualiza HorarioAgenda", description="Alterar Informações de Horário Agendado já cadastrado")
    public ResponseEntity<HorarioAgendaDTO> atualizarHorarioAgenda(@PathVariable Long id,
                                                @RequestBody @Valid HorarioAgendaDTO dto) {
        HorarioAgenda atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Deleta HorarioAgenda", description="Deletar Horário Agendado Pesquisado por ID")
    public ResponseEntity<Void> deletarHorarioAgenda(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
