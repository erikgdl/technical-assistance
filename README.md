# Assistência Técnica API

API REST para gestão de assistência técnica, com cadastro de clientes, equipamentos, técnicos, peças, serviços e fluxo completo de ordem de serviço.

## Visão geral

- **Base URL:** `http://localhost:8080/v1`
- **Formato:** JSON (`application/json`)
- **Autenticação:** não implementada
- **Persistência:** PostgreSQL

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
.
|-- Dockerfile
|-- docker-compose.yml
|-- pom.xml
|-- README.md
|-- src
|   |-- main
|   |   |-- java/com/example/assistencia_tecnica
|   |   |   |-- AssistenciaTecnicaApplication.java
|   |   |   |-- config/
|   |   |   |-- controller/
|   |   |   |-- database/
|   |   |   |   |-- model/
|   |   |   |   |-- repository/
|   |   |   |-- dto/
|   |   |   |-- enums/
|   |   |   |-- exception/
|   |   |   |-- handler/
|   |   |   |-- service/
|   |   |-- resources/
|   |       |-- application.yaml
|   |-- test
|       |-- java/com/example/assistencia_tecnica/AssistenciaTecnicaApplicationTests.java
|-- target/
```

## Pré-requisitos

- JDK 21
- Maven 3.9+
- PostgreSQL em execução
- Docker e Docker Compose (opcional para execução em containers)

## Configuração

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

Variáveis de ambiente obrigatórias:

- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

> Ao usar Docker Compose, as credenciais são fornecidas por `DB_USERNAME` e `DB_PASSWORD` (ver seção Docker).

## CORS

Origens permitidas (configurado em `CorsConfig`):

- `http://localhost:5173`
- `https://technical-assistance-now.netlify.app`

## Como executar

1. Crie o banco `assistencia_tecnica` no PostgreSQL.
2. Exporte `DATABASE_USERNAME` e `DATABASE_PASSWORD`.
3. Inicie a aplicação:

```bash
mvn spring-boot:run
```

Para rodar os testes:

```bash
mvn test
```

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Organização por camada

| Camada | Responsabilidade |
| --- | --- |
| `controller` | Exposição dos endpoints HTTP |
| `service` | Regras de negócio |
| `dto` | Contratos de entrada |
| `database/model` | Entidades JPA |
| `database/repository` | Acesso ao banco |
| `handler` | Tratamento global de exceções |
| `exception` | Exceções e payload de erro |
| `enums` | Estados do domínio |
| `config` | Configurações da aplicação (ex.: CORS) |

## Modelo de domínio

| Entidade | ID | Principais campos | Relacionamentos |
| --- | --- | --- | --- |
| `ClienteEntity` | UUID | `cpf`, `nome`, `telefone`, `email` | 1:N com `EquipamentoEntity` |
| `EquipamentoEntity` | UUID | `tipo`, `marca`, `modelo`, `numeroSerie` | N:1 com `ClienteEntity` |
| `TecnicoEntity` | Long | `matricula`, `nome`, `especialidade` | Nenhum |
| `PecaEntity` | Long | `nome`, `marca`, `quantidadeEstoque`, `precoUnitario` | Nenhum |
| `ServicoEntity` | Long | `descricao`, `precoBase` | Nenhum |
| `OrdemServicoEntity` | UUID | `defeitoRelatado`, `laudoTecnico`, `status`, `dataAbertura`, `dataConclusao`, `valorTotal` | N:1 com cliente, equipamento e técnico |
| `PecaUtilizadaEntity` | Long | `quantidade`, `precoUnitarioMomento` | N:1 com OS e peça |
| `ServicoRealizadoEntity` | Long | `precoCobrado` | N:1 com OS e serviço |

## IDs e paginação

- `UUID`: cliente, equipamento e ordem de serviço
- `Long`: técnico, peça, serviço, peça utilizada e serviço realizado
- Listagens paginadas usam `page` e `size`
- Padrão de página: `page=0`, `size=10`

Ordenação aplicada nas listagens paginadas:

- Cliente: `nome` asc
- Peça: `nome` asc
- Serviço: `descricao` asc
- Ordem de serviço: `dataAbertura` desc
- Equipamento: sem ordenação explícita

## Fluxo da ordem de serviço

Status disponíveis no enum:

`ABERTA`, `EM_ANALISE`, `AGUARDANDO_APROVACAO_CLIENTE`, `APROVADA`, `REJEITADA`, `EM_MANUTENCAO`, `CONCLUIDA`, `ENTREGUE`

Transições implementadas:

1. Abrir OS: cria com status `ABERTA`, `valorTotal = 0` e `dataAbertura`.
2. Iniciar análise: apenas se estiver `ABERTA`; vincula técnico.
3. Registrar laudo: apenas se estiver `EM_ANALISE`.
4. Adicionar peça: apenas se estiver `EM_ANALISE`; valida estoque, baixa o estoque e soma no total.
5. Adicionar serviço: apenas se estiver `EM_ANALISE`; soma no total.
6. Enviar para aprovação: exige laudo preenchido e `valorTotal > 0`.
7. Aprovar: apenas se estiver `AGUARDANDO_APROVACAO_CLIENTE`.
8. Iniciar manutenção: apenas se estiver `APROVADA`.
9. Concluir manutenção: apenas se estiver `EM_MANUTENCAO`; registra `dataConclusao`.
10. Entregar equipamento: apenas se estiver `CONCLUIDA`.
11. Reabrir OS: permitido apenas fora de `CONCLUIDA` e `ENTREGUE`; volta para `EM_ANALISE`.

## Endpoints

### Cliente

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/v1/cliente` | Cadastra cliente |
| PUT | `/v1/cliente/atualiza/{id}` | Atualiza cliente |
| GET | `/v1/cliente/todos` | Lista todos |
| GET | `/v1/cliente` | Lista paginada |
| GET | `/v1/cliente/{id}` | Busca por ID |
| GET | `/v1/cliente/cpf/{cpf}` | Busca por CPF |
| GET | `/v1/cliente/busca?nome=...` | Busca por nome |
| DELETE | `/v1/cliente/{id}` | Remove cliente |

### Equipamento

| Método | Endpoint | Descrição                      |
| --- | --- |--------------------------------|
| POST | `/v1/equipamento` | Cadastra equipamento           |
| PUT | `/v1/equipamento/atualiza/{id}` | Atualiza equipamento          |
| GET | `/v1/equipamento/todos` | Lista todos                    |
| GET | `/v1/equipamento` | Lista paginada                 |
| GET | `/v1/equipamento/{id}` | Busca por ID                   |
| GET | `/v1/equipamento/cliente/{clienteId}` | Lista por cliente              |
| GET | `/v1/equipamento/numero-serie/{numeroSerie}` | Busca por número de série IMEI |
| DELETE | `/v1/equipamento/{id}` | Remove equipamento |

### Técnico

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/v1/tecnico` | Cadastra técnico |
| GET | `/v1/tecnico/todos` | Lista todos |
| PUT | `/v1/tecnico/atualiza/{id}` | Atualiza técnico |
| GET | `/v1/tecnico/{id}` | Busca por ID |
| GET | `/v1/tecnico/matricula/{matricula}` | Busca por matrícula |
| DELETE | `/v1/tecnico/{id}` | Remove técnico |

### Peça

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/v1/peca` | Cadastra peça |
| GET | `/v1/peca/todos` | Lista todas |
| PUT | `/v1/peca/atualiza/{id}` | Atualiza peça |
| GET | `/v1/peca` | Lista paginada |
| GET | `/v1/peca/{id}` | Busca por ID |
| DELETE | `/v1/peca/{id}` | Remove peça |

### Serviço

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/v1/servico` | Cadastra serviço |
| GET | `/v1/servico/todos` | Lista todos |
| PUT | `/v1/servico/atualiza/{id}` | Atualiza serviço |
| GET | `/v1/servico` | Lista paginada |
| GET | `/v1/servico/{id}` | Busca por ID |
| DELETE | `/v1/servico/{id}` | Remove serviço |

### Ordem de serviço

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/v1/ordens-servico` | Abre OS |
| GET | `/v1/ordens-servico/todos` | Lista todas |
| GET | `/v1/ordens-servico` | Lista paginada |
| GET | `/v1/ordens-servico/{id}` | Busca por ID |
| GET | `/v1/ordens-servico/status/{status}` | Busca por status |
| GET | `/v1/ordens-servico/cliente/{clienteId}` | Busca por cliente |
| GET | `/v1/ordens-servico/tecnico/{tecnicoId}` | Busca por técnico |
| GET | `/v1/ordens-servico/{id}/pecas` | Lista peças usadas na OS |
| GET | `/v1/ordens-servico/{id}/servicos` | Lista serviços da OS |
| PATCH | `/v1/ordens-servico/{id}/iniciar-analise/{tecnicoId}` | Inicia análise |
| PATCH | `/v1/ordens-servico/{id}/laudo` | Registra laudo técnico |
| POST | `/v1/ordens-servico/{id}/pecas` | Adiciona peça na OS |
| POST | `/v1/ordens-servico/{id}/servicos` | Adiciona serviço na OS |
| PATCH | `/v1/ordens-servico/{id}/enviar-aprovacao` | Envia para aprovação |
| PATCH | `/v1/ordens-servico/{id}/aprovar` | Aprova e baixa estoque |
| PATCH | `/v1/ordens-servico/{id}/iniciar-manutencao` | Inicia manutenção |
| PATCH | `/v1/ordens-servico/{id}/reabrir` | Reabre a OS |
| PATCH | `/v1/ordens-servico/{id}/concluir` | Conclui manutenção |
| PATCH | `/v1/ordens-servico/{id}/entregar` | Marca como entregue |
| DELETE | `/v1/ordens-servico/{id}` | Remove a OS |

### Dashboard

| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | `/v1/dashboard` | Retorna dados agregados do sistema |

Exemplo de resposta:

```json
{
  "totalOS": 128,
  "totalCliente": 64,
  "totalOSAberta": 12,
  "totalOSEmManutencao": 5
}
```

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

Validações:

- `cpf`: obrigatório, 11 dígitos, CPF válido
- `nome`: obrigatório
- `telefone`: obrigatório, formato `(11) 91234-5678` ou `(11) 1234-5678`
- `email`: obrigatório e válido

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

## Observações sobre os DTOs

- `PecaDto` expõe `pecaId`, mas a criação atual da peça usa apenas `nome`, `marca`, `quantidadeEstoque` e `precoUnitario`.
- `ServicoDto` expõe `id`, mas a criação atual do serviço usa apenas `descricao` e `precoBase`.
- `ClienteEntity` possui a relação com equipamentos, mas ela não é serializada no JSON por causa de `@JsonIgnore`.
- `TecnicoEntity` gera `matricula` automaticamente no cadastro (5 dígitos).

## Simulação de Fluxo: Do Atendimento à Entrega

Abaixo está a sequência cronológica exata de chamadas à API simulando um atendimento real.

| Passo | Método | Endpoint | Descrição |
| :--- | :--- | :--- | :--- |
| **1** | POST | `/v1/cliente` | Cadastra o cliente no balcão |
| **2** | POST | `/v1/equipamento` | Vincula o aparelho quebrado ao cliente |
| **3** | POST | `/v1/tecnico` | Cadastra o técnico que fará o serviço |
| **4** | POST | `/v1/peca` | Cadastra a peça necessária no estoque |
| **5** | POST | `/v1/servico` | Cadastra a mão de obra no catálogo |
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
  "message": "Descrição do erro",
  "status": 400
}
```

Códigos tratados explicitamente:

- `400` para `BadRequestException`
- `404` para `NotFoundException`
- `500` para `Exception` genérica

Validações Bean Validation (`@Valid`) usam o retorno padrão do Spring.

## Execução com Docker

A aplicação possui suporte nativo para execução em containers, orquestrando a API e o banco de dados em um ambiente isolado.

### Arquivos de configuração

- **Dockerfile**: Configurado com multi-stage build. Realiza a compilação limpa via Maven no primeiro estágio e executa a aplicação utilizando uma imagem enxuta do Eclipse Temurin 21 no segundo estágio.
- **docker-compose.yml**: Levanta os serviços `db` (PostgreSQL 16) e `app` (API REST). Configurado com persistência de dados (volumes), limites de memória (RAM) por container e injeção segura de variáveis de ambiente.

### Variáveis de ambiente do Compose

Para que a orquestração funcione corretamente, é necessário fornecer as credenciais do banco:

- `DB_USERNAME`
- `DB_PASSWORD`

### Como iniciar via Docker

Na raiz do projeto (onde os arquivos Docker estão localizados), execute:

```bash
docker-compose up -d --build
```

## Considerações Finais

Valeu por conferir o projeto!

Esta API foi desenvolvida com o foco em aplicar e consolidar na prática os conceitos do ecossistema Java e Spring Boot. A ideia foi estruturar uma base sólida, limpa e funcional para um cenário real de assistência técnica.

Fique à vontade para clonar, explorar o código e testar a aplicação no seu ambiente. Se encontrar algum *bug*, tiver alguma sugestão de melhoria ou quiser contribuir com otimizações no código, é só abrir uma *issue* ou mandar um *pull request*.

Se este repositório foi útil para você de alguma forma, considere deixar uma ⭐.
