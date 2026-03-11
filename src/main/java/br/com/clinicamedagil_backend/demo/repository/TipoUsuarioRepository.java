package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * TipoUsuarioRepository.interface
 *
 * Repository resposavel pelo TipoUsuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
}
