package br.com.clinicamedagil_backend.demo.repository;

import br.com.clinicamedagil_backend.demo.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UsuarioRepository.interface
 *
 * Repository resposavel pelo Usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/repository -  Christian Fonseca
 * </pre>
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

}
