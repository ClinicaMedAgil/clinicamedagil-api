package br.com.clinicamedagil_backend.demo.exceptions;

import lombok.Getter;

/**
 * CampoInvalidoExeception.exeception
 *
 * Classe resposavel por trata erros de campo invalido
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - Christian Fonseca
 * </pre>
 */
public class CampoInvalidoExeception extends RuntimeException {
    @Getter
    private String campo;

    public CampoInvalidoExeception(String campo, String mensagem){
        super(mensagem);
        this.campo = campo;
    }
}
