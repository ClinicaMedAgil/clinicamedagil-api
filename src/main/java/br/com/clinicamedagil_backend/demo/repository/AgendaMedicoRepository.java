package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * AgendaMedicoRepository.interface
 *
 * Repository resposavel pelo AgendaMedico
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface AgendaMedicoRepository extends JpaRepository<AgendaMedico, Long> {

    @Query("""
            SELECT DISTINCT a FROM AgendaMedico a
            JOIN FETCH a.medico
            JOIN FETCH a.especialidade
            WHERE a.medico.id = :medicoId
              AND a.especialidade.id = :especialidadeId
              AND a.statusAgenda = :statusAgenda
              AND a.dataAgenda >= :desde
            ORDER BY a.dataAgenda ASC, a.id ASC
            """)
    List<AgendaMedico> findDisponiveisDesde(
            @Param("medicoId") Long medicoId,
            @Param("especialidadeId") Long especialidadeId,
            @Param("statusAgenda") String statusAgenda,
            @Param("desde") LocalDate desde);

    @Query("""
            SELECT DISTINCT a FROM AgendaMedico a
            JOIN FETCH a.medico
            JOIN FETCH a.especialidade
            WHERE a.medico.id = :medicoId
              AND a.statusAgenda = :statusAgenda
              AND a.dataAgenda >= :desde
            ORDER BY a.dataAgenda ASC, a.id ASC
            """)
    List<AgendaMedico> findDisponiveisPorMedicoDesde(
            @Param("medicoId") Long medicoId,
            @Param("statusAgenda") String statusAgenda,
            @Param("desde") LocalDate desde);

    /** Agendas ativas: publicação liberada ao paciente (ATIVA; DISPONIVEL mantido por compatibilidade). */
    @Query("""
            SELECT DISTINCT a FROM AgendaMedico a
            JOIN FETCH a.medico
            JOIN FETCH a.especialidade
            WHERE a.medico.id = :medicoId
              AND a.especialidade.id = :especialidadeId
              AND upper(trim(a.statusAgenda)) in :statusesAgendaAtiva
              AND a.dataAgenda >= :desde
            ORDER BY a.dataAgenda ASC, a.id ASC
            """)
    List<AgendaMedico> findAtivasDesde(
            @Param("medicoId") Long medicoId,
            @Param("especialidadeId") Long especialidadeId,
            @Param("statusesAgendaAtiva") List<String> statusesAgendaAtiva,
            @Param("desde") LocalDate desde);

    @Query("""
            SELECT DISTINCT a FROM AgendaMedico a
            JOIN FETCH a.medico
            JOIN FETCH a.especialidade
            WHERE a.medico.id = :medicoId
              AND upper(trim(a.statusAgenda)) in :statusesAgendaAtiva
              AND a.dataAgenda >= :desde
            ORDER BY a.dataAgenda ASC, a.id ASC
            """)
    List<AgendaMedico> findAtivasPorMedicoDesde(
            @Param("medicoId") Long medicoId,
            @Param("statusesAgendaAtiva") List<String> statusesAgendaAtiva,
            @Param("desde") LocalDate desde);
}
