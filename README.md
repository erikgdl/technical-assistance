# Assistencia Tecnica API

API REST para gestao de assistencia tecnica, com cadastro de clientes, equipamentos, tecnicos, pecas, servicos e fluxo completo de ordem de servico.

## Visao geral

- **Base URL:** `http://localhost:8080/v1`
- **Formato:** JSON (`application/json`)
- **Autenticacao:** nao implementada
- **Persistencia:** PostgreSQL

## Stack

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- springdoc-openapi 3.0.2
- Lombok
- PostgreSQL Driver
- JUnit 5

## Estrutura do projeto

```text
src/
  main/
    java/com/example/assistencia_tecnica/
      controller/
      service/
      dto/
      database/model/
      database/repository/
      enums/
      exception/
      handler/
    resources/
      application.yaml
  test/
    java/com/example/assistencia_tecnica/
```

## Pre-requisitos

- JDK 21
- Maven 3.9+
- PostgreSQL em execucao

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

1. Crie o banco `assistencia_tecnica` no PostgreSQL.
2. Exporte `DATABASE_USERNAME` e `DATABASE_PASSWORD`.
3. Inicie a aplicacao:

```bash
mvn spring-boot:run
```

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Organizacao por camada

| Camada | Responsabilidade |
| --- | --- |
| `controller` | Exposicao dos endpoints HTTP |
| `service` | Regras de negocio |
| `dto` | Contratos de entrada |
| `database/model` | Entidades JPA |
| `database/repository` | Acesso ao banco |
| `handler` | Tratamento global de excecoes |
| `exception` | Excecoes e payload de erro |
| `enums` | Estados do dominio |

## Modelo de dominio

| Entidade | ID | Principais campos | Relacionamentos |
| --- | --- | --- | --- |
| `ClienteEntity` | UUID | `cpf`, `nome`, `telefone`, `email` | 1:N com `EquipamentoEntity` |
| `EquipamentoEntity` | UUID | `tipo`, `marca`, `modelo`, `numeroSerie` | N:1 com `ClienteEntity` |
| `TecnicoEntity` | Long | `matricula`, `nome`, `especialidade` | Nenhum |
| `PecaEntity` | Long | `nome`, `marca`, `quantidadeEstoque`, `precoUnitario` | Nenhum |
| `ServicoEntity` | Long | `descricao`, `precoBase` | Nenhum |
| `OrdemServicoEntity` | UUID | `numeroOs`, `defeitoRelatado`, `laudoTecnico`, `status`, `dataAbertura`, `dataConclusao`, `valorTotal` | N:1 com cliente, equipamento e tecnico |
| `PecaUtilizadaEntity` | Long | `quantidade`, `precoUnitarioMomento` | N:1 com OS e peca |
| `ServicoRealizadoEntity` | Long | `precoCobrado` | N:1 com OS e servico |

## IDs e paginação

- `UUID`: cliente, equipamento e ordem de servico
- `Long`: tecnico, peca, servico, peca utilizada e servico realizado
- Listagens paginadas usam `page` e `size`
- Padrao de pagina: `page=0`, `size=10`

Ordenacao aplicada nas listagens paginadas:

- Cliente: `nome` asc
- Peca: `nome` asc
- Servico: `descricao` asc
- Ordem de servico: `dataAbertura` desc
- Equipamento: sem ordenacao explicita

## Fluxo da ordem de servico

Status disponiveis no enum:

`ABERTA`, `EM_ANALISE`, `AGUARDANDO_APROVACAO_CLIENTE`, `APROVADA`, `REJEITADA`, `EM_MANUTENCAO`, `CONCLUIDA`, `ENTREGUE`

Transicoes implementadas:

1. Abrir OS: cria com status `ABERTA`, `numeroOs` gerado automaticamente e `valorTotal = 0`.
2. Iniciar analise: apenas se estiver `ABERTA`; vincula tecnico.
3. Registrar laudo: apenas se estiver `EM_ANALISE`.
4. Adicionar peca e servico: apenas se estiver `EM_ANALISE`; soma o valor no total.
5. Enviar para aprovacao: exige laudo preenchido e `valorTotal > 0`.
6. Aprovar: apenas se estiver `AGUARDANDO_APROVACAO_CLIENTE`; valida estoque e baixa as pecas.
7. Iniciar manutencao: apenas se estiver `APROVADA`.
8. Concluir manutencao: apenas se estiver `EM_MANUTENCAO`; registra `dataConclusao`.
9. Entregar equipamento: apenas se estiver `CONCLUIDA`.
10. Reabrir OS: permitido apenas fora de `CONCLUIDA` e `ENTREGUE`.

## Endpoints

### Cliente

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/v1/cliente` | Cadastra cliente |
| GET | `/v1/cliente/todos` | Lista todos |
| GET | `/v1/cliente` | Lista paginada |
| GET | `/v1/cliente/{id}` | Busca por ID |
| GET | `/v1/cliente/cpf/{cpf}` | Busca por CPF |
| GET | `/v1/cliente/busca?nome=...` | Busca por nome |
| DELETE | `/v1/cliente/{id}` | Remove cliente |

### Equipamento

| Metodo | Endpoint | Descricao                      |
| --- | --- |--------------------------------|
| POST | `/v1/equipamento` | Cadastra equipamento           |
| GET | `/v1/equipamento/todos` | Lista todos                    |
| GET | `/v1/equipamento` | Lista paginada                 |
| GET | `/v1/equipamento/{id}` | Busca por ID                   |
| GET | `/v1/equipamento/cliente/{clienteId}` | Lista por cliente              |
| GET | `/v1/equipamento/numero-serie/{numeroSerie}` | Busca por numero de serie IMEI |

### Tecnico

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/v1/tecnico` | Cadastra tecnico |
| GET | `/v1/tecnico/todos` | Lista todos |
| GET | `/v1/tecnico/{id}` | Busca por ID |
| GET | `/v1/tecnico/matricula/{matricula}` | Busca por matricula |

### Peca

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/v1/peca` | Cadastra peca |
| GET | `/v1/peca/todos` | Lista todas |
| GET | `/v1/peca` | Lista paginada |
| GET | `/v1/peca/{id}` | Busca por ID |

### Servico

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/v1/servico` | Cadastra servico |
| GET | `/v1/servico/todos` | Lista todos |
| GET | `/v1/servico` | Lista paginada |
| GET | `/v1/servico/{id}` | Busca por ID |

### Ordem de servico

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/v1/ordens-servico` | Abre OS |
| GET | `/v1/ordens-servico/todos` | Lista todas |
| GET | `/v1/ordens-servico` | Lista paginada |
| GET | `/v1/ordens-servico/{id}` | Busca por ID |
| GET | `/v1/ordens-servico/status/{status}` | Busca por status |
| GET | `/v1/ordens-servico/cliente/{clienteId}` | Busca por cliente |
| GET | `/v1/ordens-servico/tecnico/{tecnicoId}` | Busca por tecnico |
| GET | `/v1/ordens-servico/{id}/pecas` | Lista pecas usadas na OS |
| GET | `/v1/ordens-servico/{id}/servicos` | Lista servicos da OS |
| PATCH | `/v1/ordens-servico/{id}/iniciar-analise/{tecnicoId}` | Inicia analise |
| PATCH | `/v1/ordens-servico/{id}/laudo` | Registra laudo tecnico |
| POST | `/v1/ordens-servico/{id}/pecas` | Adiciona peca na OS |
| POST | `/v1/ordens-servico/{id}/servicos` | Adiciona servico na OS |
| PATCH | `/v1/ordens-servico/{id}/enviar-aprovacao` | Envia para aprovacao |
| PATCH | `/v1/ordens-servico/{id}/aprovar` | Aprova e baixa estoque |
| PATCH | `/v1/ordens-servico/{id}/iniciar-manutencao` | Inicia manutencao |
| PATCH | `/v1/ordens-servico/{id}/reabrir` | Reabre a OS |
| PATCH | `/v1/ordens-servico/{id}/concluir` | Conclui manutencao |
| PATCH | `/v1/ordens-servico/{id}/entregar` | Marca como entregue |

## DTOs principais

### ClienteDto

```json
{
  "cpf": "12345678901",
  "nome": "Joao Silva",
  "telefone": "(11) 91234-5678",
  "email": "joao@email.com"
}
```

Validacoes:

- `cpf`: obrigatorio, 11 digitos, CPF valido
- `nome`: obrigatorio
- `telefone`: obrigatorio, formato `(11) 91234-5678` ou `(11) 1234-5678`
- `email`: obrigatorio e valido

### EquipamentoDto

```json
{
  "clienteId": "e6b8c1a0-5f28-4b8a-8b5f-9c7a0e4d2f55",
  "tipo": "Notebook",
  "marca": "Dell",
  "modelo": "Inspiron 15",
  "numeroSerie": "ABC123XYZ"
}
```

### TecnicoDto

```json
{
  "nome": "Maria Souza",
  "especialidade": "Eletronica"
}
```

### PecaDto

```json
{
  "pecaId": 1,
  "nome": "Teclado",
  "marca": "Gen",
  "quantidadeEstoque": 10,
  "precoUnitario": 120.5
}
```

### ServicoDto

```json
{
  "id": 1,
  "descricao": "Troca de teclado",
  "precoBase": 150.0
}
```

### OrdemServicoDto

```json
{
  "clienteId": "e6b8c1a0-5f28-4b8a-8b5f-9c7a0e4d2f55",
  "equipamentoId": "f0c1a54e-85e9-4c1f-8f6e-5a1b8f91dd2b",
  "defeitoRelatado": "Nao liga"
}
```

### RegistrarLaudoDto

```json
{
  "laudoTecnico": "Fonte queimada"
}
```

### AdicionarPecaDto

```json
{
  "pecaId": 1,
  "quantidadeEstoque": 2
}
```

### AdicionarServicoDto

```json
{
  "id": 1
}
```

## Observacoes sobre os DTOs

- `PecaDto` expoe `pecaId`, mas a criacao atual da peca usa apenas `nome`, `marca`, `quantidadeEstoque` e `precoUnitario`.
- `ServicoDto` expoe `id`, mas a criacao atual do servico usa apenas `descricao` e `precoBase`.
- `ClienteEntity` possui a relacao com equipamentos, mas ela nao e serializada no JSON por causa de `@JsonIgnore`.

## Simulação de Fluxo: Do Atendimento à Entrega

Abaixo está a sequência cronológica exata de chamadas à API simulando um atendimento real.

| Passo | Metodo | Endpoint | Descricao |
| :--- | :--- | :--- | :--- |
| **1** | POST | `/v1/clientes` | Cadastra o cliente no balcão |
| **2** | POST | `/v1/equipamentos` | Vincula o aparelho quebrado ao cliente |
| **3** | POST | `/v1/tecnicos` | Cadastra o técnico que fará o serviço |
| **4** | POST | `/v1/pecas` | Cadastra a peça necessária no estoque |
| **5** | POST | `/v1/servicos` | Cadastra a mão de obra no catálogo |
| **6** | POST | `/v1/ordens-servico` | Abre a OS inicial (Status: ABERTA) |
| **7** | PATCH| `/v1/ordens-servico/{id}/iniciar-analise/{tecnicoId}`| Técnico pega o aparelho (EM_ANALISE) |
| **8** | POST | `/v1/ordens-servico/{id}/pecas` | Adiciona a peça ao orçamento da OS |
| **9** | POST | `/v1/ordens-servico/{id}/servicos` | Adiciona a mão de obra ao orçamento |
| **10**| PATCH| `/v1/ordens-servico/{id}/laudo` | Técnico registra o defeito encontrado |
| **11**| PATCH| `/v1/ordens-servico/{id}/enviar-aprovacao` | Envia orçamento (AGUARDANDO_APROVACAO) |
| **12**| PATCH| `/v1/ordens-servico/{id}/aprovar` | Cliente aprova e abate a peça do estoque |
| **13**| PATCH| `/v1/ordens-servico/{id}/iniciar-manutencao` | Inicia o conserto físico na bancada |
| **14**| PATCH| `/v1/ordens-servico/{id}/concluir` | Finaliza conserto e gera data de conclusão |
| **15**| PATCH| `/v1/ordens-servico/{id}/entregar` | Cliente paga e retira o aparelho (ENTREGUE) |

> 💡 **Nota:** O `{id}` utilizado dos passos 7 ao 15 é o **UUID da Ordem de Serviço** gerado automaticamente no **Passo 6**. Basta 
> copiar o ID retornado na resposta do Passo 6 e usá-lo nas URLs dos passos seguintes.

## Erros e respostas

O handler global retorna:

```json
{
  "message": "Descricao do erro",
  "status": 400
}
```

Codigos tratados explicitamente:

- `400` para `BadRequestException`
- `404` para `NotFoundException`
- `500` para `Exception` generica

Validacoes Bean Validation (`@Valid`) usam o retorno padrao do Spring.

## Testes

O projeto possui apenas o teste padrao de contexto:

- `src/test/java/com/example/assistencia_tecnica/AssistenciaTecnicaApplicationTests.java`

