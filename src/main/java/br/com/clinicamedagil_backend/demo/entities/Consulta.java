package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Consulta.java
 *
 * Entidade Java que representa a tabela: tb_consulta
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_consulta")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_agendamento")
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Usuario medico;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Usuario paciente;

    private LocalDateTime dataConsulta;

    private String queixaPrincipal;

    private String historiaDoencaAtual;

    private String diagnostico;

    private String prescricao;

    private String observacoes;
}
