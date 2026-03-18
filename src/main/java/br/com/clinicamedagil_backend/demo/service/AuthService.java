package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginRequest;
import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginResponse;
import br.com.clinicamedagil_backend.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AuthService.java
 *
 * Classe resposavel pela camada de service e outenticação do usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         17/03/2026 - feature/spring_security_jwt - Christian Fonseca
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        var userDetails = User.builder()
                .username(request.email())
                .password("")
                .authorities(List.of())
                .build();

        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token, "Bearer");
    }
}
