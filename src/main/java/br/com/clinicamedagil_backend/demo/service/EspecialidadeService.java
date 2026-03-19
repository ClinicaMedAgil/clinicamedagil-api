package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * EspecialidadeService.java
 *
 * Classe resposavel pela camada de service do Especialidade
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
public class EspecialidadeService {

    private final EspecialidadeRepository repository;

    public List<Especialidade> listarTodos() {
        return repository.findAll();
    }

    public Especialidade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Especialidade não encontrada."));
    }

    public Especialidade salvar(Especialidade especialidade) {
        return repository.save(especialidade);
    }

    public Especialidade atualizar(Long id, Especialidade especialidade) {
        Especialidade existente = buscarPorId(id);
        existente.setNomeEspecialidade(especialidade.getNomeEspecialidade());
        existente.setDescricao(especialidade.getDescricao());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        Especialidade existente = buscarPorId(id);
        repository.delete(existente);
    }
}
