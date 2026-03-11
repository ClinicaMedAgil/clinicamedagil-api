package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.NivelAcessoDTO;
import br.com.clinicamedagil_backend.demo.entities.NivelAcesso;
import org.mapstruct.Mapper;

/**
 * NivelAcessoMapper.interface
 *
 * Mapper resposavel pelo NivelAcesso
 *
 * @author Christian Fonseca - back-end team
 *
 * <pre>
 *     History:
 *         10/03/2026 - feature/implementacao-dto -  Christian Fonseca
 * </pre>
 */
@Mapper(componentModel = "spring")
public interface NivelAcessoMapper {

    NivelAcessoDTO toDTO(NivelAcesso entity);

    NivelAcesso toEntity(NivelAcessoDTO dto);

}
