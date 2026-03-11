package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.Consulta;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.OperacaoNaoPerminitidaException;
import br.com.clinicamedagil_backend.demo.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ConsultaService.java
 *
 * Classe resposavel pela camada de service do Consulta
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
public class ConsultaService {

    private final ConsultaRepository repository;

    public List<Consulta> listarTodos() {
        return repository.findAll();
    }

    public Consulta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Consulta não encontrada."));
    }

    public Consulta salvar(Consulta consulta) {
        if (consulta.getAgendamento() == null) {
            throw new CampoInvalidoExeception("agendamento", "O agendamento é obrigatório.");
        }

        if (consulta.getMedico() == null) {
            throw new CampoInvalidoExeception("medico", "O médico é obrigatório.");
        }

        if (consulta.getPaciente() == null) {
            throw new CampoInvalidoExeception("paciente", "O paciente é obrigatório.");
        }

        if (consulta.getDataConsulta() == null) {
            consulta.setDataConsulta(LocalDateTime.now());
        }

        return repository.save(consulta);
    }

    public Consulta atualizar(Long id, Consulta consulta) {
        Consulta existente = buscarPorId(id);

        if (existente.getDataConsulta() != null && existente.getDataConsulta().isBefore(LocalDateTime.now())) {
            throw new OperacaoNaoPerminitidaException("Não é permitido alterar uma consulta já realizada.");
        }

        existente.setAgendamento(consulta.getAgendamento());
        existente.setMedico(consulta.getMedico());
        existente.setPaciente(consulta.getPaciente());
        existente.setDataConsulta(
                consulta.getDataConsulta() != null ? consulta.getDataConsulta() : existente.getDataConsulta()
        );
        existente.setQueixaPrincipal(consulta.getQueixaPrincipal());
        existente.setHistoriaDoencaAtual(consulta.getHistoriaDoencaAtual());
        existente.setDiagnostico(consulta.getDiagnostico());
        existente.setPrescricao(consulta.getPrescricao());
        existente.setObservacoes(consulta.getObservacoes());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        Consulta existente = buscarPorId(id);

        if (existente.getDataConsulta() != null && existente.getDataConsulta().isBefore(LocalDateTime.now())) {
            throw new OperacaoNaoPerminitidaException("Não é permitido excluir uma consulta já realizada.");
        }

        repository.delete(existente);
    }
}
