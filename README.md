# ClinicaMedAgil Backend

API REST do sistema ClinicaMedAgil.

## Tecnologias

- Java
- Spring Boot
- Maven
- PostgreSQL
- Docker
- GitHub Actions

## Responsabilidades da API

- Regras de negocio
- Autenticacao e autorizacao com JWT
- Comunicacao com banco de dados
- Exposicao de endpoints para consumo do frontend

## Configuracao padrao (ambiente local)

- Porta da API: `8081`
- Base URL: `http://localhost:8081/clinicamedagil-service`
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- Banco padrao: `jdbc:postgresql://localhost:5433/db_cliagil`

## Autenticacao

O endpoint de login e publico:

- `POST /clinicamedagil-service/auth/login`

Exemplo de request:

```json
{
  "email": "usuario@dominio.com",
  "senha": "123456"
}
```

Exemplo de response:

```json
{
  "token": "jwt...",
  "tipo": "Bearer"
}
```

Para os endpoints protegidos, enviar:

```http
Authorization: Bearer <token>
```

## Rotas da API

### Auth

- `POST /clinicamedagil-service/auth/login` (publica)
- `PUT /clinicamedagil-service/auth/change-password` (autenticada)

### Usuarios

- `GET /clinicamedagil-service/usuarios`
- `GET /clinicamedagil-service/usuarios/{id}`
- `POST /clinicamedagil-service/usuarios`
- `POST /clinicamedagil-service/usuarios/criarUsuario` (ADMIN)
- `POST /clinicamedagil-service/usuarios/criarUsuarioComum` (publica)
- `PUT /clinicamedagil-service/usuarios/{id}`
- `DELETE /clinicamedagil-service/usuarios/{id}`
- `PUT /clinicamedagil-service/usuarios/alterar-senha` (autenticada)

## Troca de senha

As duas rotas abaixo fazem a mesma operacao (mantido para compatibilidade com frontend):

- `PUT /clinicamedagil-service/auth/change-password`
- `PUT /clinicamedagil-service/usuarios/alterar-senha`

As duas exigem token JWT no header:

```http
Authorization: Bearer <token>
```

Payload:

```json
{
  "senhaAtual": "123456",
  "novaSenha": "novaSenha123"
}
```

Resposta de sucesso:

- `204 No Content`

### Tipos de Usuario

- `GET /clinicamedagil-service/tiposusuarios`
- `GET /clinicamedagil-service/tiposusuarios/{id}`
- `POST /clinicamedagil-service/tiposusuarios`
- `PUT /clinicamedagil-service/tiposusuarios/{id}`
- `DELETE /clinicamedagil-service/tiposusuarios/{id}`

### Perfis

- `GET /clinicamedagil-service/perfis`
- `GET /clinicamedagil-service/perfis/{id}`
- `POST /clinicamedagil-service/perfis`
- `PUT /clinicamedagil-service/perfis/{id}`
- `DELETE /clinicamedagil-service/perfis/{id}`

### Niveis de Acesso

- `GET /clinicamedagil-service/niveisacesso`
- `GET /clinicamedagil-service/niveisacesso/{id}`
- `POST /clinicamedagil-service/niveisacesso`
- `PUT /clinicamedagil-service/niveisacesso/{id}`
- `DELETE /clinicamedagil-service/niveisacesso/{id}`

### Especialidades

- `GET /clinicamedagil-service/especialidades`
- `GET /clinicamedagil-service/especialidades/{id}`
- `POST /clinicamedagil-service/especialidades`
- `PUT /clinicamedagil-service/especialidades/{id}`
- `DELETE /clinicamedagil-service/especialidades/{id}`

### Agendamentos

- `GET /clinicamedagil-service/agendamentos`
- `GET /clinicamedagil-service/agendamentos/{id}`
- `POST /clinicamedagil-service/agendamentos`
- `PUT /clinicamedagil-service/agendamentos/{id}`
- `DELETE /clinicamedagil-service/agendamentos/{id}`

### Agenda de Medicos

- `GET /clinicamedagil-service/agendamedicos`
- `GET /clinicamedagil-service/agendamedicos/{id}`
- `POST /clinicamedagil-service/agendamedicos`
- `PUT /clinicamedagil-service/agendamedicos/{id}`
- `DELETE /clinicamedagil-service/agendamedicos/{id}`

### Consultas

- `GET /clinicamedagil-service/consultas`
- `GET /clinicamedagil-service/consultas/{id}`
- `POST /clinicamedagil-service/consultas`
- `PUT /clinicamedagil-service/consultas/{id}`
- `DELETE /clinicamedagil-service/consultas/{id}`

### Horarios de Agenda

- `GET /clinicamedagil-service/horariosagendas`
- `GET /clinicamedagil-service/horariosagendas/{id}`
- `POST /clinicamedagil-service/horariosagendas`
- `PUT /clinicamedagil-service/horariosagendas/{id}`
- `DELETE /clinicamedagil-service/horariosagendas/{id}`

### Medico Especialidade

- `GET /clinicamedagil-service/medicoespecialidade`
- `GET /clinicamedagil-service/medicoespecialidade/{medicoId}/{especialidadeId}`
- `POST /clinicamedagil-service/medicoespecialidade`
- `DELETE /clinicamedagil-service/medicoespecialidade/{medicoId}/{especialidadeId}`

## Perfis/roles usados na autorizacao

Os endpoints usam combinacoes das roles:

- `ADMIN`
- `ATENDENTE`
- `MEDICO`
- `USUARIO`

Hierarquia aplicada na seguranca:

- `ADMIN` herda permissoes de `ATENDENTE`, `MEDICO` e `USUARIO`
- `ATENDENTE` herda permissoes de `MEDICO` e `USUARIO`
- `MEDICO` herda permissoes de `USUARIO`

## Regra de senha no recurso de usuario

- O `UsuarioDTO` nao expõe o campo `senha` nas respostas.
- `POST /usuarios` e `PUT /usuarios/{id}` nao recebem senha no payload.
- Senha inicial de novo usuario e definida no backend com `app.seed.default-secret`.
- No endpoint publico `POST /usuarios/criarUsuarioComum`:
  - nao recebe senha no payload;
  - nao recebe tipo de usuario no payload;
  - senha inicial e `123456` (via `app.seed.default-secret`);
  - tipo de usuario e fixado como `PACIENTE`.
- Alteracao de senha acontece somente pelos endpoints dedicados:
  - `PUT /clinicamedagil-service/auth/change-password`
  - `PUT /clinicamedagil-service/usuarios/alterar-senha`

## Regra de status no login

- Apenas usuarios com `status = ATIVO` podem autenticar.
- Usuarios com status diferente de `ATIVO` (ex.: `INATIVO`) nao conseguem login.
- Respostas esperadas no login:
  - `401` para credenciais invalidas.
  - `403` para usuario inativo.

## Prompt pronto para o frontend

Use o texto abaixo para orientar a implementacao de integracao no frontend:

```text
Voce e responsavel por integrar o frontend React com a API ClinicaMedAgil.

Contexto tecnico:
- Base URL local: http://localhost:8081/clinicamedagil-service
- Autenticacao JWT:
  - Endpoint: POST /auth/login
  - Request: { "email": string, "senha": string }
  - Response: { "token": string, "tipo": "Bearer" }
- Para endpoints protegidos, enviar header:
  Authorization: Bearer <token>

Recursos disponiveis (CRUD):
- /usuarios
- /tiposusuarios
- /perfis
- /niveisacesso
- /especialidades
- /agendamentos
- /agendamedicos
- /consultas
- /horariosagendas
- /medicoespecialidade (inclui rota composta /{medicoId}/{especialidadeId})

Requisitos de implementacao:
1) Criar camada de API (axios/fetch) com:
   - apiClient centralizado
   - interceptador para anexar token JWT
   - tratamento padronizado de erros (401, 403, 404, 500)
2) Criar fluxo de autenticacao:
   - tela de login
   - persistencia de token (localStorage ou cookie)
   - logout
   - protecao de rotas privadas
3) Implementar controle de acesso por perfil/role:
   - ADMIN, ATENDENTE, MEDICO, USUARIO
   - ocultar acoes nao permitidas na UI
4) Criar servicos/tipos para cada recurso
   - listagem, detalhe, criacao, edicao e exclusao
5) Criar fallback de configuracao:
   - usar variavel de ambiente FRONTEND_API_URL
   - default para http://localhost:8081/clinicamedagil-service
6) Adicionar documentacao de integracao:
   - como configurar .env
   - como autenticar
   - exemplos de chamadas e respostas

Entregue:
- Estrutura de servicos pronta
- Exemplo funcional completo de login + listagem de um recurso
- Guia curto de uso para o time
```

## Testes com profile PostgreSQL

Os testes usam o profile `test-postgres` por padrao em `src/test/resources/application.properties`.

Arquivo do profile:

- `src/test/resources/application-test-postgres.properties`

Variaveis opcionais para testes:

- `SPRING_TEST_DATASOURCE_URL`
- `SPRING_TEST_DATASOURCE_USERNAME`
- `SPRING_TEST_DATASOURCE_PASSWORD`

Exemplo:

- `SPRING_TEST_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db_cliagil_test ./mvnw test`
