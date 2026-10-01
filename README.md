# Currency API

Microsserviço responsável pela consulta de cotações de moedas, desenvolvido com **Spring Boot** seguindo o padrão arquitetural utilizado no `product-api`.

## Tecnologias

* Java / Spring Boot
* Spring Data JPA
* PostgreSQL
* Flyway
* HashiCorp Consul
* Maven
* REST API

## Arquitetura

```text
br.edu.atitus.currencyapi
├── controllers
├── dtos
├── entities
├── repositories
└── services
```

Principais componentes:

* `CurrencyEntity`
* `CurrencyRepository`
* `CurrencyResponse`
* `CurrencyService`
* `CurrencyServiceJpa`
* `CurrencyController`

## API

### Consultar cotação

```http
GET /currencies?source=USD&target=BRL
```

Exemplo de resposta:

```json
{
  "sourceCurrency": "USD",
  "targetCurrency": "BRL",
  "conversionRate": 5.25,
  "environment": "Currency API running in Port: 8100"
}
```

## Configuração

A aplicação utiliza **HashiCorp Consul** para configuração externalizada.

A porta obrigatória é:

```text
8100
```

As configurações de banco de dados e demais propriedades necessárias devem estar disponíveis no Consul.

## Banco de Dados

| Configuração | Valor         |
| ------------ | ------------- |
| SGBD         | PostgreSQL    |
| Banco        | `db_currency` |
| Migrações    | Flyway        |

Os scripts de criação e carga inicial devem estar configurados no Flyway.

## Execução

Para executar o projeto:

1. Inicie o HashiCorp Consul.
2. Disponibilize as configurações do `currency-api` no Consul.
3. Garanta que o PostgreSQL esteja disponível.
4. Execute o `currency-api`.
5. Acesse:

```text
http://localhost:8100/currencies?source=USD&target=BRL
```

## Repositório

O repositório deve conter:

```text
/
├── product-api/
├── currency-api/
└── script-consul.bat
```

Além dos microsserviços, devem estar presentes os arquivos de configuração e scripts de migração necessários para execução do ambiente.

> **Importante:** os nomes de packages, classes, métodos, atributos e endpoints devem seguir exatamente os contratos definidos na atividade.
