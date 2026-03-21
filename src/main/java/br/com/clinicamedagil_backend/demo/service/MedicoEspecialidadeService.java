package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.RegistroDuplicadoException;
import br.com.clinicamedagil_backend.demo.repository.MedicoEspecialidadeRepository;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import br.com.clinicamedagil_backend.demo.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public List<MedicoEspecialidade> listarTodos() {
        return repository.findAllWithMedicoEspecialidade();
    }

    public MedicoEspecialidade buscarPorId(MedicoEspecialidadeId id) {
        return repository.findById(Objects.requireNonNull(id, "Id da relação é obrigatório."))
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Relação médico/especialidade não encontrada."));
    }

    public MedicoEspecialidade salvar(MedicoEspecialidade medicoEspecialidade) {
        if (medicoEspecialidade.getId() != null && repository.existsById(medicoEspecialidade.getId())) {
            throw new RegistroDuplicadoException("Esta especialidade já está vinculada a este médico.");
        }

        return repository.save(medicoEspecialidade);
    }

    public MedicoEspecialidade salvarPorIds(Long medicoId, Long especialidadeId) {
        if (medicoId == null) {
            throw new CampoInvalidoExeception("medicoId", "O médico é obrigatório.");
        }
        if (especialidadeId == null) {
            throw new CampoInvalidoExeception("especialidadeId", "A especialidade é obrigatória.");
        }

        MedicoEspecialidadeId id = new MedicoEspecialidadeId(medicoId, especialidadeId);
        if (repository.existsById(Objects.requireNonNull(id, "Id da relação é obrigatório."))) {
            throw new RegistroDuplicadoException("Esta especialidade já está vinculada a este médico.");
        }

        Usuario medico = usuarioRepository.findById(medicoId)
                .orElseThrow(() -> new CampoInvalidoExeception("medicoId", "Médico não encontrado."));
        Especialidade especialidade = especialidadeRepository.findById(especialidadeId)
                .orElseThrow(() -> new CampoInvalidoExeception("especialidadeId", "Especialidade não encontrada."));

        MedicoEspecialidade relacao = MedicoEspecialidade.builder()
                .id(id)
                .medico(medico)
                .especialidade(especialidade)
                .build();

        return repository.save(Objects.requireNonNull(relacao, "Relação médico/especialidade é obrigatória."));
    }

    public void deletar(MedicoEspecialidadeId id) {
        MedicoEspecialidade existente = buscarPorId(id);
        repository.delete(Objects.requireNonNull(existente, "Relação médico/especialidade é obrigatória."));
    }
}
