package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.AgendamentoDTO;
import br.com.clinicamedagil_backend.demo.entities.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * AgendamentoMapper.interface
 *
 * Mapper resposavel pelo Agendamento
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface AgendamentoMapper {

    @Mapping(source = "horario.id", target = "horarioId")
    @Mapping(source = "paciente.id", target = "pacienteId")
    AgendamentoDTO toDTO(Agendamento entity);

    @Mapping(source = "horarioId", target = "horario.id")
    @Mapping(source = "pacienteId", target = "paciente.id")
    Agendamento toEntity(AgendamentoDTO dto);

}