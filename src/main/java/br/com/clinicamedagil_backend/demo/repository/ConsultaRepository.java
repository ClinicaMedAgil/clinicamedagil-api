package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ConsultaRepository.interface
 *
 * Repository resposavel pelo Consulta
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    java.util.List<Consulta> findByPacienteId(long pacienteId);

    java.util.List<Consulta> findByMedicoId(long medicoId);
}
