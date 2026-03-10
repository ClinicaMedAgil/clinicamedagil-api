package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Usuario.java
 *
 * Entidade Java que representa a tabela: tb_usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_usuario")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    private String nome;

    private String cpf;

    private String email;

    private String telefone;

    private String senha;

    private String status;

    private LocalDateTime dataCadastro;

    @ManyToOne
    @JoinColumn(name = "id_tipo_usuario")
    private TipoUsuario tipoUsuario;

    @ManyToOne
    @JoinColumn(name = "id_perfil")
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "id_nivel_acesso")
    private NivelAcesso nivelAcesso;
}
