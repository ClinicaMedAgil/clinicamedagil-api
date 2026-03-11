package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.TipoUsuario;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.repository.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TipoUsuarioService.java
 *
 * Classe resposavel pela camada de service do TipoUsuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/service -  Christian Fonseca
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class TipoUsuarioService {

    private final TipoUsuarioRepository repository;

    public List<TipoUsuario> listarTodos() {
        return repository.findAll();
    }

    public TipoUsuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Tipo de usuário não encontrado."));
    }

    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        return repository.save(tipoUsuario);
    }

    public TipoUsuario atualizar(Long id, TipoUsuario tipoUsuario) {
        TipoUsuario existente = buscarPorId(id);
        existente.setNome(tipoUsuario.getNome());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        TipoUsuario existente = buscarPorId(id);
        repository.delete(existente);
    }
}
