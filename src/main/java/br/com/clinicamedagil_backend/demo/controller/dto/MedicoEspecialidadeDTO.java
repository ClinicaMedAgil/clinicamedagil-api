package br.com.clinicamedagil_backend.demo.controller.dto;

/**
 * MedicoEspecialidadeDTO.record
 *
 * DTO resposavel pelo MedicoEspecialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
public record MedicoEspecialidadeDTO(
        Long medicoId,
        Long especialidadeId,
        MedicoResumoDTO medico,
        EspecialidadeResumoDTO especialidade

) {
    public Long medicoIdResolvido() {
        if (medicoId != null) {
            return medicoId;
        }
        return medico != null ? medico.id() : null;
    }

    public Long especialidadeIdResolvido() {
        if (especialidadeId != null) {
            return especialidadeId;
        }
        return especialidade != null ? especialidade.id() : null;
    }

    /** id + nome para exibição no front (evita fallback só com id). */
    public record MedicoResumoDTO(Long id, String nome) {
    }

    public record EspecialidadeResumoDTO(Long id, String nome) {
    }
}
