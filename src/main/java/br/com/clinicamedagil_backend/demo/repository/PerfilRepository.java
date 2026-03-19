package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * PerfilRepository.interface
 *
 * Repository resposavel pelo Perfil
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}
