package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.auth.ChangePasswordRequest;
import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginRequest;
import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginResponse;
import br.com.clinicamedagil_backend.demo.controller.dto.auth.AuthUserContextDTO;
import br.com.clinicamedagil_backend.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * AuthController.java
 *
 * Classe resposavel pela API de autenticação do usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         17/03/2026 - feature/spring_security_jwt - Christian Fonseca
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedagil-service/auth")
@RequiredArgsConstructor
@Tag(name="LoginSistema", description="Login do Sistema")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary="Login", description="Login de Acesso ao Sistema")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary="Dados de contexto do usuário autenticado", description="Retorna nome, CPF, email e role do usuário logado")
    public ResponseEntity<AuthUserContextDTO> me(Principal principal) {
        return ResponseEntity.ok(authService.getAuthenticatedUserContext(principal.getName()));
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary="Alterar senha", description="Altera a senha do usuário autenticado")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal principal) {
        authService.changePassword(principal.getName(), request.senhaAtual(), request.novaSenha());
        return ResponseEntity.noContent().build();
    }
}
