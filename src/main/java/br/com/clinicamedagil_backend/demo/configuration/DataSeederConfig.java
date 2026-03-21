package br.com.clinicamedagil_backend.demo.configuration;

import br.com.clinicamedagil_backend.demo.entities.AgendaMedico;
import br.com.clinicamedagil_backend.demo.entities.Especialidade;
import br.com.clinicamedagil_backend.demo.entities.HorarioAgenda;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidade;
import br.com.clinicamedagil_backend.demo.entities.MedicoEspecialidadeId;
import br.com.clinicamedagil_backend.demo.entities.NivelAcesso;
import br.com.clinicamedagil_backend.demo.entities.Perfil;
import br.com.clinicamedagil_backend.demo.entities.TipoUsuario;
import br.com.clinicamedagil_backend.demo.entities.Usuario;
import br.com.clinicamedagil_backend.demo.repository.AgendaMedicoRepository;
import br.com.clinicamedagil_backend.demo.repository.EspecialidadeRepository;
import br.com.clinicamedagil_backend.demo.repository.HorarioAgendaRepository;
import br.com.clinicamedagil_backend.demo.repository.MedicoEspecialidadeRepository;
import br.com.clinicamedagil_backend.demo.repository.NivelAcessoRepository;
import br.com.clinicamedagil_backend.demo.repository.PerfilRepository;
import br.com.clinicamedagil_backend.demo.repository.TipoUsuarioRepository;
import br.com.clinicamedagil_backend.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeederConfig implements CommandLineRunner {

    private static final String STATUS_ATIVO = "ATIVO";
    private static final String STATUS_DISPONIVEL = "DISPONIVEL";

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;
    @Value("${app.seed.default-secret:123456}")
    private String seedDefaultSecret;

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PerfilRepository perfilRepository;
    private final NivelAcessoRepository nivelAcessoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final AgendaMedicoRepository agendaMedicoRepository;
    private final MedicoEspecialidadeRepository medicoEspecialidadeRepository;
    private final HorarioAgendaRepository horarioAgendaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        TipoUsuario tipoMedico = saveTipoUsuario("MEDICO");
        TipoUsuario tipoAdministrador = saveTipoUsuario("ADMINISTRADOR");
        TipoUsuario tipoAtendente = saveTipoUsuario("ATENDENTE");
        TipoUsuario tipoPaciente = saveTipoUsuario("PACIENTE");

        Perfil perfilMedico = savePerfil("MEDICO", "Perfil de médico");
        Perfil perfilAdministrador = savePerfil("ADMINISTRADOR", "Perfil administrativo");
        Perfil perfilAtendente = savePerfil("ATENDENTE", "Perfil de atendimento");
        Perfil perfilPaciente = savePerfil("PACIENTE", "Perfil de paciente");

        NivelAcesso acessoBasico = saveNivelAcesso("BASICO", "Acesso básico");
        NivelAcesso acessoMedio = saveNivelAcesso("MEDIO", "Acesso intermediário");
        NivelAcesso acessoTotal = saveNivelAcesso("TOTAL", "Acesso total");

        Usuario medico1 = saveUsuario(new SeedUserData(
                        "Medico 1", "22222222221", "medico1@clinicamedagil.com", "(11)90000-0101",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoMedico, perfilMedico, acessoMedio
        );
        Usuario medico2 = saveUsuario(new SeedUserData(
                        "Medico 2", "22222222222", "medico2@clinicamedagil.com", "(11)90000-0102",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoMedico, perfilMedico, acessoMedio
        );
        Usuario medico3 = saveUsuario(new SeedUserData(
                        "Medico 3", "22222222223", "medico3@clinicamedagil.com", "(11)90000-0103",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoMedico, perfilMedico, acessoMedio
        );

        saveUsuario(new SeedUserData(
                        "Administrador 1", "33333333331", "admin1@clinicamedagil.com", "(11)90000-0201",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoAdministrador, perfilAdministrador, acessoTotal
        );

        saveUsuario(new SeedUserData(
                        "Atendente 1", "44444444441", "atendente1@clinicamedagil.com", "(11)90000-0301",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoAtendente, perfilAtendente, acessoMedio
        );
        saveUsuario(new SeedUserData(
                        "Atendente 2", "44444444442", "atendente2@clinicamedagil.com", "(11)90000-0302",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoAtendente, perfilAtendente, acessoMedio
        );

        saveUsuario(new SeedUserData(
                        "Paciente 1", "55555555551", "paciente1@clinicamedagil.com", "(11)90000-0401",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoPaciente, perfilPaciente, acessoBasico
        );
        saveUsuario(new SeedUserData(
                        "Paciente 2", "55555555552", "paciente2@clinicamedagil.com", "(11)90000-0402",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoPaciente, perfilPaciente, acessoBasico
        );
        saveUsuario(new SeedUserData(
                        "Paciente 3", "55555555553", "paciente3@clinicamedagil.com", "(11)90000-0403",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoPaciente, perfilPaciente, acessoBasico
        );
        saveUsuario(new SeedUserData(
                        "Paciente 4", "55555555554", "paciente4@clinicamedagil.com", "(11)90000-0404",
                        seedDefaultSecret, STATUS_ATIVO
                ),
                tipoPaciente, perfilPaciente, acessoBasico
        );

        Especialidade clinicaGeral = saveEspecialidade("Clinica Geral", "Atendimento clinico geral");
        Especialidade cardiologia = saveEspecialidade("Cardiologia", "Atendimento cardiologico");

        ensureMedicoEspecialidade(medico1, clinicaGeral);
        ensureMedicoEspecialidade(medico1, cardiologia);
        ensureMedicoEspecialidade(medico2, clinicaGeral);
        ensureMedicoEspecialidade(medico2, cardiologia);
        ensureMedicoEspecialidade(medico3, clinicaGeral);
        ensureMedicoEspecialidade(medico3, cardiologia);

        saveAgendaMedico(medico1, clinicaGeral, LocalDate.now().plusDays(1), STATUS_DISPONIVEL);
        saveAgendaMedico(medico1, cardiologia, LocalDate.now().plusDays(2), STATUS_DISPONIVEL);
        saveAgendaMedico(medico2, clinicaGeral, LocalDate.now().plusDays(1), STATUS_DISPONIVEL);
        saveAgendaMedico(medico2, cardiologia, LocalDate.now().plusDays(3), STATUS_DISPONIVEL);
        saveAgendaMedico(medico3, clinicaGeral, LocalDate.now().plusDays(2), STATUS_DISPONIVEL);
        saveAgendaMedico(medico3, cardiologia, LocalDate.now().plusDays(4), STATUS_DISPONIVEL);

        seedHorariosDisponiveisNasAgendas();
    }

    private TipoUsuario saveTipoUsuario(String nome) {
        return tipoUsuarioRepository.findAll().stream()
                .filter(item -> nome.equalsIgnoreCase(item.getNome()))
                .findFirst()
                .orElseGet(() -> tipoUsuarioRepository.save(TipoUsuario.builder().nome(nome).build()));
    }

    private Perfil savePerfil(String nome, String descricao) {
        return perfilRepository.findAll().stream()
                .filter(item -> nome.equalsIgnoreCase(item.getNome()))
                .findFirst()
                .orElseGet(() -> perfilRepository.save(Perfil.builder().nome(nome).descricao(descricao).build()));
    }

    private NivelAcesso saveNivelAcesso(String nome, String descricao) {
        return nivelAcessoRepository.findAll().stream()
                .filter(item -> nome.equalsIgnoreCase(item.getNome()))
                .findFirst()
                .orElseGet(() -> nivelAcessoRepository.save(NivelAcesso.builder().nome(nome).descricao(descricao).build()));
    }

    private Usuario saveUsuario(SeedUserData userData, TipoUsuario tipoUsuario, Perfil perfil, NivelAcesso nivelAcesso) {
        return usuarioRepository.findByEmail(userData.email())
                .orElseGet(() -> usuarioRepository.save(
                        Usuario.builder()
                                .nome(userData.nome())
                                .cpf(userData.cpf())
                                .email(userData.email())
                                .telefone(userData.telefone())
                                .senha(passwordEncoder.encode(userData.senha()))
                                .status(userData.status())
                                .dataCadastro(LocalDateTime.now())
                                .tipoUsuario(tipoUsuario)
                                .perfil(perfil)
                                .nivelAcesso(nivelAcesso)
                                .build()
                ));
    }

    private Especialidade saveEspecialidade(String nomeEspecialidade, String descricao) {
        return especialidadeRepository.findAll().stream()
                .filter(item -> nomeEspecialidade.equalsIgnoreCase(item.getNomeEspecialidade()))
                .findFirst()
                .orElseGet(() -> especialidadeRepository.save(
                        Especialidade.builder()
                                .nomeEspecialidade(nomeEspecialidade)
                                .descricao(descricao)
                                .build()
                ));
    }

    private void ensureMedicoEspecialidade(Usuario medico, Especialidade especialidade) {
        MedicoEspecialidadeId id = new MedicoEspecialidadeId(medico.getId(), especialidade.getId());
        if (medicoEspecialidadeRepository.existsById(id)) {
            return;
        }
        medicoEspecialidadeRepository.save(
                MedicoEspecialidade.builder()
                        .id(id)
                        .medico(medico)
                        .especialidade(especialidade)
                        .build()
        );
    }

    private void seedHorariosDisponiveisNasAgendas() {
        for (AgendaMedico agenda : agendaMedicoRepository.findAll()) {
            if (horarioAgendaRepository.countByAgenda_Id(agenda.getId()) > 0) {
                continue;
            }
            horarioAgendaRepository.save(
                    HorarioAgenda.builder()
                            .agenda(agenda)
                            .horaInicio(LocalTime.of(8, 0))
                            .horaFim(LocalTime.of(8, 30))
                            .statusHorario(STATUS_DISPONIVEL)
                            .build()
            );
            horarioAgendaRepository.save(
                    HorarioAgenda.builder()
                            .agenda(agenda)
                            .horaInicio(LocalTime.of(9, 0))
                            .horaFim(LocalTime.of(9, 30))
                            .statusHorario(STATUS_DISPONIVEL)
                            .build()
            );
        }
    }

    private void saveAgendaMedico(Usuario medico, Especialidade especialidade, LocalDate dataAgenda, String statusAgenda) {
        List<AgendaMedico> agendas = agendaMedicoRepository.findAll();
        boolean exists = agendas.stream().anyMatch(item ->
                item.getMedico() != null
                        && item.getEspecialidade() != null
                        && item.getDataAgenda() != null
                        && item.getMedico().getId().equals(medico.getId())
                        && item.getEspecialidade().getId().equals(especialidade.getId())
                        && item.getDataAgenda().equals(dataAgenda)
        );

        if (!exists) {
            agendaMedicoRepository.save(
                    AgendaMedico.builder()
                            .medico(medico)
                            .especialidade(especialidade)
                            .dataAgenda(dataAgenda)
                            .statusAgenda(statusAgenda)
                            .build()
            );
        }
    }

    private record SeedUserData(
            String nome,
            String cpf,
            String email,
            String telefone,
            String senha,
            String status
    ) {
    }
}
