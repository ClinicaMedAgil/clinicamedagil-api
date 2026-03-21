package br.com.clinicamedagil_backend.demo.controller.dto;

/**
 * Dados clínicos opcionais enviados ao encerrar a consulta pelo médico.
 * Campos omitidos ou nulos não alteram o valor já salvo na entidade.
 */
public record FinalizarConsultaMedicoDTO(
        String queixaPrincipal,
        String historiaDoencaAtual,
        String diagnostico,
        String prescricao,
        String observacoes
) {}
