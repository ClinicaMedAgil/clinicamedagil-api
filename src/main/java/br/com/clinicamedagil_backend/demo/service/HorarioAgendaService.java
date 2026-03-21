package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.OperacaoNaoPerminitidaException;
import br.com.clinicamedagil_backend.demo.repository.AgendaMedicoRepository;
import br.com.clinicamedagil_backend.demo.repository.HorarioAgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * HorarioAgendaService.java
 *
 * Classe resposavel pela camada de service do HorarioAgenda
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
public class HorarioAgendaService {

    private final HorarioAgendaRepository repository;
    private final AgendaMedicoRepository agendaMedicoRepository;
    private final AgendaMedicoService agendaMedicoService;

    public List<HorarioAgenda> listarTodos() {
        return repository.findAll();
    }

    public HorarioAgenda buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Horário da agenda não encontrado."));
    }

    public HorarioAgenda salvar(HorarioAgenda horarioAgenda) {
        if (horarioAgenda.getAgenda() == null) {
            throw new CampoInvalidoExeception("agenda", "A agenda é obrigatória.");
        }

        if (horarioAgenda.getHoraInicio() == null) {
            throw new CampoInvalidoExeception("horaInicio", "A hora inicial é obrigatória.");
        }

        if (horarioAgenda.getHoraFim() == null) {
            throw new CampoInvalidoExeception("horaFim", "A hora final é obrigatória.");
        }

        if (!horarioAgenda.getHoraFim().isAfter(horarioAgenda.getHoraInicio())) {
            throw new OperacaoNaoPerminitidaException("A hora final deve ser maior que a hora inicial.");
        }

        horarioAgenda.setAgenda(resolveAgenda(horarioAgenda));

        HorarioAgenda salvo = repository.save(horarioAgenda);
        if (salvo.getAgenda() != null && salvo.getAgenda().getId() != null) {
            agendaMedicoService.sincronizarStatusAgendaComHorariosLivres(salvo.getAgenda().getId());
        }
        return salvo;
    }

    public HorarioAgenda atualizar(Long id, HorarioAgenda horarioAgenda) {
        HorarioAgenda existente = buscarPorId(id);

        if (horarioAgenda.getHoraInicio() != null && horarioAgenda.getHoraFim() != null
                && !horarioAgenda.getHoraFim().isAfter(horarioAgenda.getHoraInicio())) {
            throw new OperacaoNaoPerminitidaException("A hora final deve ser maior que a hora inicial.");
        }

        existente.setAgenda(resolveAgenda(horarioAgenda));
        existente.setHoraInicio(horarioAgenda.getHoraInicio());
        existente.setHoraFim(horarioAgenda.getHoraFim());
        existente.setStatusHorario(horarioAgenda.getStatusHorario());

        HorarioAgenda salvo = repository.save(existente);
        if (salvo.getAgenda() != null && salvo.getAgenda().getId() != null) {
            agendaMedicoService.sincronizarStatusAgendaComHorariosLivres(salvo.getAgenda().getId());
        }
        return salvo;
    }

    public void deletar(Long id) {
        HorarioAgenda existente = buscarPorId(id);
        Long agendaId = existente.getAgenda() != null ? existente.getAgenda().getId() : null;
        repository.delete(existente);
        if (agendaId != null) {
            agendaMedicoService.sincronizarStatusAgendaComHorariosLivres(agendaId);
        }
    }

    private br.com.clinicamedagil_backend.demo.entities.AgendaMedico resolveAgenda(HorarioAgenda horarioAgenda) {
        Long agendaId = horarioAgenda.getAgenda() != null ? horarioAgenda.getAgenda().getId() : null;
        if (agendaId == null) {
            throw new CampoInvalidoExeception("agendaId", "A agenda é obrigatória.");
        }
        return agendaMedicoRepository.findById(agendaId)
                .orElseThrow(() -> new CampoInvalidoExeception("agendaId", "Agenda não encontrada."));
    }
}
