package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.MedicoEspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import org.mapstruct.Mapper;

/**
 * MedicoEspecialidadeMapper.interface
 *
 * Mapper resposavel pelo MedicoEspecialidade
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface MedicoEspecialidadeMapper {

    MedicoEspecialidadeDTO toDTO(MedicoEspecialidade entity);

    MedicoEspecialidade toEntity(MedicoEspecialidadeDTO dto);

}