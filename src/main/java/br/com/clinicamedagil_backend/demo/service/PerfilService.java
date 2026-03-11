package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.Perfil;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PerfilService.java
 *
 * Classe resposavel pela camada de service do Perfil
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
public class PerfilService {

    private final PerfilRepository repository;

    public List<Perfil> listarTodos() {
        return repository.findAll();
    }

    public Perfil buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Perfil não encontrado."));
    }

    public Perfil salvar(Perfil perfil) {
        return repository.save(perfil);
    }

    public Perfil atualizar(Long id, Perfil perfil) {
        Perfil existente = buscarPorId(id);
        existente.setNome(perfil.getNome());
        existente.setDescricao(perfil.getDescricao());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        Perfil existente = buscarPorId(id);
        repository.delete(existente);
    }
}
