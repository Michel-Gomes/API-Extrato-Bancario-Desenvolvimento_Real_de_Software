# API de Extrato Bancário

API REST para registrar transações e analisar um extrato bancário: saldo, maiores despesas, categoria com mais gastos, contagem mensal e exportação em CSV.

Projeto criado como exercício do capítulo 2 do livro **Desenvolvimento Real de Software** (Raoul-Gabriel Urma e Richard Warburton). No livro o exemplo é feito em Java puro lendo um arquivo CSV. Aqui ele foi implementado como uma API com Spring Boot e PostgreSQL, aplicando os princípios do capítulo.

## Tecnologias

- Java 25
- Spring Boot 4.1.1 (Web MVC e Data JPA)
- PostgreSQL 18
- Swagger / OpenAPI (springdoc)
- JUnit 5 e Mockito
- Docker e Docker Compose
- Lombok e Maven

## Conceitos aplicados

- **Princípio da Responsabilidade Única (SRP):** o serviço que fazia tudo foi dividido em `ExtratoService` (registrar e exportar) e `EstatisticasExtratoService` (cálculos).
- **Baixo acoplamento:** o `ExtratoService` depende da interface `ExtratoExporter`, e não do formato CSV. Para exportar em outro formato, basta criar uma nova implementação.
- **Testes unitários:** as estatísticas e o exportador são testados sem banco de dados.

## Endpoints

Todos os caminhos começam com `/api/extrato`.

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/` | Registra uma transação |
| `GET` | `/resultado-liquido` | Soma das transações, classificada como Lucro ou Prejuízo |
| `GET` | `/transacoes-mes?ano=2026&mes=9` | Quantidade de transações do mês |
| `GET` | `/top-despesas` | As 10 maiores despesas |
| `GET` | `/categoria-mais-gasta` | Categoria com maior total de despesas (204 se não houver despesas) |
| `GET` | `/csv` | Baixa o extrato completo em CSV |

Exemplo de corpo para `POST /api/extrato`. Despesas usam valor **negativo** e receitas usam valor **positivo**:

```json
{
  "nome": "Supermercado",
  "descricao": "Compras do mês",
  "categoria": "Mercado",
  "valor": -250.90,
  "data": "2026-09-15T10:30:00"
}
```

## Como rodar

### Pré-requisitos

- Docker e Docker Compose
- Para rodar fora do Docker: JDK 25 e um PostgreSQL disponível

### 1. Configurar as variáveis de ambiente

Copie o arquivo de exemplo e preencha os valores:

```bash
cp .env.example .env
```

O `.env` precisa ter estas variáveis:

```
DB_NAME=extratobancario_db
DB_USERNAME=postgres
DB_PASSWORD=sua-senha

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/extratobancario_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=sua-senha
```

- O primeiro grupo é usado pelo Docker Compose para criar o banco.
- O segundo grupo é usado pela aplicação quando ela roda fora do Docker (por exemplo, pelo IntelliJ).
- Os valores dos dois grupos devem ser iguais. O arquivo `.env` não vai para o Git.

### 2. Subir com Docker Compose

```bash
docker compose up -d --build
```

Isso sobe o PostgreSQL e a API. A API só inicia depois que o banco estiver pronto.

| Serviço | Endereço |
|---|---|
| API | http://localhost:8081 |
| Swagger | http://localhost:8081/swagger-ui/index.html |
| PostgreSQL | `localhost:5433` |

Para parar:

```bash
docker compose down        # mantém os dados
docker compose down -v     # apaga também os dados do banco
```

### Rodar sem Docker para a API

Suba somente o banco e execute a aplicação pela IDE ou pelo Maven:

```bash
docker compose up -d db
./mvnw spring-boot:run
```

## Testes

```bash
./mvnw test
```

Os testes unitários não precisam de banco. O `ExtratobancarioApplicationTests` sobe o contexto completo e precisa do PostgreSQL rodando com as variáveis do `.env`.

## Estrutura

```
src/main/java/br/com/extratobancario/
├── configurations/   configuração do Swagger
├── controller/       endpoints REST
├── dto/              objetos de entrada e saída
├── entities/         entidade Transacao
├── export/           ExtratoExporter e ExtratoCsvExporter
├── repository/       consultas ao banco
└── services/         ExtratoService e EstatisticasExtratoService
```

## Autor

Michel Gomes
