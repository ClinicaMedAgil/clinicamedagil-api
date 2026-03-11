package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * EspecialidadeRepository.interface
 *
 * Repository resposavel pelo Especialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
}
