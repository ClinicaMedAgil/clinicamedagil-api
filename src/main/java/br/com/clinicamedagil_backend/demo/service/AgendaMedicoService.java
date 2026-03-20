package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.repository.AgendaMedicoRepository;
import br.com.clinicamedagil_backend.demo.repository.EspecialidadeRepository;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AgendaMedicoService.java
 *
 * Classe resposavel pela camada de service do AgendaMedico
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
public class AgendaMedicoService {

    private final AgendaMedicoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public List<AgendaMedico> listarTodos() {
        return repository.findAll();
    }

    public AgendaMedico buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Agenda médica não encontrada."));
    }

    public AgendaMedico salvar(AgendaMedico agendaMedico) {
        if (agendaMedico.getMedico() == null) {
            throw new CampoInvalidoExeception("medico", "O médico é obrigatório.");
        }

        if (agendaMedico.getEspecialidade() == null) {
            throw new CampoInvalidoExeception("especialidade", "A especialidade é obrigatória.");
        }

        if (agendaMedico.getDataAgenda() == null) {
            throw new CampoInvalidoExeception("dataAgenda", "A data da agenda é obrigatória.");
        }

        agendaMedico.setMedico(resolveMedico(agendaMedico));
        agendaMedico.setEspecialidade(resolveEspecialidade(agendaMedico));

        return repository.save(agendaMedico);
    }

    public AgendaMedico atualizar(Long id, AgendaMedico agendaMedico) {
        AgendaMedico existente = buscarPorId(id);

        existente.setMedico(resolveMedico(agendaMedico));
        existente.setEspecialidade(resolveEspecialidade(agendaMedico));
        existente.setDataAgenda(agendaMedico.getDataAgenda());
        existente.setStatusAgenda(agendaMedico.getStatusAgenda());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        AgendaMedico existente = buscarPorId(id);
        repository.delete(existente);
    }

    private br.com.clinicamedagil_backend.demo.entities.Usuario resolveMedico(AgendaMedico agendaMedico) {
        Long medicoId = agendaMedico.getMedico() != null ? agendaMedico.getMedico().getId() : null;
        if (medicoId == null) {
            throw new CampoInvalidoExeception("medicoId", "O médico é obrigatório.");
        }
        return usuarioRepository.findById(medicoId)
                .orElseThrow(() -> new CampoInvalidoExeception("medicoId", "Médico não encontrado."));
    }

    private br.com.clinicamedagil_backend.demo.entities.Especialidade resolveEspecialidade(AgendaMedico agendaMedico) {
        Long especialidadeId = agendaMedico.getEspecialidade() != null ? agendaMedico.getEspecialidade().getId() : null;
        if (especialidadeId == null) {
            throw new CampoInvalidoExeception("especialidadeId", "A especialidade é obrigatória.");
        }
        return especialidadeRepository.findById(especialidadeId)
                .orElseThrow(() -> new CampoInvalidoExeception("especialidadeId", "Especialidade não encontrada."));
    }
}
