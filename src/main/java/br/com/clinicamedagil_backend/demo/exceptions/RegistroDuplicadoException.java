package br.com.clinicamedagil_backend.demo.exceptions;

/**
 * RegistroDuplicadoException.exception
 *
 * Classe responsavel por tratar registros duplicados
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - Christian Fonseca
 * </pre>
 */
public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String message) {
        super(message);
    }
}
