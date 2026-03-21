package br.com.clinicamedagil_backend.demo.controller.mapper;

import br.com.clinicamedagil_backend.demo.controller.dto.ConsultaDTO;
import br.com.clinicamedagil_backend.demo.entities.Consulta;
import org.mapstruct.BeanMapping;
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
    @Mapping(source = "paciente.nome", target = "nomePaciente")
    @Mapping(source = "agendamento.horario.id", target = "horarioId")
    @Mapping(source = "agendamento.horario.agenda.id", target = "agendaId")
    @Mapping(source = "agendamento.horario.horaInicio", target = "horaInicio")
    @Mapping(source = "agendamento.horario.horaFim", target = "horaFim")
    @Mapping(source = "agendamento.horario.agenda.dataAgenda", target = "dataAgenda")
    @Mapping(source = "medico.nome", target = "nomeMedico")
    @Mapping(source = "agendamento.horario.agenda.especialidade.nomeEspecialidade", target = "nomeEspecialidade")
    @Mapping(source = "agendamento.horario.agenda.especialidade.id", target = "especialidadeId")
    ConsultaDTO toDTO(Consulta entity);

    @BeanMapping(ignoreUnmappedSourceProperties = {
            "nomePaciente", "horarioId", "agendaId", "horaInicio", "horaFim", "dataAgenda",
            "nomeMedico", "nomeEspecialidade", "especialidadeId"
    })
    @Mapping(source = "agendamentoId", target = "agendamento.id")
    @Mapping(source = "medicoId", target = "medico.id")
    @Mapping(source = "pacienteId", target = "paciente.id")
    Consulta toEntity(ConsultaDTO dto);

}
