package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * HorarioAgendaRepository.interface
 *
 * Repository resposavel pelo HorarioAgenda
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface HorarioAgendaRepository extends JpaRepository<HorarioAgenda, Long> {

    List<HorarioAgenda> findByAgenda_IdInAndStatusHorario(Collection<Long> agendaIds, String statusHorario);

    long countByAgenda_Id(Long agendaId);

    long countByAgenda_IdAndStatusHorario(Long agendaId, String statusHorario);

    void deleteByAgenda_Id(Long agendaId);
}
