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

import br.com.clinicamedagil_backend.demo.controller.dto.AgendaMedicoDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.AgendaMedicoMapper;
import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import br.com.clinicamedagil_backend.demo.service.AgendaMedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AgendaMedicoController.java
 *
 * Classe responsavel pela API de Controller do AgendaMedicos
 *
 * @author Dailes Santos - back-end team
 *
 * <pre>
 *     History:
 *         14/03/2026 - feature/controller -  Dailes Santos
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedadil-service/agendamedicos")
@RequiredArgsConstructor
@Tag(name="AgendaMedicos", description="Gerenciamentos de Agenda Médicos")
public class AgendaMedicoController {

    private final AgendaMedicoService service;
    private final AgendaMedicoMapper mapper;

    @GetMapping
    @Operation(summary="Relação de AgendaMedico", description="Listar Todos Agendamentos Médicos")
    public ResponseEntity<List<AgendaMedicoDTO>> listarTodosAgendaMedicos() {
        List<AgendaMedicoDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary="Pesquisa AgendaMedico por Id", description="Localizar Agendamentos Médicos por Id")
    public ResponseEntity<AgendaMedicoDTO> buscarAgendaMedicoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Cadastra AgendaMedico", description="Cadastrar Novo Agendamento Médico")
    public ResponseEntity<AgendaMedicoDTO> salvarAgendaMedico(@RequestBody @Valid AgendaMedicoDTO dto) {
        AgendaMedico salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary="Atualiza AgendaMedico", description="Alterar Informações de Agendamento Médico já Cadastrado")
    public ResponseEntity<AgendaMedicoDTO> atualizarAgendaMedico(@PathVariable Long id,
                                                @RequestBody @Valid AgendaMedicoDTO dto) {
        AgendaMedico atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Deleta AgendaMedico", description="Deletar um Agendamento Médico Pesquisado por ID")
    public ResponseEntity<Void> deletarAgendaMedico(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
