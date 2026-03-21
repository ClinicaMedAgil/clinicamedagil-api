package br.com.clinicamedagil_backend.demo.controller;

import br.com.clinicamedagil_backend.demo.controller.dto.ConsultaDTO;
import br.com.clinicamedagil_backend.demo.controller.mapper.ConsultaMapper;
import br.com.clinicamedagil_backend.demo.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Lista de consultas apenas para o próprio paciente (usuário logado = paciente da consulta).
 */
@RestController
@RequestMapping("/clinicamedagil-service/paciente/consultas")
@RequiredArgsConstructor
@Tag(name = "Paciente — minhas consultas", description = "Consultas do paciente autenticado")
public class PacienteConsultasController {

    private final ConsultaService consultaService;
    private final ConsultaMapper consultaMapper;

    @GetMapping("/minhas")
    @Operation(
            summary = "Minhas consultas",
            description = "Lista as consultas em que o usuário autenticado é o paciente. "
                    + "Apenas perfis PACIENTE ou USUARIO.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('USUARIO')")
    public ResponseEntity<List<ConsultaDTO>> listarMinhasConsultas(Authentication authentication) {
        List<ConsultaDTO> lista = consultaService.listarSomenteDoPacienteLogado(authentication.getName())
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(
            summary = "Cancelar minha consulta",
            description = "Remove o agendamento da consulta cujo id foi informado. "
                    + "Só é permitido se o paciente logado for o dono da consulta (id_paciente). "
                    + "Libera o horário na agenda. Perfis PACIENTE ou USUARIO.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('USUARIO')")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id, Authentication authentication) {
        consultaService.deletarComPermissao(id, authentication.getName(), authorities(authentication));
        return ResponseEntity.noContent().build();
    }

    private List<String> authorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .toList();
    }
}
