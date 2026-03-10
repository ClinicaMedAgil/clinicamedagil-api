package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * MedicoEspecialidadeId.java
 *
 * Entidade Java que representa o composto id
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - Christian Fonseca
 * </pre>
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoEspecialidadeId implements Serializable {

    private Long idMedico;

    private Long idEspecialidade;
}

