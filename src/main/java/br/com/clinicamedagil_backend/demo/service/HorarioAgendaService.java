package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.OperacaoNaoPerminitidaException;
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

        return repository.save(horarioAgenda);
    }

    public HorarioAgenda atualizar(Long id, HorarioAgenda horarioAgenda) {
        HorarioAgenda existente = buscarPorId(id);

        if (horarioAgenda.getHoraInicio() != null && horarioAgenda.getHoraFim() != null
                && !horarioAgenda.getHoraFim().isAfter(horarioAgenda.getHoraInicio())) {
            throw new OperacaoNaoPerminitidaException("A hora final deve ser maior que a hora inicial.");
        }

        existente.setAgenda(horarioAgenda.getAgenda());
        existente.setHoraInicio(horarioAgenda.getHoraInicio());
        existente.setHoraFim(horarioAgenda.getHoraFim());
        existente.setStatusHorario(horarioAgenda.getStatusHorario());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        HorarioAgenda existente = buscarPorId(id);
        repository.delete(existente);
    }
}
