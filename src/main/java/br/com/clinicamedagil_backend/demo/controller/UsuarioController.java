package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.auth.ChangePasswordRequest;
import br.com.clinicamedagil_backend.demo.controller.dto.AtualizarMeuPerfilDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.UsuarioComumDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.UsuarioDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.UsuarioPerfilDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.UsuarioMapper;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.service.AuthService;
import br.com.clinicamedagil_backend.demo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.security.Principal;
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
 * http://localhost:8081/swagger-ui/index.html
 */
@RestController
@RequestMapping("/clinicamedagil-service/usuarios")
@RequiredArgsConstructor
@Tag(name="Usuarios", description="Gerenciamentos de Usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;
    private final AuthService authService;

    @GetMapping
    @Operation(summary="Relação de Usuarios", description="Listar Todos Usuarios")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ATENDENTE')")
    public ResponseEntity<List<UsuarioDTO>> listarTodosUsuarios() {
        List<UsuarioDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary="Pesquisa Usuário pelo Id", description="Localizar Usuário por Id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ATENDENTE')")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @GetMapping("/me")
    @Operation(summary="Perfil do usuário autenticado", description="Retorna os dados completos do usuário logado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioPerfilDTO> buscarMeuPerfil(Principal principal) {
        Usuario usuario = service.buscarPorEmail(principal.getName());
        UsuarioPerfilDTO perfil = new UsuarioPerfilDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getStatus(),
                usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getId() : null,
                usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getNome() : null,
                usuario.getPerfil() != null ? usuario.getPerfil().getId() : null,
                usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null,
                usuario.getNivelAcesso() != null ? usuario.getNivelAcesso().getId() : null,
                usuario.getNivelAcesso() != null ? usuario.getNivelAcesso().getNome() : null
        );
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/me")
    @Operation(summary="Atualizar perfil do usuário autenticado", description="Permite ao usuário atualizar apenas os próprios dados de perfil")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioPerfilDTO> atualizarMeuPerfil(@RequestBody @Valid AtualizarMeuPerfilDTO dto, Principal principal) {
        Usuario usuario = service.atualizarMeuPerfil(principal.getName(), dto);
        UsuarioPerfilDTO perfil = new UsuarioPerfilDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getStatus(),
                usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getId() : null,
                usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getNome() : null,
                usuario.getPerfil() != null ? usuario.getPerfil().getId() : null,
                usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null,
                usuario.getNivelAcesso() != null ? usuario.getNivelAcesso().getId() : null,
                usuario.getNivelAcesso() != null ? usuario.getNivelAcesso().getNome() : null
        );
        return ResponseEntity.ok(perfil);
    }

    @PostMapping
    @Operation(summary="Cadastra Usuario", description="Cadastrar Novo Usuário")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ATENDENTE')")
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        Usuario salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PostMapping("/criarUsuario")
    @Operation(summary="Cadastra Usuario", description="Cadastrar Novo Usuário (ADMIN)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ATENDENTE')")
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        return salvarUsuario(dto);
    }

    @PostMapping("/criarUsuarioComum")
    @Operation(summary="Cadastra Usuario Comum", description="Cadastro público de paciente com senha padrão")
    public ResponseEntity<UsuarioDTO> criarUsuarioComum(@RequestBody @Valid UsuarioComumDTO dto) {
        Usuario salvo = service.criarUsuarioComum(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary="Atualiza Usuário", description="Alterar Informações de Usuário já Cadastrado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ATENDENTE')")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id,
                                                @RequestBody @Valid UsuarioDTO dto) {
        Usuario atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Deleta Usuário", description="Deletar Usuário Pesquisado por ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ATENDENTE')")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterar-senha")
    @Operation(summary="Alterar senha do usuário autenticado", description="Altera a senha do usuário logado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid ChangePasswordRequest request, Principal principal) {
        authService.changePassword(principal.getName(), request.senhaAtual(), request.novaSenha());
        return ResponseEntity.noContent().build();
    }
}
