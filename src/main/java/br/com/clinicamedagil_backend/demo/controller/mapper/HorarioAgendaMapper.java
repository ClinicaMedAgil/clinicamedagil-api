package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.HorarioAgendaDTO;
import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * HorarioAgendaMapper.interface
 *
 * Mapper resposavel pelo HorarioAgenda
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface HorarioAgendaMapper {

    @Mapping(source = "agenda.id", target = "agendaId")
    HorarioAgendaDTO toDTO(HorarioAgenda entity);

    @Mapping(source = "agendaId", target = "agenda.id")
    HorarioAgenda toEntity(HorarioAgendaDTO dto);

}