package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MedicoEspecialidadeRepository.interface
 *
 * Repository resposavel pelo MedicoEspecialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface MedicoEspecialidadeRepository extends JpaRepository<MedicoEspecialidade, MedicoEspecialidadeId> {
}
