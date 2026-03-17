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

import br.com.clinicamedagil_backend.demo.controller.dto.EspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.EspecialidadeMapper;
import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import br.com.clinicamedagil_backend.demo.service.EspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * EspecialidadeController.java
 *
 * Classe resposavel pela API de servicos da Especialidade
 *
 * @author João Paulo - back-end team
 *
 * <pre>
 *     History:
 *         15/03/2026 - feature/controller -  João Paulo
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedadil-service/especialidades")
@RequiredArgsConstructor
@Tag(name="Especialistas", description="Gerenciamentos de Especialistas")
public class EspecialidadeController {

    private final EspecialidadeService service;
    private final EspecialidadeMapper mapper;


    @GetMapping
    @Operation(summary="Relação de Especialistas", description="Listat Todos os Especialistas")
    public ResponseEntity<List<EspecialidadeDTO>> listarTodasEspecialidades() {
        List<EspecialidadeDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary="Pesquisa Especialista por Id", description="Buscar Especialista por Id")
    public ResponseEntity<EspecialidadeDTO> buscarEspecialidadePorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }


    @PostMapping
    @Operation(summary="Cadastra Especialista", description="Cadastrar Novo Especialista")
    public ResponseEntity<EspecialidadeDTO> salvarEspecialidade(@RequestBody @Valid EspecialidadeDTO dto) {
        Especialidade salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary="Atualiza Especialista", description="Alterar informações de Especialista já Cadastrado")
    public ResponseEntity<EspecialidadeDTO> atualizarEspecialidade(@PathVariable Long id,
                                                       @RequestBody @Valid EspecialidadeDTO dto) {
        Especialidade atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary="Deleta Especialista", description="Deletar Especialista Pesquisado por ID")
    public ResponseEntity<Void> deletarEspecialidade(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
