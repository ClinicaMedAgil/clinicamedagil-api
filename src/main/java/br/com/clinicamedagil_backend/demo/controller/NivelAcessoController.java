package br.com.clinicamedagil_backend.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clinicamedagil_backend.demo.controller.dto.NivelAcessoDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.NivelAcessoMapper;
import br.com.clinicamedagil_backend.demo.entities.NivelAcesso;
import br.com.clinicamedagil_backend.demo.service.NivelAcessoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * NIvelAcessoController.java
 *
 * Classe resposavel pela API de servicos do Nivel de Acesso
 *
 * @author João Paulo - back-end team
 *
 * <pre>
 *     History:
 *         15/03/2026 - feature/controller -  João Paulo
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedagil-service/niveisacesso")
@RequiredArgsConstructor
@Tag(name="NiveisAcessos", description="Gerenciamentos de Acessos de Usuários")
public class NivelAcessoController {

    private final NivelAcessoService service;
    private final NivelAcessoMapper mapper;

    @GetMapping
    @Operation(summary="Relação de NiveisAcessos", description="Lista todos os Niveis de Acessos")
    public ResponseEntity<List<NivelAcessoDTO>> listarTodosNiveisAcesso() {
        List<NivelAcessoDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary="Pesquisa NivelAcesso por Id", description="Buscar Nivel de Acesso por Id")
    public ResponseEntity<NivelAcessoDTO> buscarNivelAcessoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary="Cadastra NivelAcesso", description="Cadastrar Novo Nivel de Acesso")
    public ResponseEntity<NivelAcessoDTO> salvarNivelAcesso(@RequestBody @Valid NivelAcessoDTO dto) {
        NivelAcesso salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary="Atualiza NivelAcesso", description="Alterar informações de Nivel de Acesso já cadastrado") 
    public ResponseEntity<NivelAcessoDTO> atualizarNivelAcesso(@PathVariable Long id,
                                                       @RequestBody @Valid NivelAcessoDTO dto) {
        NivelAcesso atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Deleta NivelAcesso", description="Deletar Nivel de Acesso Pesquisado por ID")
    public ResponseEntity<Void> deletarNivelAcesso(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
