package br.com.clinicamedagil_backend.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clinicamedagil_backend.demo.controller.dto.ConsultaDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.ConsultaMapper;
import br.com.clinicamedagil_backend.demo.entities.Consulta;
import br.com.clinicamedagil_backend.demo.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AgendaMedicoController.java
 *
 * Classe responsavel pela API de controller do Consultas
 *
 * @author Dailes Santos - back-end team
 *
 * <pre>
 *     History:
 *         14/03/2026 - feature/controller -  Dailes Santos
 * </pre>
 */
@RestController
@RequestMapping("/clinicamedagil-service/consultas")
@RequiredArgsConstructor
@Tag(name="Consultas", description="Gerenciamentos de Consultas")
public class ConsultasController {

    private final ConsultaService service;
    private final ConsultaMapper mapper;

    @GetMapping
    @Operation(summary="Relação de Consultas", description="Listar Todas Consultas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ATENDENTE')")
    public ResponseEntity<List<ConsultaDTO>> listarTodosConsultas() {
        List<ConsultaDTO> lista = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/minhas")
    @Operation(summary="Relação de Minhas Consultas", description="Listar consultas do usuário autenticado")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('MEDICO') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<List<ConsultaDTO>> listarMinhasConsultas(Authentication authentication) {
        List<ConsultaDTO> lista = service.listarMinhas(authentication.getName(), authorities(authentication))
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/paciente")
    @Operation(summary="Relação de Consultas do Paciente", description="Paciente visualiza apenas consultas em que é o id_paciente")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('USUARIO') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<List<ConsultaDTO>> listarConsultasPaciente(Authentication authentication) {
        List<ConsultaDTO> lista = service.listarMinhasComoPaciente(authentication.getName(), authorities(authentication))
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/medico")
    @Operation(summary="Relação de Consultas do Médico", description="Médico visualiza apenas consultas em que é o id_medico")
    @PreAuthorize("hasRole('MEDICO') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<List<ConsultaDTO>> listarConsultasMedico(Authentication authentication) {
        List<ConsultaDTO> lista = service.listarMinhasComoMedico(authentication.getName(), authorities(authentication))
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary="Pesquisa Consulta por Id", description="Localizar Consultar por Id")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('ATENDENTE') or hasRole('MEDICO') or hasRole('ADMIN')")
    public ResponseEntity<ConsultaDTO> buscarConsultaPorId(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(mapper.toDTO(service.buscarPorIdComPermissao(id, authentication.getName(), authorities(authentication))));
    }

    @PostMapping
    @Operation(summary="Cadastra Consulta", description="Cadastrar Nova Consulta")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<ConsultaDTO> salvarConsulta(@RequestBody @Valid ConsultaDTO dto, Authentication authentication) {
        Consulta salvo = service.salvarComPermissao(mapper.toEntity(dto), authentication.getName(), authorities(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary="Atualiza Consulta", description="Alterar Informações de Consulta já Cadastrada")
    @PreAuthorize("hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<ConsultaDTO> atualizarConsulta(@PathVariable Long id,
                                                @RequestBody @Valid ConsultaDTO dto,
                                                Authentication authentication) {
        Consulta atualizado = service.atualizarComPermissao(id, mapper.toEntity(dto), authorities(authentication));
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary="Deleta Consulta", description="Deletar Consulta Pesquisada por ID")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id, Authentication authentication) {
        service.deletarComPermissao(id, authentication.getName(), authorities(authentication));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id:\\d+}/finalizar")
    @Operation(summary = "Finalizar Consulta", description = "Finaliza consulta do médico responsável")
    @PreAuthorize("hasRole('MEDICO') or hasRole('ATENDENTE') or hasRole('ADMIN')")
    public ResponseEntity<ConsultaDTO> finalizarConsulta(@PathVariable Long id, Authentication authentication) {
        Consulta finalizada = service.finalizarComPermissao(id, authentication.getName(), authorities(authentication));
        return ResponseEntity.ok(mapper.toDTO(finalizada));
    }

    private List<String> authorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .toList();
    }
}
