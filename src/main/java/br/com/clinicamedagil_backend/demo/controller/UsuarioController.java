package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.UsuarioDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.UsuarioMapper;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UsuarioController.java
 *
 * Classe resposavel pela API de servicos do Usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         12/03/2026 - feature/controller -  Christian Fonseca
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedadil-service/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodosUsuarios() {
        List<UsuarioDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        Usuario salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id,
                                                @RequestBody @Valid UsuarioDTO dto) {
        Usuario atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
