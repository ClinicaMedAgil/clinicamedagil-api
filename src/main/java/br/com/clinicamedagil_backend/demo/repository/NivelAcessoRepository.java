package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.NivelAcesso;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * NivelAcessoRepository.interface
 *
 * Repository resposavel pelo NivelAcesso
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface NivelAcessoRepository extends JpaRepository<NivelAcesso, Long> {
}
