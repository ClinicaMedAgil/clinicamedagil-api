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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clinicamedagil_backend.demo.controller.dto.FinalizarConsultaMedicoDTO;
import br.com.clinicamedagil_backend.demo.entities.Consulta;

import java.util.List;

/**
 * Lista de consultas apenas para o próprio médico (usuário logado = médico da consulta, id_medico).
 */
@RestController
@RequestMapping("/clinicamedagil-service/medico/consultas")
@RequiredArgsConstructor
@Tag(name = "Médico — minhas consultas", description = "Consultas do médico autenticado")
public class MedicoConsultasController {

    private final ConsultaService consultaService;
    private final ConsultaMapper consultaMapper;

    @GetMapping({"", "/minhas"})
    @Operation(
            summary = "Minhas consultas",
            description = "Lista as consultas em que o usuário autenticado é o médico (id_medico). "
                    + "Apenas perfil MEDICO.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConsultaDTO>> listarMinhasConsultas(Authentication authentication) {
        List<ConsultaDTO> lista = consultaService.listarSomenteDoMedicoLogado(
                        authentication.getName(),
                        authorities(authentication))
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id:\\d+}/finalizar")
    @Operation(
            summary = "Encerrar consulta (médico)",
            description = "Grava opcionalmente queixa, história, diagnóstico, prescrição e observações; "
                    + "define statusConsulta = FINALIZADA e dataConsulta = instante do encerramento. "
                    + "Resposta com todos os dados da consulta (incl. agenda, horário, especialidade). "
                    + "Somente o médico responsável (id_medico). Corpo JSON opcional.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConsultaDTO> finalizarConsulta(
            @PathVariable Long id,
            @RequestBody(required = false) FinalizarConsultaMedicoDTO dados,
            Authentication authentication) {
        Consulta finalizada = consultaService.finalizarComoMedicoComDadosClinicos(
                id,
                authentication.getName(),
                authorities(authentication),
                dados);
        return ResponseEntity.ok(consultaMapper.toDTO(finalizada));
    }

    private List<String> authorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .toList();
    }
}
