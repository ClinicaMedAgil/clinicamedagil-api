package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.Consulta;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.OperacaoNaoPerminitidaException;
import br.com.clinicamedagil_backend.demo.repository.AgendamentoRepository;
import br.com.clinicamedagil_backend.demo.repository.ConsultaRepository;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final ConsultaRepository repository;
    private final AgendamentoRepository agendamentoRepository;
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
            return repository.findByMedicoId(usuarioId);
        }
        if (hasAnyRole(authorities, ROLE_PACIENTE, ROLE_USUARIO)) {
            return repository.findByPacienteId(usuarioId);
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
        return repository.findByPacienteId(usuarioId);
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
        return repository.findByMedicoId(usuarioId);
    }

    public Consulta buscarPorId(Long id) {
        return repository.findById(Objects.requireNonNull(id, "Id da consulta é obrigatório."))
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Consulta não encontrada."));
    }

    public Consulta buscarPorIdComPermissao(Long id, String email, Collection<String> authorities) {
        Consulta consulta = buscarPorId(id);
        validarAcessoConsulta(consulta, email, authorities);
        return consulta;
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

        consulta.setAgendamento(resolveAgendamento(consulta));
        consulta.setMedico(resolveMedico(consulta));
        consulta.setPaciente(resolvePaciente(consulta));

        return repository.save(consulta);
    }

    public Consulta salvarComPermissao(Consulta consulta, String email, Collection<String> authorities) {
        Usuario usuarioLogado = buscarUsuarioLogado(email);

        if (hasRole(authorities, ROLE_PACIENTE)) {
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

        if (existente.getDataConsulta() != null && existente.getDataConsulta().isBefore(LocalDateTime.now())) {
            throw new OperacaoNaoPerminitidaException("Não é permitido alterar uma consulta já realizada.");
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

    public void deletar(Long id) {
        Consulta existente = buscarPorId(id);

        if (existente.getDataConsulta() != null && existente.getDataConsulta().isBefore(LocalDateTime.now())) {
            throw new OperacaoNaoPerminitidaException("Não é permitido excluir uma consulta já realizada.");
        }

        repository.delete(existente);
    }

    public void deletarComPermissao(Long id, String email, Collection<String> authorities) {
        Consulta consulta = buscarPorId(id);
        validarAcessoConsulta(consulta, email, authorities);

        if (!hasAnyRole(authorities, ROLE_ADMIN, ROLE_ATENDENTE, ROLE_PACIENTE)) {
            throw new OperacaoNaoPerminitidaException("Perfil sem permissão para excluir consulta.");
        }
        if (hasRole(authorities, ROLE_PACIENTE) && !isPacienteDono(consulta, buscarUsuarioLogado(email))) {
            throw new OperacaoNaoPerminitidaException("Paciente só pode excluir a própria consulta.");
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

    private Consulta finalizar(Consulta consulta) {
        if (consulta.getDataConsulta() != null && consulta.getDataConsulta().isBefore(LocalDateTime.now())) {
            throw new OperacaoNaoPerminitidaException("Consulta já finalizada.");
        }
        consulta.setDataConsulta(LocalDateTime.now());
        return repository.save(consulta);
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
        return authorities != null && authorities.contains("ROLE_" + role);
    }
}
