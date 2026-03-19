package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * MedicoEspecialidade.java
 *
 * Entidade Java que representa a tabela: tb_medico_especialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_medico_especialidade")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoEspecialidade {

    @EmbeddedId
    private MedicoEspecialidadeId id;

    @ManyToOne
    @MapsId("idMedico")
    @JoinColumn(name = "id_medico")
    private Usuario medico;

    @ManyToOne
    @MapsId("idEspecialidade")
    @JoinColumn(name = "id_especialidade")
    private Especialidade especialidade;
}