package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.EspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import org.mapstruct.Mapper;

/**
 * EspecialidadeMapper.interface
 *
 * Mapper resposavel pelo Especialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface EspecialidadeMapper {

    EspecialidadeDTO toDTO(Especialidade entity);

    Especialidade toEntity(EspecialidadeDTO dto);

}
