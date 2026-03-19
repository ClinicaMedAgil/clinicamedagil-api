package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
