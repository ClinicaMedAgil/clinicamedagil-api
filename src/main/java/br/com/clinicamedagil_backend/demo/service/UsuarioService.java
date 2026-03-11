package br.com.clinicamedagil_backend.demo.service;

import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.exceptions.CampoInvalidoExeception;
import br.com.clinicamedagil_backend.demo.exceptions.RegistroDuplicadoException;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    private final UsuarioRepository repository;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CampoInvalidoExeception("id", "Usuário não encontrado."));
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

        usuario.setDataCadastro(LocalDateTime.now());
        return repository.save(usuario);
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
        existente.setSenha(usuario.getSenha());
        existente.setStatus(usuario.getStatus());
        existente.setTipoUsuario(usuario.getTipoUsuario());
        existente.setPerfil(usuario.getPerfil());
        existente.setNivelAcesso(usuario.getNivelAcesso());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        Usuario existente = buscarPorId(id);
        repository.delete(existente);
    }
}
