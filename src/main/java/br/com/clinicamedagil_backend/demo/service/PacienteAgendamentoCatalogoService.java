package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.controller.dto.AgendaComHorariosDisponiveisDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.AgendaMedicoDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.HorarioAgendaDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.AgendaMedicoMapper;
import br.com.clinicamedagil_backend.demo.controller.mapper.HorarioAgendaMapper;
import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.repository.AgendaMedicoRepository;
import br.com.clinicamedagil_backend.demo.repository.EspecialidadeRepository;
import br.com.clinicamedagil_backend.demo.repository.HorarioAgendaRepository;
import br.com.clinicamedagil_backend.demo.repository.MedicoEspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteAgendamentoCatalogoService {

    private static final String STATUS_DISPONIVEL = "DISPONIVEL";

    /** Agenda publicada para o paciente; DISPONIVEL mantido por compatibilidade com dados antigos. */
    private static final List<String> STATUS_AGENDA_ATIVA_PARA_PACIENTE = List.of("ATIVA", "DISPONIVEL");

    private final EspecialidadeService especialidadeService;
    private final EspecialidadeRepository especialidadeRepository;
    private final MedicoEspecialidadeRepository medicoEspecialidadeRepository;
    private final AgendaMedicoRepository agendaMedicoRepository;
    private final HorarioAgendaRepository horarioAgendaRepository;
    private final AgendaMedicoMapper agendaMedicoMapper;
    private final HorarioAgendaMapper horarioAgendaMapper;

    public List<Especialidade> listarEspecialidades() {
        return especialidadeService.listarTodos();
    }

    public Especialidade buscarEspecialidade(Long id) {
        return especialidadeService.buscarPorId(Objects.requireNonNull(id, "Id da especialidade é obrigatório."));
    }

    public List<MedicoEspecialidade> listarMedicosPorEspecialidade(Long especialidadeId) {
        especialidadeService.buscarPorId(especialidadeId);
        return medicoEspecialidadeRepository.findByEspecialidadeId(especialidadeId);
    }

    /**
     * Resolve especialidade por nome (igualdade ignorando maiúsculas; se não achar, busca parcial única)
     * e retorna vínculos médico–especialidade.
     */
    public List<MedicoEspecialidade> listarMedicosPorNomeEspecialidade(String nomeEspecialidade) {
        if (nomeEspecialidade == null || nomeEspecialidade.isBlank()) {
            throw new CampoInvalidoExeception("nome", "Informe o nome da especialidade.");
        }
        String trimmed = nomeEspecialidade.trim();
        Optional<Especialidade> exata = especialidadeRepository.findByNomeEspecialidadeIgnoreCase(trimmed);
        Long especialidadeId;
        if (exata.isPresent()) {
            especialidadeId = exata.get().getId();
        } else {
            List<Especialidade> parciais = especialidadeRepository.findByNomeEspecialidadeContainingIgnoreCase(trimmed);
            if (parciais.isEmpty()) {
                throw new CampoInvalidoExeception("nome", "Nenhuma especialidade encontrada com esse nome.");
            }
            if (parciais.size() > 1) {
                throw new CampoInvalidoExeception(
                        "nome",
                        "Várias especialidades correspondem à busca. Informe o nome completo da especialidade.");
            }
            especialidadeId = parciais.get(0).getId();
        }
        return medicoEspecialidadeRepository.findByEspecialidadeId(especialidadeId);
    }

    /** Agendas futuras ativas do médico (todas as especialidades) com horários DISPONIVEL. */
    public List<AgendaComHorariosDisponiveisDTO> listarAgendasComHorariosDisponiveisPorMedico(Long medicoId) {
        Objects.requireNonNull(medicoId, "Id do médico é obrigatório.");
        LocalDate hoje = LocalDate.now();
        List<AgendaMedico> agendas = agendaMedicoRepository.findAtivasPorMedicoDesde(
                medicoId, STATUS_AGENDA_ATIVA_PARA_PACIENTE, hoje);
        return montarAgendasComHorariosDisponiveis(agendas);
    }

    public List<AgendaComHorariosDisponiveisDTO> listarAgendasComHorariosDisponiveis(Long especialidadeId, Long medicoId) {
        MedicoEspecialidadeId relacaoId = new MedicoEspecialidadeId(
                Objects.requireNonNull(medicoId, "Id do médico é obrigatório."),
                Objects.requireNonNull(especialidadeId, "Id da especialidade é obrigatório."));
        if (!medicoEspecialidadeRepository.existsById(relacaoId)) {
            throw new CampoInvalidoExeception(
                    "medicoId",
                    "Este médico não está vinculado à especialidade informada.");
        }

        LocalDate hoje = LocalDate.now();
        List<AgendaMedico> agendas = agendaMedicoRepository.findAtivasDesde(
                medicoId, especialidadeId, STATUS_AGENDA_ATIVA_PARA_PACIENTE, hoje);

        return montarAgendasComHorariosDisponiveis(agendas);
    }

    private List<AgendaComHorariosDisponiveisDTO> montarAgendasComHorariosDisponiveis(List<AgendaMedico> agendas) {
        if (agendas.isEmpty()) {
            return List.of();
        }
        List<Long> agendaIds = agendas.stream().map(AgendaMedico::getId).toList();
        List<HorarioAgenda> horarios = horarioAgendaRepository.findByAgenda_IdInAndStatusHorario(agendaIds, STATUS_DISPONIVEL);
        Map<Long, List<HorarioAgenda>> porAgenda = horarios.stream()
                .filter(h -> h.getAgenda() != null && h.getAgenda().getId() != null)
                .collect(Collectors.groupingBy(h -> h.getAgenda().getId()));
        List<AgendaComHorariosDisponiveisDTO> resultado = new ArrayList<>();
        for (AgendaMedico agenda : agendas) {
            AgendaMedicoDTO agendaDto = agendaMedicoMapper.toDTO(agenda);
            List<HorarioAgenda> lista = porAgenda.getOrDefault(agenda.getId(), Collections.emptyList());
            List<HorarioAgendaDTO> horariosDto = lista.stream()
                    .sorted((a, b) -> {
                        if (a.getHoraInicio() == null || b.getHoraInicio() == null) {
                            return 0;
                        }
                        return a.getHoraInicio().compareTo(b.getHoraInicio());
                    })
                    .map(horarioAgendaMapper::toDTO)
                    .toList();
            resultado.add(new AgendaComHorariosDisponiveisDTO(agendaDto, horariosDto));
        }
        return resultado;
    }
}
