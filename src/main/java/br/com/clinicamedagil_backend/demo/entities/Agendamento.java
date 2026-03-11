package br.com.clinicamedagil_backend.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Agendamento.java
 *
 * Entidade Java que representa a tabela: tb_agendamento
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         09/03/2026 - feature/entidade - Christian Fonseca
 * </pre>
 */
@Entity
@Table(name = "tb_agendamento")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_horario")
    private HorarioAgenda horario;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Usuario paciente;

    private LocalDateTime dataMarcacao;

    private String statusAgendamento;
}