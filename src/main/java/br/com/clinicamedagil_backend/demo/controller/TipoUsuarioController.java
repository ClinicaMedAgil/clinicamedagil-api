package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.TipoUsuarioDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.TipoUsuarioMapper;
import br.com.clinicamedagil_backend.demo.entities.TipoUsuario;
import br.com.clinicamedagil_backend.demo.service.TipoUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TipoUsuarioController.java
 *
 * Classe resposavel pela API de servicos do TipoUsuario
 *
 * @author João Paulo - back-end team
 *
 * <pre>
 *     History:
 *         15/03/2026 - feature/controller -  João Paulo
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedadil-service/tiposusuarios")
@RequiredArgsConstructor
public class TipoUsuarioController {

    private final TipoUsuarioService service;
    private final TipoUsuarioMapper mapper;

    @GetMapping
    public ResponseEntity<List<TipoUsuarioDTO>> listarTodosTiposUsuarios() {
        List<TipoUsuarioDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioDTO> buscarTipoUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }


    @PostMapping
    public ResponseEntity<TipoUsuarioDTO> salvarTipoUsuario(@RequestBody @Valid TipoUsuarioDTO dto) {
        TipoUsuario salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioDTO> atualizarTipoUsuario(@PathVariable Long id,
                                                       @RequestBody @Valid TipoUsuarioDTO dto) {
        TipoUsuario atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTipoUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
