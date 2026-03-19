package br.com.clinicamedagil_backend.demo.controller.dto;

/**
 * ErroCampo.record
 *
 * Record criado para retorna a resposta
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade -  Christian Fonseca
 * </pre>
 */
public record ErroCampo(String campo, String erro) {
}
