package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.NivelAcessoDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.NivelAcessoMapper;
import br.com.clinicamedagil_backend.demo.entities.NivelAcesso;
import br.com.clinicamedagil_backend.demo.service.NivelAcessoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/clinicamedadil-service/niveisacesso")
@RequiredArgsConstructor
public class NivelAcessoController {

    private final NivelAcessoService service;
    private final NivelAcessoMapper mapper;

    @GetMapping
    public ResponseEntity<List<NivelAcessoDTO>> listarTodosNiveisAcesso() {
        List<NivelAcessoDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelAcessoDTO> buscarNivelAcessoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<NivelAcessoDTO> salvarNivelAcesso(@RequestBody @Valid NivelAcessoDTO dto) {
        NivelAcesso salvo = service.salvar(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelAcessoDTO> atualizarNivelAcesso(@PathVariable Long id,
                                                       @RequestBody @Valid NivelAcessoDTO dto) {
        NivelAcesso atualizado = service.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarNivelAcesso(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
