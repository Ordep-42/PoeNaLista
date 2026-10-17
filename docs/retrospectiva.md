# Retrospectiva da Sprint 1 — PõeNaLista

**Data da Cerimônia:** 16/10/2026  
**Participantes:** Pedro Galvão, Pedro Cursino, Raylana Lara, Viviane Estefani  

---

## 1. Visão Geral da Sprint 1

* **Sprint Goal (Objetivo da Sprint):** "Implementar o fluxo inicial de grupos e cadastro de itens com categorização simples, e criação de grupos de moradores (casas)"
* **Status do Objetivo:** Atingido parcialmente

### Métricas da Sprint
* **Pontos Planejados:** 15 pontos
* **Pontos Concluídos:** 9 pontos
* **Histórias / Issues Entregues:** 
  * US02 — Adicionar e categorizar itens da lista
  * US03 — Marcar itens como comprados/pendentes 
* **Histórias / Issues Não Entregues:** 
  * US01 — Criar e entrar em um grupo de moradia
* **Taxa de Conclusão / Velocidade:** 60% dos pontos da Sprint 1 concluídos

---

## 2. O que funcionou bem

* **Comunicação Assíncrona:** O uso do grupo no WhatsApp para tirar dúvidas e discutir necessidades do projeto manteve o alinhamento da equipe ao longo do ciclo.
* **Qualidade do Código e Testes:** Boa cobertura de testes unitários na camada de serviços do Spring Boot antes da abertura dos PRs.

---

## 3. O que não funcionou tão bem

* **Gargalo na Coluna "Em Revisão":** Houve acúmulo de tarefas e Pull Requests aguardando revisão de código no final do ciclo da sprint, perto do prazo final de entrega.
* **Integração Frontend/Backend:** O fatiamento inicial subestimou o tempo necessário para construir a arquitetura inicial do sistema.

---

## 4. Análise de Causa Raiz e Desvios de Processo

* **Análise da causa raiz do acúmulo de PRs:** Os desenvolvedores tendiam a focar em puxar novos cartões para a coluna *"Em progresso"* em vez de parar para revisar o código dos colegas na coluna *"Em revisão"*.
* **Efeito Observado:** Concentração de merges nos últimos dias da sprint, o que aumentou a pressão na etapa final de validação do build.

---

## 5. Plano de Ação de Melhoria (Saída Obrigatória)

De acordo com as diretrizes do Scrum/XP aplicadas à disciplina, selecionamos **exatamente 1 ação concreta de melhoria** priorizada para execução imediata na Sprint 2:

* **Ação Selecionada:** **Aplicação Rigorosa da Regra "Code Review First" (Prioridade para Revisão de Código)**.
  * **O que será feito:** Sempre que a coluna *"Em revisão"* no GitHub Projects atingir o limite estipulado de 2 cartões, o desenvolvimento de novos cartões em *"Em progresso"* será interrompido até que ao menos um Pull Request seja revisado, aprovado e integrado na branch `main`.
  * **Responsável por monitorar:** Pedro Galvão
  * **Critério de Sucesso:** Nenhum Pull Request aberto sem revisão por mais de 24 horas e zerar o acúmulo de PRs nos dois dias finais da Sprint 2.
