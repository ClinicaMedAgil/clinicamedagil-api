package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.RegistroDuplicadoException;
import br.com.clinicamedagil_backend.demo.repository.MedicoEspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MedicoEspecialidadeService.java
 *
 * Classe resposavel pela camada de service do MedicoEspecialidade
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
public class MedicoEspecialidadeService {

    private final MedicoEspecialidadeRepository repository;

    public List<MedicoEspecialidade> listarTodos() {
        return repository.findAll();
    }

    public MedicoEspecialidade buscarPorId(MedicoEspecialidadeId id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Relação médico/especialidade não encontrada."));
    }

    public MedicoEspecialidade salvar(MedicoEspecialidade medicoEspecialidade) {
        if (medicoEspecialidade.getId() != null && repository.existsById(medicoEspecialidade.getId())) {
            throw new RegistroDuplicadoException("Esta especialidade já está vinculada a este médico.");
        }

        return repository.save(medicoEspecialidade);
    }

    public void deletar(MedicoEspecialidadeId id) {
        MedicoEspecialidade existente = buscarPorId(id);
        repository.delete(existente);
    }
}
