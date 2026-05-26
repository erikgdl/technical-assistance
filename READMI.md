# Assistencia Tecnica API

API REST para gestao de assistencia tecnica com cadastro de clientes, equipamentos, tecnicos, pecas e servicos, e fluxo completo de ordens de servico. Persistencia via PostgreSQL e documentacao OpenAPI.

## Stack

- Java 21
- Spring Boot 4.0.6 (WebMVC, Data JPA)
- PostgreSQL
- Springdoc OpenAPI (Swagger UI)
- Lombok
- JUnit 5

## Requisitos

- JDK 21
- PostgreSQL
- Maven Wrapper (mvnw)

## Estrutura do projeto

```
src/main/java/com/example/assistencia_tecnica
  AssistenciaTecnicaApplication.java
  controller/
  service/
  database/
    model/
    repository/
  dto/
  enums/
  exception/
  handler/
src/main/resources/application.yaml
src/test/java/com/example/assistencia_tecnica/AssistenciaTecnicaApplicationTests.java
```

## Configuracao

Arquivo: `src/main/resources/application.yaml`

```yaml
server:
  port: 8080
spring:
  application:
    name: assistencia-tecnica
  datasource:
    url: jdbc:postgresql://localhost:5432/assistencia_tecnica
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

Variaveis de ambiente obrigatorias:

- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

## Como executar

1. Suba o PostgreSQL e crie o banco `assistencia_tecnica`.
2. Configure as variaveis de ambiente do banco.
3. Execute a aplicacao:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
.\mvnw.cmd spring-boot:run
```

Servidor padrao: `http://localhost:8080`

## Documentacao OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Modelo de dados

| Entidade | Chave | Campos principais | Relacionamentos |
| --- | --- | --- | --- |
| **Cliente** | UUID | `cpf`, `nome`, `telefone`, `email` | 1:N com **Equipamento** |
| **Equipamento** | UUID | `tipo`, `marca`, `modelo`, `numeroSerie` | N:1 com **Cliente** |
| **Tecnico** | Long | `matricula`, `nome`, `especialidade` | 1:N com **OrdemServico** |
| **Peca** | Long | `nome`, `marca`, `quantidadeEstoque`, `precoUnitario` | 1:N via **PecaUtilizada** |
| **Servico** | Long | `descricao`, `precoBase` | 1:N via **ServicoRealizado** |
| **OrdemServico** | UUID | `defeitoRelatado`, `laudoTecnico`, `status`, `dataAbertura`, `dataConclusao`, `valorTotal` | N:1 com **Cliente**, **Equipamento**, **Tecnico** |
| **PecaUtilizada** | Long | `quantidade`, `precoUnitarioMomento` | N:1 com **OrdemServico** e **Peca** |
| **ServicoRealizado** | Long | `precoCobrado` | N:1 com **OrdemServico** e **Servico** |

## API

**Prefixo:** `/v1`

| Metodo | Endpoint | Descricao | Body |
| :--- | :--- | :--- | :--- |
| **POST** | `/cliente` | Cadastrar cliente | `ClienteDto` |
| **POST** | `/equipamento` | Cadastrar equipamento | `EquipamentoDto` |
| **POST** | `/tecnico` | Cadastrar tecnico | `TecnicoDto` |
| **POST** | `/peca` | Cadastrar peca em estoque | `PecaDto` |
| **POST** | `/servico` | Cadastrar servico no catalogo | `ServicoDto` |
| **POST** | `/ordens-servico` | Abrir OS | `OrdemServicoDto` |
| **PATCH** | `/ordens-servico/{id}/iniciar-analise/{tecnicoId}` | Iniciar analise | - |
| **PATCH** | `/ordens-servico/{id}/laudo` | Registrar laudo tecnico | `RegistrarLaudoDto` |
| **POST** | `/ordens-servico/{id}/pecas` | Adicionar peca a OS | `AdicionarPecaDto` |
| **POST** | `/ordens-servico/{id}/servicos` | Adicionar servico a OS | `AdicionarServicoDto` |
| **PATCH** | `/ordens-servico/{id}/enviar-aprovacao` | Enviar OS para aprovacao | - |
| **PATCH** | `/ordens-servico/{id}/aprovar` | Aprovar OS (abate o estoque) | - |
| **PATCH** | `/ordens-servico/{id}/iniciar-manutencao` | Iniciar manutencao | - |
| **PATCH** | `/ordens-servico/{id}/reabrir` | Reabrir OS para nova analise | - |
| **PATCH** | `/ordens-servico/{id}/concluir` | Concluir manutencao | - |
| **PATCH** | `/ordens-servico/{id}/entregar` | Entregar equipamento | - |

Observacao: a API atualmente expoe somente cadastro e fluxo de ordens de servico (nao ha endpoints de listagem/consulta).

## DTOs (requests)

**ClienteDto**
```json
{
  "cpf": "12345678901",
  "nome": "Joao Silva",
  "telefone": "(11) 91234-5678",
  "email": "joao@email.com"
}
```

**EquipamentoDto**
```json
{
  "clienteId": "e6b8c1a0-5f28-4b8a-8b5f-9c7a0e4d2f55",
  "tipo": "Notebook",
  "marca": "Dell",
  "modelo": "Inspiron 15",
  "numeroSerie": "ABC123XYZ"
}
```

**TecnicoDto** (matricula gerada automaticamente)
```json
{
  "nome": "Maria Souza",
  "especialidade": "Eletronica"
}
```

**PecaDto** (o campo `pecaId` e ignorado no cadastro)
```json
{
  "nome": "Teclado",
  "marca": "Gen",
  "quantidadeEstoque": 10,
  "precoUnitario": 120.50
}
```

**ServicoDto** (o campo `id` e ignorado no cadastro)
```json
{
  "descricao": "Troca de teclado",
  "precoBase": 150.00
}
```

**OrdemServicoDto**
```json
{
  "clienteId": "e6b8c1a0-5f28-4b8a-8b5f-9c7a0e4d2f55",
  "equipamentoId": "f0c1a54e-85e9-4c1f-8f6e-5a1b8f91dd2b",
  "defeitoRelatado": "Nao liga"
}
```

**AdicionarPecaDto**
```json
{
  "pecaId": 1,
  "quantidadeEstoque": 2
}
```

**AdicionarServicoDto**
```json
{
  "id": 1
}
```

**RegistrarLaudoDto**
```json
{
  "laudoTecnico": "Fonte queimada"
}
```

### Validacoes principais

- `ClienteDto.cpf` deve ter 11 digitos e ser um CPF valido.
- `ClienteDto.telefone` deve seguir o padrao `(11) 91234-5678`.
- `PecaDto.quantidadeEstoque` >= 0 e `precoUnitario` >= 0.
- `ServicoDto.precoBase` >= 0.
- `AdicionarPecaDto.pecaId` deve ser positivo.
- `AdicionarServicoDto.id` deve ser positivo.
- `RegistrarLaudoDto.laudoTecnico` nao pode estar em branco.

## Fluxo de Ordem de Servico

Status possiveis:

`ABERTA -> EM_ANALISE -> AGUARDANDO_APROVACAO_CLIENTE -> APROVADA -> EM_MANUTENCAO -> CONCLUIDA -> ENTREGUE`

Existe o status `REJEITADA` no enum, mas nao ha endpoint para transicionar para ele.

Regras principais (conforme service):

- **Abrir OS:** cliente e equipamento precisam existir e estar relacionados. Status inicial `ABERTA`, `valorTotal = 0` e `dataAbertura` com a data atual.
- **Iniciar analise:** somente se a OS estiver `ABERTA`; tecnico e vinculado.
- **Registrar laudo:** somente se a OS estiver `EM_ANALISE`.
- **Adicionar pecas/servicos:** somente se a OS estiver `EM_ANALISE`. Para pecas, valida estoque suficiente e soma no `valorTotal`.
- **Enviar para aprovacao:** somente se `EM_ANALISE`, com laudo preenchido e `valorTotal > 0`.
- **Aprovar OS:** somente se `AGUARDANDO_APROVACAO_CLIENTE`; valida estoque e abate as pecas.
- **Iniciar manutencao:** somente se `APROVADA`.
- **Reabrir OS:** permitido desde que a OS nao esteja `CONCLUIDA` ou `ENTREGUE`; volta para `EM_ANALISE`.
- **Concluir manutencao:** somente se `EM_MANUTENCAO`; define `dataConclusao`.
- **Entregar equipamento:** somente se `CONCLUIDA`.

## Erros

Resposta padrao para erros de negocio e "not found":
```json
{
  "message": "Descricao do erro",
  "status": 400
}
```

Codigos mais comuns:

- **400**: regra de negocio (BadRequestException)
- **404**: recurso nao encontrado (NotFoundException)
- **500**: erro nao tratado (Exception)

Observacao: erros de validacao (`@Valid`) seguem o formato padrao do Spring.

## Build e testes

```bash
./mvnw clean test
```
