package br.com.clinicamedagil_backend.demo.controller.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest.record
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
public record LoginRequest(

        @Email(message = "Email inválido")
        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha

) {}
