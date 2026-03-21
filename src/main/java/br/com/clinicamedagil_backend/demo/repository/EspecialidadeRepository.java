package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

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

    Optional<Especialidade> findByNomeEspecialidadeIgnoreCase(String nomeEspecialidade);

    List<Especialidade> findByNomeEspecialidadeContainingIgnoreCase(String nomeEspecialidade);
}
