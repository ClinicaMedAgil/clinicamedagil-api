package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.MedicoEspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.MedicoEspecialidadeMapper;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import br.com.clinicamedagil_backend.demo.service.MedicoEspecialidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/clinicamedadil-service/medicoespecialidade")
@RequiredArgsConstructor
public class MedicoEspecialidadeController {

    private final MedicoEspecialidadeService service;
    private final MedicoEspecialidadeMapper mapper;

    @GetMapping
    public ResponseEntity<List<MedicoEspecialidadeDTO>> listarTodosMedicoEspecialidades() {
        List<MedicoEspecialidadeDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping
    public ResponseEntity<MedicoEspecialidadeDTO> buscarMedicoEspecialidadePorId(@RequestBody @Valid MedicoEspecialidadeId id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<MedicoEspecialidadeDTO> salvarMedicoEspecialidade(@RequestBody @Valid MedicoEspecialidadeDTO dto) {
        MedicoEspecialidade salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }


    @DeleteMapping
    public ResponseEntity<Void> deletarMedicoEspecialidade(@RequestBody @Valid MedicoEspecialidadeId id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
