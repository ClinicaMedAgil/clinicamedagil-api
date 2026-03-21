package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.MedicoEspecialidadeDTO;
import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    @Mapping(source = "id.idMedico", target = "medicoId")
    @Mapping(source = "id.idEspecialidade", target = "especialidadeId")
    @Mapping(source = "medico", target = "medico")
    @Mapping(source = "especialidade", target = "especialidade")
    MedicoEspecialidadeDTO toDTO(MedicoEspecialidade entity);

    MedicoEspecialidadeDTO.MedicoResumoDTO toMedicoResumo(Usuario usuario);

    @Mapping(source = "nomeEspecialidade", target = "nome")
    MedicoEspecialidadeDTO.EspecialidadeResumoDTO toEspecialidadeResumo(Especialidade esp);

    @Mapping(source = "medicoId", target = "id.idMedico")
    @Mapping(source = "especialidadeId", target = "id.idEspecialidade")
    @Mapping(target = "medico", ignore = true)
    @Mapping(target = "especialidade", ignore = true)
    MedicoEspecialidade toEntity(MedicoEspecialidadeDTO dto);

}