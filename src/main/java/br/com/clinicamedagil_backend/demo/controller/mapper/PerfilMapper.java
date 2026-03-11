package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.PerfilDTO;
import br.com.clinicamedagil_backend.demo.entities.Perfil;
import org.mapstruct.Mapper;

/**
 * PerfilMapper.interface
 *
 * Mapper resposavel pelo Perfil
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface PerfilMapper {

    PerfilDTO toDTO(Perfil entity);

    Perfil toEntity(PerfilDTO dto);

}
