# PoeNaLista
Gerenciador de lista de compras sincronizada em tempo real para grupos que moram juntos. Desenvolvido como MVP da disciplina DIM0510 (Processos de Software)

## Equipe
* **Pedro Galvão do Amaral Neto** - Matrícula: 20230049153 - GitHub: [@Ordep-42](https://github.com/Ordep-42) - Papel: Product Owner e Developer
* **Pedro de Andrade Cursino** - Matrícula: 20220050043 - Github: [@pedroac7](https://github.com/pedroac7) - Papel: Developer
* **Raylanna Lara Felix de Araujo** - Matrícula: 20230002630 - GitHub: [@Ray-Lara](https://github.com/Ray-Lara) - Papel: Scrum Master e Developer
* **Viviane Estefani da Silva Santos Lopes** - Matrícula: 20220056717 - GitHub: [@viviestefani](https://github.com/viviestefani) - Papel: Developer

## Informações da Disciplina
* **Disciplina:** DIM0510 - Processos de Software
* **Coorte:** B
* **Integração com outras disciplinas:** Não se aplica.

## Como executar o projeto

### Pré-requisitos
* **JDK 17 ou superior** (ex.: [Eclipse Temurin](https://adoptium.net/)) com `java` no `PATH` (ou `JAVA_HOME` configurado). Confira com `java -version`.
* **Não é preciso instalar o Maven**: o projeto usa o Maven Wrapper (`mvnw`), que baixa a versão certa na primeira execução (requer internet nessa primeira vez).
* **Não é preciso instalar banco de dados**: por padrão a aplicação usa H2 em memória. PostgreSQL só é necessário no perfil `prod` (ver abaixo).

### Subindo a aplicação
Na raiz do repositório:
```bash
./mvnw spring-boot:run      # Linux/macOS (Git Bash no Windows também funciona)
mvnw.cmd spring-boot:run    # Windows (PowerShell/CMD)
```

Quando aparecer no terminal a mensagem `Started PoeNaListaApplication`, acesse **[http://localhost:8080](http://localhost:8080)**. Para parar, use `Ctrl+C`.

### O que esperar na tela
Ao abrir `http://localhost:8080` você cai direto na **lista de compras** — não há login por enquanto:

* **Cabeçalho** verde com a marca *PoeNaLista*, seguido do título da lista (**Lista de Compras**) e da casa (**Minha Casa**).
* **Adicionar item**: formulário com *Nome*, *Quantidade* (padrão 1) e *Categoria* (Hortifrúti, Padaria, Frios, Laticínios, Carnes, Mercearia, Bebidas, Limpeza, Higiene, Outros). Ao clicar em **Adicionar**, o item entra na lista e aparece uma mensagem de confirmação; campos inválidos (nome vazio, quantidade menor que 1, categoria não escolhida) são apontados no próprio formulário.
* **Itens**: contador e a lista dos itens atuais, cada um com nome, categoria e os controles:
  * **−** / **+** diminuem ou aumentam a quantidade em 1 (o **−** fica desabilitado em 1);
  * campo numérico + **Atualizar** define a quantidade diretamente (mínimo 1);
  * **Remover** tira o item da lista (após confirmação).
* Com a lista vazia, aparece o aviso *"A lista está vazia"*.

> **Lista padrão (provisória):** enquanto login (#1) e criação de casas (#2) não estão integrados, a aplicação cria automaticamente na inicialização um usuário (`morador.padrao@poenalista.local`, sem senha utilizável), uma casa *Minha Casa* e a lista *Lista de Compras* — ver `shoppinglist/DefaultShoppingListService.java`. Todos que acessam a página usam essa mesma lista.

### Banco de dados
Por padrão a aplicação usa um banco **H2 em memória** (os dados somem ao reiniciar); o console do banco fica em [http://localhost:8080/h2-console](http://localhost:8080/h2-console) com a JDBC URL `jdbc:h2:mem:poenalista`, usuário `sa` e senha vazia.

Para usar **PostgreSQL**, ative o perfil `prod` e informe a conexão por variáveis de ambiente (`DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`):
```bash
SPRING_PROFILES_ACTIVE=prod DATABASE_URL=jdbc:postgresql://localhost:5432/poenalista ./mvnw spring-boot:run
```

### Testes
```bash
./mvnw test      # Linux/macOS
mvnw.cmd test    # Windows
```
Os testes usam o H2 em memória e não precisam de nenhum serviço externo. Os testes de aceitação da história #3 (adicionar, alterar quantidade e remover itens) estão em `src/test/java/br/ufrn/poenalista/web/GerenciarItensDaListaTest.java`.


### Estrutura do projeto
Os pacotes são organizados por funcionalidade, para que cada história do quadro fique concentrada em um pacote:

```
src/main/java/br/ufrn/poenalista/
├── PoeNaListaApplication.java   # ponto de entrada
├── user/          # User — conta e entrada no app (#1)
├── house/         # House, Resident, Invite — casas e convite de moradores (#2, #6)
├── shoppinglist/  # ShoppingList — listas de compras de uma casa
├── item/          # Item, Category — itens da lista (#3, #5)
├── config/        # configurações transversais (ex.: WebSocket p/ tempo real, #4)
└── web/           # páginas gerais (início)
src/main/resources/
├── application.properties        # perfil padrão (H2)
├── application-prod.properties   # perfil prod (PostgreSQL)
├── db/migration/                 # migrations Flyway (V1__..., V2__...)
├── templates/                    # páginas Thymeleaf (fragments/layout.html = cabeçalho comum)
└── static/css/                   # estilos
```

Cada pacote de funcionalidade segue a convenção: entidade JPA, `Repository`, `Service` e `Controller`, com as páginas em `templates/<pacote>/`.

O banco é criado **pelas migrations Flyway**, que rodam sozinhas ao subir o app; o Hibernate só valida que as entidades batem com as tabelas. Tabelas, diagrama e regras para criar novas migrations estão em [docs/modelo-de-dados.md](docs/modelo-de-dados.md). O CI (GitHub Actions) roda `./mvnw verify` em todo PR para `main`.

## Links
* [Proposta do Projeto](docs/proposta.md)
* [Modelo de Dados](docs/modelo-de-dados.md)
* [Quadro Kanban no GitHub Projects](https://github.com/users/Ordep-42/projects/10)
* [Vídeo de Apresentação da Sprint 0](https://drive.google.com/file/d/13UqQW9wg5qu5rqee_HHRxQ_MpwveISmf/view?usp=sharing)


## Checklist do projeto
### Sprint 0
- [X] Repositório público + README completo
- [X] docs/proposta.md (≤3 pág.)
- [X] GitHub Projects com ≥5 itens, ≥3 estimados
- [X] Coorte declarada (A=presencial / B=online)
- [X] Integração com outra disciplina declarada (se houver)
- [X] Vídeo 5 min

### Sprint 1
- [ ] Incremento funcional em main
- [ ] Kanban com WIP limits configurados
- [ ] Evidência de prática XP
- [ ] docs/retrospectiva-01.md com ações
- [ ] Vídeo 5 min

### Sprint 2
- [ ] CI verde (build + testes + lint) com gate em PR
- [ ] Dockerfile + docker-compose.yml
- [ ] docs/dora.md com as 5 métricas e método
- [ ] Segundo incremento
- [ ] Vídeo 5 min

### Sprint 3
- [ ] docs/vsm.md com tempos medidos
- [ ] ≥2 gargalos com dados
- [ ] 1–2 melhorias com métrica-alvo
- [ ] ≥3 PRs com revisão substantiva
- [ ] Evolução DORA S2→S3
- [ ] Vídeo 5 min

### Entrega Final
- [ ] MVP funcional, CI verde, README completo e licença
- [ ] docs/relatorio-final.md (≤6 pág.)
- [ ] docs/melhoria-de-processo.md (≤4 pág.)
- [ ] docs/topologia.md (≤1 pág.)
- [ ] docs/uso-de-ia.md
- [ ] Vídeo 10 min
- [ ] Apresentação ao vivo
