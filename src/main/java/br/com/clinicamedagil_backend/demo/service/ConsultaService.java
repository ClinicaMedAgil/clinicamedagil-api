package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.controller.dto.FinalizarConsultaMedicoDTO;
import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import br.com.clinicamedagil_backend.demo.entities.Agendamento;
import br.com.clinicamedagil_backend.demo.entities.Consulta;
import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.OperacaoNaoPerminitidaException;
import br.com.clinicamedagil_backend.demo.exceptions.RegistroDuplicadoException;
import br.com.clinicamedagil_backend.demo.repository.AgendamentoRepository;
import br.com.clinicamedagil_backend.demo.repository.ConsultaRepository;
import br.com.clinicamedagil_backend.demo.repository.HorarioAgendaRepository;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_ATENDENTE = "ATENDENTE";
    private static final String ROLE_MEDICO = "MEDICO";
    private static final String ROLE_PACIENTE = "PACIENTE";
    private static final String ROLE_USUARIO = "USUARIO";
    private static final String MSG_USUARIO_SEM_ID = "Usuário autenticado sem identificador.";
    private static final String STATUS_HORARIO_DISPONIVEL = "DISPONIVEL";
    private static final String STATUS_HORARIO_RESERVADO = "RESERVADO";

    /** Marcada, ainda não encerrada pelo médico. */
    public static final String STATUS_CONSULTA_AGENDADA = "AGENDADA";
    /** Encerrada pelo endpoint de finalização (médico/atendente/admin). */
    public static final String STATUS_CONSULTA_FINALIZADA = "FINALIZADA";

    private static final String MSG_CONFLITO_AGENDA =
            "Já existe uma consulta marcada com essa especialidade ou para essa data e horário.";

    private final ConsultaRepository repository;
    private final AgendamentoRepository agendamentoRepository;
    private final HorarioAgendaRepository horarioAgendaRepository;
    private final AgendaMedicoService agendaMedicoService;
    private final UsuarioRepository usuarioRepository;

    public List<Consulta> listarTodos() {
        return repository.findAll();
    }

    public List<Consulta> listarMinhas(String email, Collection<String> authorities) {
        Usuario usuarioLogado = buscarUsuarioLogado(email);
        long usuarioId = Objects.requireNonNull(usuarioLogado.getId(),
                MSG_USUARIO_SEM_ID);
        if (hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE)) {
            return repository.findAll();
        }
        if (hasRole(authorities, ROLE_MEDICO)) {
            return repository.findByMedicoIdWithDetalhes(usuarioId);
        }
        if (hasAnyRole(authorities, ROLE_PACIENTE, ROLE_USUARIO)) {
            return repository.findByPacienteIdWithDetalhes(usuarioId);
        }
        throw new OperacaoNaoPerminitidaException("Perfil sem permissão para consultar consultas.");
    }

    public List<Consulta> listarMinhasComoPaciente(String email, Collection<String> authorities) {
        if (!hasAnyRole(authorities, ROLE_PACIENTE, ROLE_USUARIO, ROLE_ADMIN, ROLE_ATENDENTE)) {
            throw new OperacaoNaoPerminitidaException("Apenas PACIENTE/USUARIO, ATENDENTE ou ADMIN podem listar consultas de paciente.");
        }
        Usuario usuarioLogado = buscarUsuarioLogado(email);
        long usuarioId = Objects.requireNonNull(usuarioLogado.getId(),
                MSG_USUARIO_SEM_ID);
        if (hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE)) {
            return repository.findAll();
        }
        return repository.findByPacienteIdWithDetalhes(usuarioId);
    }

    /**
     * Somente consultas em que o usuário logado é o paciente (uso em endpoint exclusivo PACIENTE/USUARIO).
     */
    public List<Consulta> listarSomenteDoPacienteLogado(String email) {
        Usuario usuarioLogado = buscarUsuarioLogado(email);
        long usuarioId = Objects.requireNonNull(usuarioLogado.getId(), MSG_USUARIO_SEM_ID);
        return repository.findByPacienteIdWithDetalhes(usuarioId);
    }

    /**
     * Somente consultas em que o usuário logado é o médico (uso em endpoint exclusivo MEDICO).
     */
    public List<Consulta> listarSomenteDoMedicoLogado(String email, Collection<String> authorities) {
        if (!hasRole(authorities, ROLE_MEDICO)) {
            throw new OperacaoNaoPerminitidaException("Apenas MEDICO pode listar suas consultas por este endpoint.");
        }
        Usuario usuarioLogado = buscarUsuarioLogado(email);
        long usuarioId = Objects.requireNonNull(usuarioLogado.getId(), MSG_USUARIO_SEM_ID);
        return repository.findByMedicoIdWithDetalhes(usuarioId);
    }

    public List<Consulta> listarMinhasComoMedico(String email, Collection<String> authorities) {
        if (!hasAnyRole(authorities, ROLE_MEDICO, ROLE_ADMIN, ROLE_ATENDENTE)) {
            throw new OperacaoNaoPerminitidaException("Apenas MEDICO, ATENDENTE ou ADMIN podem listar consultas de médico.");
        }
        Usuario usuarioLogado = buscarUsuarioLogado(email);
        long usuarioId = Objects.requireNonNull(usuarioLogado.getId(),
                MSG_USUARIO_SEM_ID);
        if (hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE)) {
            return repository.findAll();
        }
        return repository.findByMedicoIdWithDetalhes(usuarioId);
    }

    public Consulta buscarPorId(Long id) {
        return repository.findById(Objects.requireNonNull(id, "Id da consulta é obrigatório."))
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Consulta não encontrada."));
    }

    public Consulta buscarPorIdComPermissao(Long id, String email, Collection<String> authorities) {
        Consulta consulta = repository.findByIdWithDetalhes(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Consulta não encontrada."));
        validarAcessoConsulta(consulta, email, authorities);
        return consulta;
    }

    /**
     * Fluxo simplificado: cria agendamento no horário e persiste a consulta (reserva o horário).
     */
    @Transactional
    public Consulta marcarPorHorarioId(Long horarioId, String email, Collection<String> authorities) {
        Objects.requireNonNull(horarioId, "horarioId é obrigatório.");
        Usuario paciente = buscarUsuarioLogado(email);
        HorarioAgenda horario = horarioAgendaRepository.findById(horarioId)
                .orElseThrow(() -> new CampoInvalidoExeception("horarioId", "Horário não encontrado."));
        AgendaMedico agenda = horario.getAgenda();
        if (agenda == null || agenda.getMedico() == null || agenda.getMedico().getId() == null) {
            throw new CampoInvalidoExeception("agenda", "Horário sem agenda ou médico associado.");
        }
        Usuario medico = agenda.getMedico();

        Agendamento agendamento = Agendamento.builder()
                .horario(horario)
                .paciente(paciente)
                .dataMarcacao(LocalDateTime.now())
                .statusAgendamento("MARCADO")
                .build();
        agendamento = agendamentoRepository.save(agendamento);

        Consulta consulta = Consulta.builder()
                .agendamento(agendamento)
                .medico(medico)
                .paciente(paciente)
                .statusConsulta(STATUS_CONSULTA_AGENDADA)
                .build();

        return salvarComPermissao(consulta, email, authorities);
    }

    @Transactional
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

        Agendamento agendamento = resolveAgendamento(consulta);
        Usuario medico = resolveMedico(consulta);
        Usuario paciente = resolvePaciente(consulta);

        HorarioAgenda horario = carregarHorarioDisponivelParaMarcacao(agendamento, medico);

        validarConflitoAgendaMarcacao(paciente.getId(), horario, null);

        if (isConsultaFinalizada(consulta)) {
            throw new CampoInvalidoExeception("statusConsulta", "Não é permitido criar consulta já finalizada.");
        }
        if (consulta.getStatusConsulta() == null || consulta.getStatusConsulta().isBlank()) {
            consulta.setStatusConsulta(STATUS_CONSULTA_AGENDADA);
        }

        consulta.setAgendamento(agendamento);
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);

        Consulta salvo = repository.save(consulta);
        horario.setStatusHorario(STATUS_HORARIO_RESERVADO);
        horarioAgendaRepository.save(horario);
        if (horario.getAgenda() != null && horario.getAgenda().getId() != null) {
            agendaMedicoService.sincronizarStatusAgendaComHorariosLivres(horario.getAgenda().getId());
        }
        return salvo;
    }

    private HorarioAgenda carregarHorarioDisponivelParaMarcacao(Agendamento agendamento, Usuario medico) {
        if (agendamento.getHorario() == null || agendamento.getHorario().getId() == null) {
            throw new CampoInvalidoExeception("horario", "Agendamento sem horário associado.");
        }
        HorarioAgenda horario = horarioAgendaRepository.findById(agendamento.getHorario().getId())
                .orElseThrow(() -> new CampoInvalidoExeception("horarioId", "Horário não encontrado."));
        String status = horario.getStatusHorario();
        if (status == null || !STATUS_HORARIO_DISPONIVEL.equalsIgnoreCase(status.trim())) {
            throw new OperacaoNaoPerminitidaException("Este horário não está disponível para marcação.");
        }
        AgendaMedico agenda = horario.getAgenda();
        if (agenda == null || agenda.getMedico() == null || agenda.getMedico().getId() == null) {
            throw new CampoInvalidoExeception("agenda", "Horário sem agenda ou médico associado.");
        }
        if (!agenda.getMedico().getId().equals(medico.getId())) {
            throw new CampoInvalidoExeception("medicoId", "O médico informado não corresponde ao horário selecionado.");
        }
        return horario;
    }

    public Consulta salvarComPermissao(Consulta consulta, String email, Collection<String> authorities) {
        Usuario usuarioLogado = buscarUsuarioLogado(email);

        if (hasAnyRole(authorities, ROLE_PACIENTE, ROLE_USUARIO)) {
            if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
                consulta.setPaciente(usuarioLogado);
            } else if (!consulta.getPaciente().getId().equals(usuarioLogado.getId())) {
                throw new OperacaoNaoPerminitidaException("Paciente só pode criar consulta para si próprio.");
            }
        }

        return salvar(consulta);
    }

    public Consulta atualizar(Long id, Consulta consulta) {
        Consulta existente = buscarPorId(id);

        if (isConsultaFinalizada(existente)) {
            throw new OperacaoNaoPerminitidaException("Não é permitido alterar uma consulta já finalizada.");
        }

        existente.setAgendamento(resolveAgendamento(consulta));
        existente.setMedico(resolveMedico(consulta));
        existente.setPaciente(resolvePaciente(consulta));
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

    public Consulta atualizarComPermissao(Long id, Consulta consulta, Collection<String> authorities) {
        if (!hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE)) {
            throw new OperacaoNaoPerminitidaException("Apenas ADMIN ou ATENDENTE podem editar consultas.");
        }
        return atualizar(id, consulta);
    }

    @Transactional
    public void deletar(Long id) {
        Consulta existente = buscarPorId(id);

        if (isConsultaFinalizada(existente)) {
            throw new OperacaoNaoPerminitidaException("Não é permitido cancelar uma consulta já finalizada pelo médico.");
        }

        Agendamento agendamento = existente.getAgendamento();
        HorarioAgenda horario = agendamento != null ? agendamento.getHorario() : null;
        if (horario != null) {
            horario.setStatusHorario(STATUS_HORARIO_DISPONIVEL);
            horarioAgendaRepository.save(horario);
            if (horario.getAgenda() != null && horario.getAgenda().getId() != null) {
                agendaMedicoService.sincronizarStatusAgendaComHorariosLivres(horario.getAgenda().getId());
            }
        }

        repository.delete(existente);
        if (agendamento != null && agendamento.getId() != null) {
            agendamentoRepository.deleteById(agendamento.getId());
        }
    }

    public void deletarComPermissao(Long id, String email, Collection<String> authorities) {
        Consulta consulta = buscarPorId(id);
        validarAcessoConsulta(consulta, email, authorities);

        if (!hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE, ROLE_PACIENTE, ROLE_USUARIO)) {
            throw new OperacaoNaoPerminitidaException("Perfil sem permissão para excluir consulta.");
        }
        if (hasAnyRole(authorities, ROLE_PACIENTE, ROLE_USUARIO)
                && !isPacienteDono(consulta, buscarUsuarioLogado(email))) {
            throw new OperacaoNaoPerminitidaException("Paciente só pode cancelar a própria consulta.");
        }

        deletar(id);
    }

    public Consulta finalizarComPermissao(Long id, String email, Collection<String> authorities) {
        Consulta consulta = buscarPorId(id);
        Usuario usuarioLogado = buscarUsuarioLogado(email);

        if (hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE)) {
            return finalizar(consulta);
        }
        if (hasRole(authorities, ROLE_MEDICO) && isMedicoDono(consulta, usuarioLogado)) {
            return finalizar(consulta);
        }

        throw new OperacaoNaoPerminitidaException("Apenas médico responsável, atendente ou administrador podem finalizar a consulta.");
    }

    /**
     * Encerra a consulta pelo médico logado (dono por id_medico), grava dados clínicos opcionais,
     * define status FINALIZADA e retorna a consulta com relacionamentos carregados para o DTO completo.
     */
    @Transactional
    public Consulta finalizarComoMedicoComDadosClinicos(
            Long id,
            String email,
            Collection<String> authorities,
            FinalizarConsultaMedicoDTO dados) {
        if (!hasRole(authorities, ROLE_MEDICO)) {
            throw new OperacaoNaoPerminitidaException("Apenas MEDICO pode encerrar consulta por este endpoint.");
        }
        Consulta consulta = repository.findByIdWithDetalhes(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Consulta não encontrada."));
        Usuario usuarioLogado = buscarUsuarioLogado(email);
        if (!isMedicoDono(consulta, usuarioLogado)) {
            throw new OperacaoNaoPerminitidaException("Apenas o médico responsável pode encerrar esta consulta por este endpoint.");
        }
        if (isConsultaFinalizada(consulta)) {
            throw new OperacaoNaoPerminitidaException("Consulta já finalizada.");
        }
        if (dados != null) {
            if (dados.queixaPrincipal() != null) {
                consulta.setQueixaPrincipal(dados.queixaPrincipal());
            }
            if (dados.historiaDoencaAtual() != null) {
                consulta.setHistoriaDoencaAtual(dados.historiaDoencaAtual());
            }
            if (dados.diagnostico() != null) {
                consulta.setDiagnostico(dados.diagnostico());
            }
            if (dados.prescricao() != null) {
                consulta.setPrescricao(dados.prescricao());
            }
            if (dados.observacoes() != null) {
                consulta.setObservacoes(dados.observacoes());
            }
        }
        consulta.setStatusConsulta(STATUS_CONSULTA_FINALIZADA);
        consulta.setDataConsulta(LocalDateTime.now());
        repository.save(consulta);
        return repository.findByIdWithDetalhes(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Consulta não encontrada após encerramento."));
    }

    private Consulta finalizar(Consulta consulta) {
        if (isConsultaFinalizada(consulta)) {
            throw new OperacaoNaoPerminitidaException("Consulta já finalizada.");
        }
        consulta.setStatusConsulta(STATUS_CONSULTA_FINALIZADA);
        consulta.setDataConsulta(LocalDateTime.now());
        return repository.save(consulta);
    }

    private static boolean isConsultaFinalizada(Consulta c) {
        if (c == null || c.getStatusConsulta() == null) {
            return false;
        }
        return STATUS_CONSULTA_FINALIZADA.equalsIgnoreCase(c.getStatusConsulta().trim());
    }

    /**
     * Impede nova marcação se o paciente já tem consulta não finalizada na mesma especialidade,
     * ou no mesmo dia e horário (data da agenda + início/fim do slot) que outra consulta agendada.
     */
    private void validarConflitoAgendaMarcacao(Long pacienteId, HorarioAgenda horario, Long excludeConsultaId) {
        if (pacienteId == null || horario == null) {
            return;
        }
        AgendaMedico agenda = horario.getAgenda();
        if (agenda == null || agenda.getEspecialidade() == null || agenda.getEspecialidade().getId() == null) {
            return;
        }
        LocalDate dataAgenda = agenda.getDataAgenda();
        LocalTime horaInicio = horario.getHoraInicio();
        LocalTime horaFim = horario.getHoraFim();
        if (dataAgenda == null || horaInicio == null || horaFim == null) {
            return;
        }
        Long espId = agenda.getEspecialidade().getId();
        long mesmaEsp = repository.countAgendadaNaoFinalizadaMesmaEspecialidade(pacienteId, espId, excludeConsultaId);
        long mesmoDiaHora = repository.countAgendadaNaoFinalizadaMesmoDiaEHora(
                pacienteId, dataAgenda, horaInicio, horaFim, excludeConsultaId);
        if (mesmaEsp > 0 || mesmoDiaHora > 0) {
            throw new RegistroDuplicadoException("agenda", MSG_CONFLITO_AGENDA);
        }
    }

    private br.com.clinicamedagil_backend.demo.entities.Agendamento resolveAgendamento(Consulta consulta) {
        Long agendamentoId = consulta.getAgendamento() != null ? consulta.getAgendamento().getId() : null;
        if (agendamentoId == null) {
            throw new CampoInvalidoExeception("agendamentoId", "O agendamento é obrigatório.");
        }
        return agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new CampoInvalidoExeception("agendamentoId", "Agendamento não encontrado."));
    }

    private br.com.clinicamedagil_backend.demo.entities.Usuario resolveMedico(Consulta consulta) {
        Long medicoId = consulta.getMedico() != null ? consulta.getMedico().getId() : null;
        if (medicoId == null) {
            throw new CampoInvalidoExeception("medicoId", "O médico é obrigatório.");
        }
        return usuarioRepository.findById(medicoId)
                .orElseThrow(() -> new CampoInvalidoExeception("medicoId", "Médico não encontrado."));
    }

    private br.com.clinicamedagil_backend.demo.entities.Usuario resolvePaciente(Consulta consulta) {
        Long pacienteId = consulta.getPaciente() != null ? consulta.getPaciente().getId() : null;
        if (pacienteId == null) {
            throw new CampoInvalidoExeception("pacienteId", "O paciente é obrigatório.");
        }
        return usuarioRepository.findById(pacienteId)
                .orElseThrow(() -> new CampoInvalidoExeception("pacienteId", "Paciente não encontrado."));
    }

    private void validarAcessoConsulta(Consulta consulta, String email, Collection<String> authorities) {
        if (hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE)) {
            return;
        }

        Usuario usuarioLogado = buscarUsuarioLogado(email);
        if (hasAnyRole(authorities, ROLE_PACIENTE, ROLE_USUARIO) && isPacienteDono(consulta, usuarioLogado)) {
            return;
        }
        if (hasRole(authorities, ROLE_MEDICO) && isMedicoDono(consulta, usuarioLogado)) {
            return;
        }

        throw new OperacaoNaoPerminitidaException("Você não tem permissão para acessar esta consulta.");
    }

    private Usuario buscarUsuarioLogado(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new CampoInvalidoExeception("email", "Usuário autenticado não encontrado."));
    }

    private boolean isPacienteDono(Consulta consulta, Usuario usuarioLogado) {
        return consulta.getPaciente() != null && consulta.getPaciente().getId() != null
                && consulta.getPaciente().getId().equals(usuarioLogado.getId());
    }

    private boolean isMedicoDono(Consulta consulta, Usuario usuarioLogado) {
        return consulta.getMedico() != null && consulta.getMedico().getId() != null
                && consulta.getMedico().getId().equals(usuarioLogado.getId());
    }

    private boolean hasAnyRole(Collection<String> authorities, String... roles) {
        for (String role : roles) {
            if (hasRole(authorities, role)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRole(Collection<String> authorities, String role) {
        if (authorities == null || role == null || role.isBlank()) {
            return false;
        }
        return authorities.contains("ROLE_" + role) || authorities.contains(role);
    }
}
