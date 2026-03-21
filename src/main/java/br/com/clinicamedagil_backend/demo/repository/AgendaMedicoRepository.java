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
            SELECT a FROM AgendaMedico a
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
            SELECT a FROM AgendaMedico a
            WHERE a.medico.id = :medicoId
              AND a.statusAgenda = :statusAgenda
              AND a.dataAgenda >= :desde
            ORDER BY a.dataAgenda ASC, a.id ASC
            """)
    List<AgendaMedico> findDisponiveisPorMedicoDesde(
            @Param("medicoId") Long medicoId,
            @Param("statusAgenda") String statusAgenda,
            @Param("desde") LocalDate desde);
}
