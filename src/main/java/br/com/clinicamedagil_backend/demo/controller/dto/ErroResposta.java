package br.com.clinicamedagil_backend.demo.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * ErroResposta.record
 *
 * Record criado para tratar erros nas respostas
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - Christian Fonseca
 * </pre>
 */
public record ErroResposta(int status , String mensagem, List<ErroCampo> erros) {

    public static ErroResposta respostaPadrao(String mensagem){
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }

    public static  ErroResposta conflito(String mensagem){
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }
}
