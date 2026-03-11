package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.ConsultaDTO;
import br.com.clinicamedagil_backend.demo.entities.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * ConsultaMapper.interface
 *
 * Mapper resposavel pelo Consulta
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface ConsultaMapper {

    @Mapping(source = "agendamento.id", target = "agendamentoId")
    @Mapping(source = "medico.id", target = "medicoId")
    @Mapping(source = "paciente.id", target = "pacienteId")
    ConsultaDTO toDTO(Consulta entity);

    @Mapping(source = "agendamentoId", target = "agendamento.id")
    @Mapping(source = "medicoId", target = "medico.id")
    @Mapping(source = "pacienteId", target = "paciente.id")
    Consulta toEntity(ConsultaDTO dto);

}
