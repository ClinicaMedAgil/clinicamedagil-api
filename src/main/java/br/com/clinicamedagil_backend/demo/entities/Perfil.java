package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;


/**
 * Perfil.java
 *
 * Entidade Java que representa a tabela: tb_perfil
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_perfil")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long id;

    private String nome;

    private String descricao;
}