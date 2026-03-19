package br.com.clinicamedagil_backend.demo.security;

import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CustomUserDetailsService.java
 *
 * Classe resposavel por fazer uma busca no banco para validar o usuario e seu perfil
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
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        String role = usuario.getPerfil() != null && usuario.getPerfil().getNome() != null
                ? "ROLE_" + usuario.getPerfil().getNome().toUpperCase()
                : "ROLE_USUARIO";

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .build();
    }
}