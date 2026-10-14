# Currency API 💱

Microsserviço de alta disponibilidade responsável pela consulta e conversão de cotações de moedas em tempo real, desenvolvido com **Spring Boot** e arquitetura de microsserviços.

---

## 🚀 Tecnologias e Ferramentas

* **Linguagem / Framework:** Java | Spring Boot (REST API)
* **Persistência de Dados:** Spring Data JPA | PostgreSQL
* **Migrations:** Flyway
* **Service Discovery & Config:** HashiCorp Consul
* **Resiliência:** Resilience4j (Circuit Breaker / Rate Limiter / Retry / Fallback)
* **Build Tool:** Maven

---

## 🏗️ Arquitetura e Estrutura

```text
br.edu.atitus.currencyapi
├── configs/          # Configurações globais (Consul, Beans, WebClient/RestTemplate)
├── controllers/      # Endpoints REST da aplicação
├── dtos/             # Objetos de Transferência de Dados
├── entities/         # Mapeamentos e entidades de banco de dados
├── repositories/     # Interfaces de acesso a dados (Spring Data JPA)
└── services/         # Regras de negócio e integração com APIs externas

```

### Componentes Principais:

* **`CurrencyController`**: Expõe os endpoints REST de consulta de cotação.
* **`CurrencyService`**: Interface e implementação com suporte a padrões de resiliência e fallback.
* **`CurrencyRepository`**: Interface JPA para acesso e cache local de moedas.
* **External Client**: Módulo de integração com provedores externos de câmbio.

---

## 🔄 Integração Externa & Resiliência

O microsserviço consome uma **API externa de câmbio** para obter cotações em tempo real. Para garantir a tolerância a falhas e alta disponibilidade da aplicação, foram aplicados **Resilience Patterns**:

* **Circuit Breaker:** Interrompe chamadas à API externa se a taxa de falhas exceder o limite, evitando sobrecarga no sistema.
* **Retry Pattern:** Tenta reexecutar automaticamente requisições que falharam por instabilidades temporárias.
* **Fallback Methods:** Retorna respostas padrão ou dados em cache caso a API externa fique indisponível, garantindo degradação graciosa.

---

## 📌 API Endpoints

### Consultar Cotação

Retorna a taxa de conversão entre a moeda de origem (`source`) e a moeda de destino (`target`).

* **URL:** `GET /currencies`
* **Parâmetros de Query:**
* `source` (obrigatório): Código da moeda de origem (ex: `USD`)
* `target` (obrigatório): Código da moeda de destino (ex: `BRL`)



#### Exemplo de Requisição:

```http
GET http://localhost:8100/currencies?source=USD&target=BRL

```

#### Exemplo de Resposta:

```json
{
  "sourceCurrency": "USD",
  "targetCurrency": "BRL",
  "conversionRate": 5.25,
  "environment": "Currency API running in Port: 8100"
}

```

---

## ⚙️ Configuração do Ambiente

A aplicação utiliza o **HashiCorp Consul** para gerenciamento de configurações externalizadas e descoberta de serviços.

### Banco de Dados

| Configuração | Valor |
| --- | --- |
| **SGBD** | PostgreSQL |
| **Database** | `db_currency` |
| **Migrações** | Flyway (executadas automaticamente na inicialização) |

> **Nota:** As credenciais e configurações de conexão com o PostgreSQL e com a API Externa devem estar devidamente cadastradas no Consul.

---

## 🛠️ Como Executar a Aplicação

1. **Inicie os serviços de infraestrutura:**
* Garanta que o **PostgreSQL** esteja rodando com o banco `db_currency` criado.
* Suba o servidor do **HashiCorp Consul**.


2. **Carregue as configurações:**
* Disponibilize as propriedades de configuração do `currency-api` no Consul.


3. **Inicie a aplicação:**
```bash
mvn spring-boot:run

```


*A aplicação subirá por padrão na porta **8100**.*
4. **Teste o endpoint:**
Acesse no navegador ou cliente HTTP:
```text
http://localhost:8100/currencies?source=USD&target=BRL

```
