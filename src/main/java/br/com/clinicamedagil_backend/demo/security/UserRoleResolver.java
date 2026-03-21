package br.com.clinicamedagil_backend.demo.security;

import br.com.clinicamedagil_backend.demo.entities.Usuario;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Define a role usada no JWT e no Spring Security a partir do usuário.
 * Prioriza {@link Usuario#getTipoUsuario()} sobre {@link Usuario#getPerfil()}, pois no cadastro
 * os dois IDs são independentes; se o perfil não for atualizado junto com o tipo, o médico
 * continuaria autenticando como PACIENTE.
 * <p>
 * Nomes são normalizados para maiúsculas <strong>sem acento</strong> (ex.: {@code MEDICO}), alinhado a
 * {@code hasRole('MEDICO')} e aos valores gravados no seed ({@code tb_perfil}, {@code tb_tipo_usuario}).
 */
public final class UserRoleResolver {

    private UserRoleResolver() {
    }

    public static String resolveNormalizedRole(Usuario usuario) {
        String tipoNome = usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getNome() : null;
        String perfilNome = usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null;
        String raw = firstNonBlank(tipoNome, perfilNome);
        return normalizeRole(raw);
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a;
        }
        if (b != null && !b.isBlank()) {
            return b;
        }
        return null;
    }

    private static String normalizeRole(String nome) {
        if (nome == null || nome.isBlank()) {
            return "USUARIO";
        }
        String withoutAccents = stripAccents(nome.toUpperCase(Locale.ROOT).trim());
        if ("ADMINISTRADOR".equals(withoutAccents)) {
            return "ADMIN";
        }
        return withoutAccents;
    }

    private static String stripAccents(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
    }
}
