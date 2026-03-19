package br.com.clinicamedagil_backend.demo.controller.dto.auth;

/**
 * LoginResponse.record
 *
 * Responsel pela autenticação do usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         17/03/2026 - feature/spring_security_jwt - Christian Fonseca
 * </pre>
 */
public record LoginResponse(
        String token,
        String tipo
) {}
