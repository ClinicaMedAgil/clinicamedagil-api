package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Especialidade.java
 *
 * Entidade Java que representa a tabela: tb_especialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_especialidade")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidade")
    private Long id;

    private String nomeEspecialidade;

    private String descricao;
}
