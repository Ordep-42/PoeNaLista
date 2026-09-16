# Proposta do Projeto: PoeNaLista

## 1. Visão do Produto

```
Para moradores de repúblicas e famílias
Que compram itens repetidos ou esquecem produtos no mercado
O PoeNaLista é um gerenciador colaborativo de listas de compras
Que sincroniza em tempo real o que precisa ser comprado e o que já está no carrinho
Diferente de listas de papel ou mensagens perdidas no WhatsApp
Nosso produto agrupa os itens por categoria e notifica os moradores sobre o status da compra
```

## 2. Definição do MVP

**O que ENTRA no MVP:**
* Criação de lista de compras compartilhada em tempo real.
* Funcionalidades básicas de CRUD (adicionar, editar, remover itens).
* Marcação de itens como "comprados" ou "pendentes".
* Categorização simples de produtos (ex: limpeza, frios, laticínios).
* Cadastro básico de grupos/moradores.

**O que FICA DE FORA do MVP:**
* Integração com preços reais de supermercados.
* Divisão financeira do valor da compra entre os moradores.
* Autenticação complexa de usuários (ex: login via Google/OAuth).

**Hipótese de valor:** 
Acreditamos que pessoas que dividem a mesma casa vão utilizar o PoeNaLista de forma colaborativa porque ele evita gastos duplicados, reduz idas desnecessárias ao mercado e elimina falhas de comunicação sobre o que falta na despensa.

---

## 3. Backlog Inicial
Quadro Kanban da equipe: [https://github.com/users/Ordep-42/projects/10](https://github.com/users/Ordep-42/projects/10)

## 4. Stack Tecnológico e Justificativa

Para o desenvolvimento do PoeNaLista, a equipe optou pela linguagem Java (JDK 17) utilizando o ecossistema Spring Boot (Spring MVC e Spring Data JPA) para o backend e a framework Thymeleaf como motor de renderização no frontend, utilizando Apache Maven para o gerenciamento de dependências e build. A persistência de dados será feita com PostgreSQL em ambiente persistente e H2 Database para execuções em memória, complementados por JUnit e Mockito para a construção de testes automatizados.

A escolha por uma arquitetura monolítica com renderização no servidor (Server-Side Rendering) via Thymeleaf justifica-se pela busca por produtividade e simplicidade operacional. Ao evitar a separação entre uma API e uma aplicação frontend autônoma, a equipe elimina o overhead de gerenciar builds distintos, rotas duplicadas e configurações de CORS.

Além da produtividade oferecida pelas convenções do Spring Boot na criação das operações de CRUD para moradores, grupos e itens, a stack destaca-se pela maturidade em suporte a testes unitários e de integração. O uso do banco de dados H2 em memória possibilita a execução rápida e isolada da suíte de testes a cada Pull Request na pipeline de integração contínua (CI), garantindo a validação automatizada das histórias de usuário e suportando a Definição de Pronto (DoD) acordada pelo time.

## 5. Acordo de Processo
* **Cadência:** As sprints seguirão o calendário oficial da disciplina (ciclos de aproximadamente 2 a 3 semanas). O planejamento (Sprint Planning) ocorrerá na primeira segunda-feira da sprint e o fechamento (Review e Retrospectiva) na última sexta-feira do ciclo.
  
* **Cerimônias:** 
  * **Planning (Síncrona - 1h):** No início da sprint para definir o Sprint Goal e refinar o Sprint Backlog.
  * **Daily (Assíncrona - 15 min):** Realizada diariamente via grupo do WhatsApp até as 12h, respondendo: O que fiz ontem? O que farei hoje? Há algum impedimento?
  * **Review e Retrospectiva (Síncrona - 1h):** Realizada no fim da sprint para demonstração do incremento funcional e definição de melhorias do processo.
* **Definição de Pronto (DoD):**
  * [ ] Todos os critérios de aceitação validados.
  * [ ] Código integrado via Pull Request para a branch `main`.
  * [ ] Pipeline de CI verde (testes e build passando).
  * [ ] Revisado e aprovado por pelo menos 1 membro diferente do autor.
* **Papéis:** A equipe é multidisciplinar. Para o andamento do Scrum, definimos:
  * **Product Owner:** [INSERIR NOME] (prioriza o backlog e valida critérios de aceitação).
  * **Scrum Master:** [INSERIR NOME] (garante as cerimônias e a remoção de impedimentos).
  * **Developers:** [INSERIR NOMES] (foco no desenvolvimento fullstack e infraestrutura).
  * *Regra de Revisão:* A validação de código é cruzada. Ninguém aprova o próprio PR.
* **Ferramentas:** WhatsApp (Daily e comunicação rápida), Discord (reuniões síncronas), GitHub Projects (Kanban), GitHub Actions (CI) e JUnit (Testes automatizados).
* **WIP limits:** 
  * **Em progresso:** Máximo de 2 itens.
  * **Em revisão:** Máximo de 2 itens.

## 6. Equipe
* **Pedro Galvão do Amaral Neto** - Matrícula: 20230049153 - Papel: 
* **Pedro de Andrade Cursino** - Matrícula: 20220050043 - Papel: 
* **Raylanna Lara Felix de Araujo** - Matrícula: 20230002630 - Papel: 
* **Viviane Estefani da Silva Santos Lopes** - Matrícula: 20220056717 - Papel: 

## 7. Coorte e Integração
* **Coorte de apresentação:** B
* **Integração com outras disciplinas:** Não se aplica.
