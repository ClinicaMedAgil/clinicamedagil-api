package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    @Query("""
            SELECT DISTINCT m FROM MedicoEspecialidade m
            JOIN FETCH m.medico
            JOIN FETCH m.especialidade
            WHERE m.id.idEspecialidade = :especialidadeId
            """)
    List<MedicoEspecialidade> findByEspecialidadeId(@Param("especialidadeId") Long especialidadeId);

    @Query("""
            SELECT DISTINCT m FROM MedicoEspecialidade m
            JOIN FETCH m.medico
            JOIN FETCH m.especialidade
            """)
    List<MedicoEspecialidade> findAllWithMedicoEspecialidade();
}
