package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.PerfilDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.PerfilMapper;
import br.com.clinicamedagil_backend.demo.entities.Perfil;
import br.com.clinicamedagil_backend.demo.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PerfilController.java
 *
 * Classe resposavel pela API de servicos do Perfil
 *
 * @author João Paulo - back-end team
 *
 * <pre>
 *     History:
 *         15/03/2026 - feature/controller -  João Paulo
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedadil-service/perfis")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService service;
    private final PerfilMapper mapper;

    @GetMapping
    public ResponseEntity<List<PerfilDTO>> listarTodosPerfis() {
        List<PerfilDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilDTO> buscarPerfilPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<PerfilDTO> salvarPerfil(@RequestBody @Valid PerfilDTO dto) {
        Perfil salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilDTO> atualizarPerfil(@PathVariable Long id,
                                                       @RequestBody @Valid PerfilDTO dto) {
        Perfil atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPerfil(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
