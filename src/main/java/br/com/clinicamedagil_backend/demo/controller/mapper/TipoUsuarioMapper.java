package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.TipoUsuarioDTO;
import br.com.clinicamedagil_backend.demo.entities.TipoUsuario;
import org.mapstruct.Mapper;

/**
 * TipoUsuarioMapper.interface
 *
 * Mapper resposavel pelo TipoUsuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface TipoUsuarioMapper {

    TipoUsuarioDTO toDTO(TipoUsuario entity);

    TipoUsuario toEntity(TipoUsuarioDTO dto);

}