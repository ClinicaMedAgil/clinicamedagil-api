package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.Agendamento;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.OperacaoNaoPerminitidaException;
import br.com.clinicamedagil_backend.demo.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AgendamentoService.java
 *
 * Classe resposavel pela camada de service do Agendamento
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
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Agendamento não encontrado."));
    }

    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento.getHorario() == null) {
            throw new CampoInvalidoExeception("horario", "O horário é obrigatório.");
        }

        if (agendamento.getPaciente() == null) {
            throw new CampoInvalidoExeception("paciente", "O paciente é obrigatório.");
        }

        if (agendamento.getDataMarcacao() == null) {
            agendamento.setDataMarcacao(LocalDateTime.now());
        }

        return repository.save(agendamento);
    }

    public Agendamento atualizar(Long id, Agendamento agendamento) {
        Agendamento existente = buscarPorId(id);

        if ("CANCELADO".equalsIgnoreCase(existente.getStatusAgendamento())) {
            throw new OperacaoNaoPerminitidaException("Não é permitido alterar um agendamento cancelado.");
        }

        existente.setHorario(agendamento.getHorario());
        existente.setPaciente(agendamento.getPaciente());
        existente.setDataMarcacao(
                agendamento.getDataMarcacao() != null ? agendamento.getDataMarcacao() : existente.getDataMarcacao()
        );
        existente.setStatusAgendamento(agendamento.getStatusAgendamento());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        Agendamento existente = buscarPorId(id);

        if ("CONCLUIDO".equalsIgnoreCase(existente.getStatusAgendamento())) {
            throw new OperacaoNaoPerminitidaException("Não é permitido excluir um agendamento concluído.");
        }

        repository.delete(existente);
    }
}
