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
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
public class RegistroDuplicadoException extends RuntimeException {

    private final String campo;

    public RegistroDuplicadoException(String message) {
        super(message);
        this.campo = null;
    }

    /**
     * @param campo nome do campo em conflito (ex.: {@code email}, {@code cpf}) para o cliente tratar por campo
     */
    public RegistroDuplicadoException(String campo, String message) {
        super(message);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
