# PoeNaLista
Gerenciador de lista de compras sincronizada em tempo real para grupos que moram juntos. Desenvolvido como MVP da disciplina DIM0510 (Processos de Software)

## Equipe
* **Pedro Galvão do Amaral Neto** - Matrícula: 20230049153 - GitHub: [@Ordep-42](https://github.com/Ordep-42) - Papel:
* **Pedro de Andrade Cursino** - Matrícula: 20220050043 - Github: [@pedroac7](https://github.com/pedroac7) - Papel:
* **Raylanna Lara Felix de Araujo** - Matrícula: 20230002630 - GitHub: [@Ray-Lara](https://github.com/Ray-Lara) - Papel:
* **Viviane Estefani da Silva Santos Lopes** - Matrícula: 20220056717 - GitHub: [@viviestefani](https://github.com/viviestefani) - Papel: 

## Informações da Disciplina
* **Disciplina:** DIM0510 - Processos de Software
* **Coorte:** B
* **Integração com outras disciplinas:** Não se aplica.

## Como rodar

### Pré-requisitos
* **JDK 17** (ex.: [Eclipse Temurin](https://adoptium.net/)). Não é preciso instalar o Maven: o projeto usa o Maven Wrapper (`mvnw`).

### Comandos
```bash
./mvnw spring-boot:run      # Linux/macOS
mvnw.cmd spring-boot:run    # Windows
```

Acesse [http://localhost:8080](http://localhost:8080). Por padrão a aplicação usa um banco **H2 em memória** (os dados somem ao reiniciar); o console do banco fica em [http://localhost:8080/h2-console](http://localhost:8080/h2-console) com a JDBC URL `jdbc:h2:mem:poenalista`, usuário `sa` e senha vazia.

Para rodar os testes: `./mvnw test`.

Para usar **PostgreSQL**, ative o perfil `prod` e informe a conexão por variáveis de ambiente (`DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`):
```bash
SPRING_PROFILES_ACTIVE=prod DATABASE_URL=jdbc:postgresql://localhost:5432/poenalista ./mvnw spring-boot:run
```

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
