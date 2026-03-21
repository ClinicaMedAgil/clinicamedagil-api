package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * ConsultaRepository.interface
 *
 * Repository resposavel pelo Consulta
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPacienteId(long pacienteId);

    List<Consulta> findByMedicoId(long medicoId);

    @Query("""
            SELECT DISTINCT c FROM Consulta c
            JOIN FETCH c.agendamento a
            JOIN FETCH a.horario h
            JOIN FETCH h.agenda ag
            JOIN FETCH ag.medico
            JOIN FETCH ag.especialidade
            JOIN FETCH c.medico
            JOIN FETCH c.paciente
            WHERE c.paciente.id = :pacienteId
            ORDER BY ag.dataAgenda DESC, h.horaInicio ASC, c.id DESC
            """)
    List<Consulta> findByPacienteIdWithDetalhes(@Param("pacienteId") long pacienteId);

    @Query("""
            SELECT DISTINCT c FROM Consulta c
            JOIN FETCH c.agendamento a
            JOIN FETCH a.horario h
            JOIN FETCH h.agenda ag
            JOIN FETCH ag.medico
            JOIN FETCH ag.especialidade
            JOIN FETCH c.medico
            JOIN FETCH c.paciente
            WHERE c.medico.id = :medicoId
            ORDER BY ag.dataAgenda DESC, h.horaInicio ASC, c.id DESC
            """)
    List<Consulta> findByMedicoIdWithDetalhes(@Param("medicoId") long medicoId);

    @Query("""
            SELECT DISTINCT c FROM Consulta c
            JOIN FETCH c.agendamento a
            JOIN FETCH a.horario h
            JOIN FETCH h.agenda ag
            JOIN FETCH ag.medico
            JOIN FETCH ag.especialidade
            JOIN FETCH c.medico
            JOIN FETCH c.paciente
            WHERE c.id = :id
            """)
    Optional<Consulta> findByIdWithDetalhes(@Param("id") Long id);

    @Query("""
            SELECT COUNT(c) FROM Consulta c
            JOIN c.agendamento a
            JOIN a.horario h
            JOIN h.agenda ag
            WHERE c.paciente.id = :pacienteId
              AND (c.statusConsulta IS NULL OR UPPER(TRIM(c.statusConsulta)) <> 'FINALIZADA')
              AND ag.especialidade.id = :especialidadeId
              AND (:excludeId IS NULL OR c.id <> :excludeId)
            """)
    long countAgendadaNaoFinalizadaMesmaEspecialidade(
            @Param("pacienteId") Long pacienteId,
            @Param("especialidadeId") Long especialidadeId,
            @Param("excludeId") Long excludeConsultaId);

    @Query("""
            SELECT COUNT(c) FROM Consulta c
            JOIN c.agendamento a
            JOIN a.horario h
            JOIN h.agenda ag
            WHERE c.paciente.id = :pacienteId
              AND (c.statusConsulta IS NULL OR UPPER(TRIM(c.statusConsulta)) <> 'FINALIZADA')
              AND ag.dataAgenda = :dataAgenda
              AND h.horaInicio = :horaInicio
              AND h.horaFim = :horaFim
              AND (:excludeId IS NULL OR c.id <> :excludeId)
            """)
    long countAgendadaNaoFinalizadaMesmoDiaEHora(
            @Param("pacienteId") Long pacienteId,
            @Param("dataAgenda") LocalDate dataAgenda,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim,
            @Param("excludeId") Long excludeConsultaId);
}
