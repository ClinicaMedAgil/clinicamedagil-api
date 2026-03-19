package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.NivelAcesso;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.repository.NivelAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * NivelAcessoService.java
 *
 * Classe resposavel pela camada de service do NivelAcesso
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
public class NivelAcessoService {

    private final NivelAcessoRepository repository;

    public List<NivelAcesso> listarTodos() {
        return repository.findAll();
    }

    public NivelAcesso buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Nível de acesso não encontrado."));
    }

    public NivelAcesso salvar(NivelAcesso nivelAcesso) {
        return repository.save(nivelAcesso);
    }

    public NivelAcesso atualizar(Long id, NivelAcesso nivelAcesso) {
        NivelAcesso existente = buscarPorId(id);
        existente.setNome(nivelAcesso.getNome());
        existente.setDescricao(nivelAcesso.getDescricao());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        NivelAcesso existente = buscarPorId(id);
        repository.delete(existente);
    }
}
