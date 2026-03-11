package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.UsuarioDTO;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * UsuarioMapper.interface
 *
 * Mapper resposavel pelo Usuario
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "tipoUsuario.id", target = "tipoUsuarioId")
    @Mapping(source = "perfil.id", target = "perfilId")
    @Mapping(source = "nivelAcesso.id", target = "nivelAcessoId")
    UsuarioDTO toDTO(Usuario entity);

    @Mapping(source = "tipoUsuarioId", target = "tipoUsuario.id")
    @Mapping(source = "perfilId", target = "perfil.id")
    @Mapping(source = "nivelAcessoId", target = "nivelAcesso.id")
    Usuario toEntity(UsuarioDTO dto);

}
