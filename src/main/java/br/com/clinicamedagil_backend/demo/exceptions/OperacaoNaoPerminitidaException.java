package br.com.clinicamedagil_backend.demo.exceptions;

/**
 * OperacaoNaoPerminitidaException.exception
 *
 * Classe responsavel por trata operacoes nao permitidas
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
public class OperacaoNaoPerminitidaException extends RuntimeException {
    public OperacaoNaoPerminitidaException(String message) {

      super(message);
    }
}
