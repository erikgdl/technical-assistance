# 📋 Roadmap de Implementação: Regras de Negócio (Ordem de Serviço)

Este checklist traduz a jornada real do cliente na assistência técnica para tarefas de desenvolvimento no seu pacote `service` (Spring Boot). Use isto para guiar a lógica dos seus métodos e validações.

---

## 🚶‍♂️ Passo 1: A Recepção (Cadastros Básicos)

- [ ] **1.1. Implementar `ClienteService`**
    - [ ] Criar validação para não permitir cadastrar dois clientes com o mesmo CPF.
    - [ ] Criar endpoint `POST /api/clientes`.

- [ ] **1.2. Implementar `EquipamentoService`**
    - [ ] Validar que todo Equipamento receba um `cliente_id` válido no momento da criação.
    - [ ] Se o `cliente_id` não existir no banco, lançar exceção (ex: `ClienteNaoEncontradoException`).
    - [ ] Criar endpoint `POST /api/equipamentos`.

---

## 📝 Passo 2: A Abertura da Ordem de Serviço

- [ ] **2.1. Implementar método `abrirOS` no `OrdemServicoService`**
    - [ ] Receber `cliente_id`, `equipamento_id` e `defeito_relatado`.
    - [ ] Validar se o equipamento pertence de fato ao cliente informado.
    - [ ] Setar o status inicial obrigatoriamente para `ABERTA`.
    - [ ] Setar o `valor_total` inicial para `0.00`.
    - [ ] Gerar a `data_abertura` (Timestamp atual) automaticamente.
    - [ ] Salvar no banco.

---

## 🔍 Passo 3: O Diagnóstico (Laudo Técnico)

- [ ] **3.1. Implementar método `iniciarAnalise`**
    - [ ] Alterar o status de `ABERTA` para `EM_ANALISE`.
    - [ ] Vincular o `tecnico_id` (lançar erro se o técnico não existir).

- [ ] **3.2. Implementar método `registrarLaudo`**
    - [ ] Receber o texto do laudo técnico.
    - [ ] **Regra de Bloqueio:** Não permitir salvar o laudo se o texto estiver vazio.

---

## 💰 Passo 4: Montando o Orçamento (Adicionando Itens)

- [ ] **4.1. Implementar método `adicionarPeca` (Tabela M:N)**
    - [ ] Checar se a OS está no status `EM_ANALISE`. Se não estiver, lançar erro (não pode adicionar peça em OS já aprovada ou entregue).
    - [ ] Buscar a `Peca` no banco para pegar o preço atual.
    - [ ] **Regra de Estoque (Prévia):** Validar se a `quantidade` solicitada existe no estoque global. Se não, lançar `EstoqueInsuficienteException`.
    - [ ] **Congelamento:** Salvar o registro em `Peca_Utilizada` copiando o valor atual para a coluna `preco_unitario_momento`.

- [ ] **4.2. Implementar método `adicionarServico` (Tabela M:N)**
    - [ ] Buscar o `Servico` no banco e salvar em `Servico_Realizado` com o preço daquele instante.

- [ ] **4.3. Implementar Recálculo Automático**
    - [ ] Toda vez que uma peça ou serviço for adicionado, somar os totais e atualizar a coluna `valor_total` da OS.

- [ ] **4.4. Implementar método `enviarParaAprovacao`**
    - [ ] Mudar status para `AGUARDANDO_APROVACAO_CLIENTE`.
    - [ ] **Regra de Bloqueio:** Impedir essa mudança se não houver um laudo técnico preenchido ou se o valor total for 0.

---

## 🤝 Passo 5: A Aprovação e a Baixa de Estoque

- [ ] **5.1. Implementar método `aprovarOS` (Anotar com `@Transactional`)**
    - [ ] Mudar status para `APROVADA`.
    - [ ] **Baixa de Estoque:** Fazer um *loop* em todas as peças utilizadas nesta OS e subtrair a quantidade do estoque global da tabela `Peca`.
    - [ ] Garantir que, se o banco der erro na hora de diminuir o estoque de alguma peça, toda a transação seja desfeita (Rollback) e a OS não seja aprovada.

---

## 🔧 Passo 6: Execução (Mão na Massa)

- [ ] **6.1. Implementar método `iniciarManutencao`**
    - [ ] Mudar status para `EM_MANUTENCAO`.
    - [ ] **Regra de Trava:** Adicionar *Ifs* nos métodos de `adicionarPeca` e `adicionarServico` para **bloquear** qualquer alteração de itens e valores enquanto o status for diferente de `EM_ANALISE`.
    - [ ] Se precisar adicionar peça nova, criar método `reabrirOrcamento` (que volta o status para `EM_ANALISE` e cancela a aprovação anterior).

---

## 🏁 Passo 7: O Fim da Linha (Conclusão e Entrega)

- [ ] **7.1. Implementar método `concluirManutencao`**
    - [ ] Mudar status para `CONCLUIDA`.
    - [ ] Setar a `data_conclusao` (Timestamp atual).

- [ ] **7.2. Implementar método `entregarEquipamento`**
    - [ ] Mudar status final para `ENTREGUE`.
    - [ ] A OS não pode sofrer mais NENHUMA alteração a partir daqui (estado final da máquina de estados).