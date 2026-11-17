# PicPay Backend Challenge — API de Transferências

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-6DB33F?logo=springboot&logoColor=white)
![H2](https://img.shields.io/badge/H2-em%20mem%C3%B3ria-blue)
![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)

Implementação do desafio **PicPay Backend**: uma API REST de transferência de dinheiro entre usuários, construída com **Java 17 + Spring Boot**. O projeto foi desenvolvido para fins de estudo, seguindo as instruções do desafio original do PicPay.

> ⚠️ Projeto educacional, sem fins lucrativos ou promocionais. Não deve ser usado em produção.

---

## Sumário

- [Sobre o desafio](#sobre-o-desafio)
- [Funcionalidades](#funcionalidades)
- [Regras de negócio](#regras-de-negócio)
- [Tecnologias](#tecnologias)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como executar](#como-executar)
- [Endpoints](#endpoints)
- [Modelo de dados](#modelo-de-dados)
- [Configuração](#configuração)
- [Testes](#testes)
- [Limitações e próximos passos](#limitações-e-próximos-passos)
- [Referências](#referências)

---

## Sobre o desafio

O cenário é uma carteira digital em que dois tipos de usuário existem:

| Tipo | Pode enviar dinheiro? | Pode receber? |
| --- | --- | --- |
| `COMMON` (pessoa física) | ✅ Sim | ✅ Sim |
| `MERCHANT` (lojista) | ❌ Não | ✅ Sim |

A API cadastra usuários com saldo inicial, registra transferências entre eles e consulta o serviço autorizador externo antes de concluir a operação.

## Funcionalidades

- Cadastro de usuários (`COMMON` e `MERCHANT`) com documento e e-mail únicos;
- Listagem de todos os usuários cadastrados;
- Transferência de valores entre dois usuários;
- Validação de saldo e de tipo de usuário antes da transferência;
- Consulta a um autorizador externo (Mocky) antes de efetivar a transação;
- Persistência da transação com pagador, recebedor, valor e data/hora;
- Atualização de saldo das duas carteiras;
- Envio de notificações (simuladas no console) para pagador e recebedor;
- Console do H2 habilitado para inspeção do banco em memória.

## Regras de negócio

1. **Lojistas não podem enviar dinheiro** — apenas receber.
2. O pagador precisa ter **saldo suficiente** para o valor transferido.
3. A transferência só é concluída se o **autorizador externo** responder `Autorizado`.
4. O valor é debitado do pagador e creditado no recebedor **após** a autorização.
5. Uma notificação é registrada para cada parte envolvida na transação.

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 17 |
| Framework | Spring Boot 3.2.4 (Web, Data JPA) |
| Banco de dados | H2 (em memória) |
| Persistência | Spring Data JPA / Hibernate |
| Comunicação HTTP | `RestTemplate` |
| Build | Maven Wrapper (`mvnw`) |
| Utilitários | Lombok, Spring Boot DevTools |

## Estrutura do projeto

```text
src/main/java/desafio/backend/picpay
├── PicpayApplication.java          # Ponto de entrada
├── config/
│   └── AppConfig.java              # Bean do RestTemplate
├── controller/
│   ├── TransactionController.java  # POST /transactions
│   └── UserController.java         # GET/POST /users
├── domain/
│   ├── Transaction.java            # Entidade de transferência
│   ├── User.java                   # Entidade de usuário
│   └── UserType.java               # COMMON | MERCHANT
├── dto/
│   ├── TransactionDTO.java
│   └── UserDTO.java
├── repository/
│   ├── TransactionRepository.java
│   └── UserRepository.java
└── service/
    ├── NotificationService.java
    ├── TransactionService.java
    └── UserService.java
```

## Como executar

### Pré-requisitos

- **Java 17** ou superior (`java -version`);
- Maven **não** é obrigatório: o projeto inclui o Maven Wrapper.

### Windows (PowerShell/CMD)

```bash
mvnw.cmd spring-boot:run
```

### Linux/macOS

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

### Console do H2

Acesse `http://localhost:8080/h2-console` com:

| Campo | Valor |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:testdb` |
| Usuário | `user` |
| Senha | `password` |

> O banco é **em memória**: os dados são apagados ao reiniciar a aplicação.

## Endpoints

Base: `http://localhost:8080`

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/users` | Lista todos os usuários |
| `POST` | `/users` | Cadastra um usuário |
| `POST` | `/transactions` | Realiza uma transferência |

### Criar usuário

```http
POST /users
Content-Type: application/json
```

```json
{
  "name": "Rayan Ferraz",
  "document": "12345678900",
  "balance": 1000.00,
  "email": "rayan@email.com",
  "password": "senha123",
  "userType": "COMMON"
}
```

Resposta: `201 Created` com o usuário persistido (incluindo o `id`).

Para um lojista, use `"userType": "MERCHANT"`.

### Listar usuários

```http
GET /users
```

Resposta: `200 OK`

```json
[
  {
    "id": 1,
    "name": "Rayan Ferraz",
    "document": "12345678900",
    "email": "rayan@email.com",
    "password": "senha123",
    "userType": "COMMON",
    "balance": 1000.00
  }
]
```

### Realizar transferência

```http
POST /transactions
Content-Type: application/json
```

```json
{
  "amount": 150.00,
  "payerId": 1,
  "payeeId": 2
}
```

Resposta de sucesso: `201 Created` com a transação criada.

| Situação | Resultado |
| --- | --- |
| Pagador lojista | Erro — lojista não pode transferir |
| Saldo insuficiente | Erro — saldo insuficiente |
| Autorizador nega | Erro — transação não autorizada |
| Usuário inexistente | Erro — usuário não encontrado |

## Modelo de dados

**User (`users`)**

| Campo | Tipo | Observação |
| --- | --- | --- |
| `id` | `Long` | Chave primária |
| `name` | `String` | Nome |
| `document` | `String` | Único |
| `email` | `String` | Único |
| `password` | `String` | Texto puro (ver limitações) |
| `userType` | `UserType` | `COMMON` ou `MERCHANT` |
| `balance` | `BigDecimal` | Saldo disponível |

**Transaction (`transactions`)**

| Campo | Tipo | Observação |
| --- | --- | --- |
| `id` | `Long` | Chave primária |
| `amount` | `BigDecimal` | Valor transferido |
| `payer` | `User` | Muitos-para-um (`payer_id`) |
| `payee` | `User` | Muitos-para-um (`payee_id`) |
| `transactionTime` | `LocalDateTime` | Data/hora da transação |

## Configuração

Arquivo: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=user
spring.datasource.password=password
spring.h2.console.enabled=true
spring.jackson.time-zone=America/Sao_Paulo
```

O autorizador externo está definido em `TransactionService`:

```text
https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc
```

> Esse endpoint é um mock público e pode ficar indisponível. Se a transferência passar a falhar, verifique a URL ou substitua por outro mock.

## Testes

```bash
# Windows
mvnw.cmd test

# Linux/macOS
./mvnw test
```

O projeto contém apenas o teste de contexto gerado pelo Spring Initializr.

## Limitações e próximos passos

- **Senhas em texto puro** — não há hash (BCrypt) nem autenticação/autorização.
- **Sem validação de entrada** — `@Valid`, Bean Validation e tratamento global de exceções (`@ControllerAdvice`) não foram implementados.
- **Notificações apenas no console** — não há integração real com e-mail/SMS.
- **Transação não é atômica** — `createTransaction` não está anotado com `@Transactional`; uma falha no meio do fluxo pode deixar saldos inconsistentes.
- **Dependência de mock externo** — o autorizador pode sair do ar.
- **H2 em memória** — os dados não persistem entre execuções.
- **Sem paginação** na listagem de usuários.
- **Sem controle de concorrência** em transferências simultâneas.

## Referências

- Desafio original: [PicPay/picpay-desafio-backend](https://github.com/PicPay/picpay-desafio-backend)
- Documentação Spring Boot: [spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)

## Autor

Desenvolvido por **Rayan Ferraz** para fins educacionais.

- GitHub: [@rayanferrazz](https://github.com/rayanferrazz)
