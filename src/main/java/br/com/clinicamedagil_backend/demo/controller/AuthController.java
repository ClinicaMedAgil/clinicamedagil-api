package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginRequest;
import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginResponse;
import br.com.clinicamedagil_backend.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
