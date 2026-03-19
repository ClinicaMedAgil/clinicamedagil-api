package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * TipoUsuario.java
 *
 * Entidade Java que representa a tabela: tb_tipo_usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_tipo_usuario")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_usuario")
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;
}