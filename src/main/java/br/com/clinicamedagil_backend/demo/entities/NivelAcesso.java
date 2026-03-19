package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * NivelAcesso.java
 *
 * Entidade Java que representa a tabela: tb_nivel_acesso
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_nivel_acesso")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel_acesso")
    private Long id;

    private String nome;

    private String descricao;
}