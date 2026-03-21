package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginRequest;
import br.com.clinicamedagil_backend.demo.controller.dto.auth.LoginResponse;
import br.com.clinicamedagil_backend.demo.controller.dto.auth.AuthUserContextDTO;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import br.com.clinicamedagil_backend.demo.security.JwtService;
import br.com.clinicamedagil_backend.demo.security.UserRoleResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private static final String CAMPO_EMAIL = "email";
    private static final String MENSAGEM_USUARIO_NAO_ENCONTRADO = "Usuário não encontrado.";
    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_CPF = "cpf";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new CampoInvalidoExeception(CAMPO_EMAIL, MENSAGEM_USUARIO_NAO_ENCONTRADO));

        if (!isUsuarioAtivo(usuario.getStatus())) {
            throw new DisabledException("Usuário inativo. Login não permitido.");
        }

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

        String role = UserRoleResolver.resolveNormalizedRole(usuario);
        AuthUserContextDTO usuarioContexto = new AuthUserContextDTO(
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                role
        );

        var claims = new HashMap<String, Object>();
        if (usuarioContexto.nome() != null) claims.put(CLAIM_NAME, usuarioContexto.nome());
        if (usuarioContexto.cpf() != null) claims.put(CLAIM_CPF, usuarioContexto.cpf());
        if (usuarioContexto.email() != null) claims.put(CLAIM_EMAIL, usuarioContexto.email());
        claims.put(CLAIM_ROLE, usuarioContexto.role());

        String token = jwtService.generateToken(claims, userDetails.getUsername());

        return new LoginResponse(token, "Bearer", usuarioContexto);
    }

    public AuthUserContextDTO getAuthenticatedUserContext(String email) {
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new CampoInvalidoExeception(CAMPO_EMAIL, MENSAGEM_USUARIO_NAO_ENCONTRADO));

        String role = UserRoleResolver.resolveNormalizedRole(usuario);
        return new AuthUserContextDTO(usuario.getNome(), usuario.getCpf(), usuario.getEmail(), role);
    }

    public void changePassword(String email, String senhaAtual, String novaSenha) {
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new CampoInvalidoExeception(CAMPO_EMAIL, MENSAGEM_USUARIO_NAO_ENCONTRADO));

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new CampoInvalidoExeception("senhaAtual", "A senha atual está incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    private boolean isUsuarioAtivo(String status) {
        return status != null && "ATIVO".equals(status.trim().toUpperCase(Locale.ROOT));
    }

}
