package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.controller.dto.UsuarioComumDTO;
import br.com.clinicamedagil_backend.demo.controller.dto.AtualizarMeuPerfilDTO;
import br.com.clinicamedagil_backend.demo.entities.NivelAcesso;
import br.com.clinicamedagil_backend.demo.entities.Perfil;
import br.com.clinicamedagil_backend.demo.entities.TipoUsuario;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.RegistroDuplicadoException;
import br.com.clinicamedagil_backend.demo.repository.NivelAcessoRepository;
import br.com.clinicamedagil_backend.demo.repository.PerfilRepository;
import br.com.clinicamedagil_backend.demo.repository.TipoUsuarioRepository;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * UsuarioService.java
 *
 * Classe resposavel pela camada de service do Usuario
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
public class UsuarioService {
    private static final String STATUS_ATIVO = "ATIVO";
    private static final String TIPO_PACIENTE = "PACIENTE";
    private static final String PERFIL_PACIENTE = "PACIENTE";
    private static final String ACESSO_BASICO = "BASICO";

    private final UsuarioRepository repository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PerfilRepository perfilRepository;
    private final NivelAcessoRepository nivelAcessoRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.seed.default-secret:123456}")
    private String defaultSecret;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        Long usuarioId = Objects.requireNonNull(id, "Id do usuário é obrigatório.");
        return repository.findById(usuarioId)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Usuário não encontrado."));
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new CampoInvalidoExeception("email", "Usuário não encontrado."));
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            repository.findByEmail(usuario.getEmail())
                    .ifPresent(u -> {
                        throw new RegistroDuplicadoException("Já existe um usuário cadastrado com este email.");
                    });
        }

        if (usuario.getCpf() != null && !usuario.getCpf().isBlank()) {
            repository.findByCpf(usuario.getCpf())
                    .ifPresent(u -> {
                        throw new RegistroDuplicadoException("Já existe um usuário cadastrado com este CPF.");
                    });
        }

        usuario.setSenha(passwordEncoder.encode(defaultSecret));
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setTipoUsuario(resolveTipoUsuario(usuario));
        usuario.setPerfil(resolvePerfil(usuario));
        usuario.setNivelAcesso(resolveNivelAcesso(usuario));

        return repository.save(Objects.requireNonNull(usuario, "Usuário para salvar é obrigatório."));
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        Usuario existente = buscarPorId(id);

        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            repository.findByEmail(usuario.getEmail())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> {
                        throw new RegistroDuplicadoException("Já existe outro usuário cadastrado com este email.");
                    });
        }

        if (usuario.getCpf() != null && !usuario.getCpf().isBlank()) {
            repository.findByCpf(usuario.getCpf())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> {
                        throw new RegistroDuplicadoException("Já existe outro usuário cadastrado com este CPF.");
                    });
        }

        existente.setNome(usuario.getNome());
        existente.setCpf(usuario.getCpf());
        existente.setEmail(usuario.getEmail());
        existente.setTelefone(usuario.getTelefone());
        existente.setStatus(usuario.getStatus());
        existente.setTipoUsuario(resolveTipoUsuario(usuario));
        existente.setPerfil(resolvePerfil(usuario));
        existente.setNivelAcesso(resolveNivelAcesso(usuario));

        return repository.save(existente);
    }

    public Usuario criarUsuarioComum(UsuarioComumDTO dto) {
        Objects.requireNonNull(dto, "Dados do usuário comum são obrigatórios.");
        validarDuplicidadeEmailCpf(dto.email(), dto.cpf(), null);

        TipoUsuario tipoPaciente = tipoUsuarioRepository.findByNomeIgnoreCase(TIPO_PACIENTE)
                .orElseThrow(() -> new CampoInvalidoExeception("tipoUsuario", "Tipo de usuário PACIENTE não encontrado."));

        Perfil perfilPaciente = perfilRepository.findByNomeIgnoreCase(PERFIL_PACIENTE)
                .orElseThrow(() -> new CampoInvalidoExeception("perfil", "Perfil PACIENTE não encontrado."));

        NivelAcesso nivelBasico = nivelAcessoRepository.findByNomeIgnoreCase(ACESSO_BASICO)
                .orElseThrow(() -> new CampoInvalidoExeception("nivelAcesso", "Nível de acesso BASICO não encontrado."));

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .telefone(dto.telefone())
                .status(STATUS_ATIVO)
                .senha(passwordEncoder.encode(defaultSecret))
                .dataCadastro(LocalDateTime.now())
                .tipoUsuario(tipoPaciente)
                .perfil(perfilPaciente)
                .nivelAcesso(nivelBasico)
                .build();

        return repository.save(Objects.requireNonNull(usuario, "Usuário para salvar é obrigatório."));
    }

    public Usuario atualizarMeuPerfil(String emailAutenticado, AtualizarMeuPerfilDTO dto) {
        Objects.requireNonNull(dto, "Dados de atualização do perfil são obrigatórios.");
        Usuario usuario = buscarPorEmail(emailAutenticado);
        validarDuplicidadeEmailCpf(dto.email(), dto.cpf(), usuario.getId());

        usuario.setNome(dto.nome());
        usuario.setCpf(dto.cpf());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());

        return repository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario existente = buscarPorId(id);
        repository.delete(Objects.requireNonNull(existente, "Usuário para excluir é obrigatório."));
    }

    private br.com.clinicamedagil_backend.demo.entities.TipoUsuario resolveTipoUsuario(Usuario usuario) {
        Long tipoUsuarioId = usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getId() : null;
        if (tipoUsuarioId == null) {
            return null;
        }
        return tipoUsuarioRepository.findById(tipoUsuarioId)
                .orElseThrow(() -> new CampoInvalidoExeception("tipoUsuarioId", "Tipo de usuário não encontrado."));
    }

    private br.com.clinicamedagil_backend.demo.entities.Perfil resolvePerfil(Usuario usuario) {
        Long perfilId = usuario.getPerfil() != null ? usuario.getPerfil().getId() : null;
        if (perfilId == null) {
            return null;
        }
        return perfilRepository.findById(perfilId)
                .orElseThrow(() -> new CampoInvalidoExeception("perfilId", "Perfil não encontrado."));
    }

    private br.com.clinicamedagil_backend.demo.entities.NivelAcesso resolveNivelAcesso(Usuario usuario) {
        Long nivelAcessoId = usuario.getNivelAcesso() != null ? usuario.getNivelAcesso().getId() : null;
        if (nivelAcessoId == null) {
            return null;
        }
        return nivelAcessoRepository.findById(nivelAcessoId)
                .orElseThrow(() -> new CampoInvalidoExeception("nivelAcessoId", "Nível de acesso não encontrado."));
    }

    private void validarDuplicidadeEmailCpf(String email, String cpf, Long idIgnorado) {
        if (email != null && !email.isBlank()) {
            repository.findByEmail(email)
                    .filter(u -> idIgnorado == null || !u.getId().equals(idIgnorado))
                    .ifPresent(u -> {
                        throw new RegistroDuplicadoException("Já existe outro usuário cadastrado com este email.");
                    });
        }

        if (cpf != null && !cpf.isBlank()) {
            repository.findByCpf(cpf)
                    .filter(u -> idIgnorado == null || !u.getId().equals(idIgnorado))
                    .ifPresent(u -> {
                        throw new RegistroDuplicadoException("Já existe outro usuário cadastrado com este CPF.");
                    });
        }
    }
}
