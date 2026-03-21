# Relatorio de correcoes - API + Banco

## Entregas realizadas

- Criado `docker-compose.yml` com PostgreSQL 16, volume persistente e healthcheck.
- Criado script de inicializacao em `docker/postgres/init/01-init.sql`.
- Externalizada configuracao de conexao ao banco em `application.properties` usando variaveis de ambiente.
- Implementada rotina de seed automatica e idempotente em `DataSeederConfig`.

## Seed implementado

Ao iniciar a API com `app.seed.enabled=true`, sao garantidos:

- Tipos de usuario: `USUARIO`, `MEDICO`, `ADMINISTRADOR`, `ATENDENTE`, `PACIENTE`.
- Perfis e niveis de acesso necessarios.
- Pelo menos 2 registros para cada grupo: usuario comum, medico, administrador, atendente e paciente.
- Especialidades medicas basicas.
- Agenda de medico para os medicos seedados.

## Ajustes tecnicos aplicados

- Password do seed parametrizada por `app.seed.default-secret`.
- Seed com verificacao por email e por agenda para evitar duplicidade em reinicios.
- Rotina executada em transacao unica (`@Transactional`) para consistencia.

## Validacoes executadas

- Linter nos arquivos alterados: sem erros apos ajustes.
- Tentativa de subir Postgres via Docker: falhou por permissao local no socket Docker.
- Tentativa de build/teste Maven: falhou por JDK local sem suporte ao `release 21`.

## Pendencias do ambiente local para validacao completa

1. Conceder permissao de uso do Docker ao usuario atual.
2. Executar a API com Java 21 instalado/configurado.
