package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
