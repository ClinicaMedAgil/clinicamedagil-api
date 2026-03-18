package br.com.clinicamedagil_backend.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clinicamedagil_backend.demo.controller.dto.AgendamentoDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.AgendamentoMapper;
import br.com.clinicamedagil_backend.demo.entities.Agendamento;
import br.com.clinicamedagil_backend.demo.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AgendaMedicoController.java
 *
 * Classe responsavel pela API de controller do Agendamentos
 *
 * @author Dailes Santos - back-end team
 *
 * <pre>
 *     History:
 *         14/03/2026 - feature/controller -  Dailes Santos
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedagil-service/agendamentos")
@RequiredArgsConstructor
@Tag(name="Agendamentos", description="Gerenciamentos de Agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;
    private final AgendamentoMapper mapper;

    @GetMapping
    @Operation(summary="Relação de Agendamentos", description="Lista Todos Agendamentos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO') or hasRole('PACIENTE')")
    public ResponseEntity<List<AgendamentoDTO>> listarTodosAgendamentos() {
        List<AgendamentoDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary="Pesquisa Agendamento por Id", description="Localizar Agendamento por Id")
    public ResponseEntity<AgendamentoDTO> buscarAgendamentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Cadastra Agendamento", description="Cadastrar Novo Agendamento")
    public ResponseEntity<AgendamentoDTO> salvarAgendamento(@RequestBody @Valid AgendamentoDTO dto) {
        Agendamento salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary="Atualiza Agendamento", description="Alterar informações de Agendamento já Cadastrado")
    public ResponseEntity<AgendamentoDTO> atualizarAgendamento(@PathVariable Long id,
                                                @RequestBody @Valid AgendamentoDTO dto) {
        Agendamento atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Deleta Agendamento", description="Deletar Agendamento Pesquisado por ID")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
