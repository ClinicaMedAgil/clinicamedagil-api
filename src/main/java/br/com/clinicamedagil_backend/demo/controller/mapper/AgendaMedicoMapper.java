package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.AgendaMedicoDTO;
import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * AgendaMedicoMapper.interface
 *
 * Mapper resposavel pelo AgendaMedico
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface AgendaMedicoMapper {

    @Mapping(source = "medico.id", target = "medicoId")
    @Mapping(source = "especialidade.id", target = "especialidadeId")
    @Mapping(source = "medico.nome", target = "nomeMedico")
    @Mapping(source = "especialidade.nomeEspecialidade", target = "nomeEspecialidade")
    AgendaMedicoDTO toDTO(AgendaMedico entity);

    @Mapping(source = "medicoId", target = "medico.id")
    @Mapping(source = "especialidadeId", target = "especialidade.id")
    AgendaMedico toEntity(AgendaMedicoDTO dto);

}
