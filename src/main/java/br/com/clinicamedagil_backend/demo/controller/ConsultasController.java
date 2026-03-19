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

import br.com.clinicamedagil_backend.demo.controller.dto.ConsultaDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.ConsultaMapper;
import br.com.clinicamedagil_backend.demo.entities.Consulta;
import br.com.clinicamedagil_backend.demo.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AgendaMedicoController.java
 *
 * Classe responsavel pela API de controller do Consultas
 *
 * @author Dailes Santos - back-end team
 *
 * <pre>
 *     History:
 *         14/03/2026 - feature/controller -  Dailes Santos
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedagil-service/consultas")
@RequiredArgsConstructor
@Tag(name="Consultas", description="Gerenciamentos de Consultas")
public class ConsultasController {

    private final ConsultaService service;
    private final ConsultaMapper mapper;

    @GetMapping
    @Operation(summary="Relação de Consultas", description="Listar Todas Consultas")
    @PreAuthorize("hasRole('ATENDENTE') or hasRole('MEDICO')")
    public ResponseEntity<List<ConsultaDTO>> listarTodosConsultas() {
        List<ConsultaDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary="Pesquisa Consulta por Id", description="Localizar Consultar por Id")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ATENDENTE') or ('MEDICO')")
    public ResponseEntity<ConsultaDTO> buscarConsultaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Cadastra Consulta", description="Cadastrar Nova Consulta")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ATENDENTE')")
    public ResponseEntity<ConsultaDTO> salvarConsulta(@RequestBody @Valid ConsultaDTO dto) {
        Consulta salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary="Atualiza Consulta", description="Alterar Informações de Consulta já Cadastrada")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<ConsultaDTO> atualizarConsulta(@PathVariable Long id,
                                                @RequestBody @Valid ConsultaDTO dto) {
        Consulta atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Deleta Consulta", description="Deletar Consulta Pesquisada por ID")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ATENDENTE')")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
