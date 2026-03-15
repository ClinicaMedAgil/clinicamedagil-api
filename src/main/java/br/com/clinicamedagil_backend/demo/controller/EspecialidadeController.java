package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.EspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.EspecialidadeMapper;
import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import br.com.clinicamedagil_backend.demo.service.EspecialidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class EspecialidadeController {

    private final EspecialidadeService service;
    private final EspecialidadeMapper mapper;


    @GetMapping
    public ResponseEntity<List<EspecialidadeDTO>> listarTodasEspecialidades() {
        List<EspecialidadeDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeDTO> buscarEspecialidadePorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }


    @PostMapping
    public ResponseEntity<EspecialidadeDTO> salvarEspecialidade(@RequestBody @Valid EspecialidadeDTO dto) {
        Especialidade salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeDTO> atualizarEspecialidade(@PathVariable Long id,
                                                       @RequestBody @Valid EspecialidadeDTO dto) {
        Especialidade atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEspecialidade(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
