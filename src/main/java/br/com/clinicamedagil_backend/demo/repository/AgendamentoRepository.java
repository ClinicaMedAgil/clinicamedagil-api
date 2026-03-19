package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AgendamentoRepository.interface
 *
 * Repository resposavel pelo Agendamento
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}