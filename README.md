# WEX Transaction Service

A Java/Spring Boot application for managing financial transactions, built with a full Hexagonal Architecture. It features a clean separation of concerns with explicit ports and adapters, and handles currency conversion using historical exchange rates from an external API.

---

## Features
- Create, retrieve, and list transactions
- Convert transaction amounts to a target currency using historical rates
- Pagination and default sorting on listing
- Unit-tested service layer
- Clean separation: domain, application, and infrastructure layers

---

## Requirements
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Docker (optional, for containerized runs)

---

## Getting Started

### 1. Clone the Repository
```sh
git clone https://github.com/m3iller/wex-transaction.git
cd wex-transaction
```

### 2. Configure Environment
Create a `.env` file in the project root by copying the example below. This file securely stores your database credentials.

```env
# .env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=transaction_db
POSTGRES_HOST=postgres
SPRING_PROFILES_ACTIVE=dev

# A secret key for authenticating API requests
API_KEY=your-secret-api-key
```

### 3. Build and Run with Docker Compose
This single command builds the application, starts the PostgreSQL database, and runs the service.
```sh
docker-compose up --build
```
The application will be available at `http://localhost:8080`.

---

## Authentication

All API endpoints under `/transactions` are protected and require an API key.

### Providing the API Key
You must include the API key in the `X-API-KEY` header of your requests.

#### Using `curl`
```sh
curl -H "X-API-KEY: your-secret-api-key" http://localhost:8080/transactions/1
```

#### Using Swagger UI
1.  Navigate to the [Swagger UI](http://localhost:8080/swagger-ui.html).
2.  Click the **Authorize** button at the top right.
3.  Enter your API key from the `.env` file into the `apiKeyAuth` value field and click "Authorize".
4.  All subsequent requests made through the UI will now include the correct authentication header.

---

## API Endpoints

### Create Transaction
```
POST /transactions
Content-Type: application/json
{
  "description": "Test Transaction",
  "transactionDate": "2025-06-16",
  "amount": 100.00
}
```

### Get Transaction by ID
```
GET /transactions/{id}
```

### List Transactions (paginated, sorted)
```
GET /transactions?page=0&size=10&sort=transactionDate,desc
```
- Default sort: `transactionDate` descending

### Convert Transaction Currency
```
GET /transactions/{id}/convert/{locale}
```
- Example: `/transactions/1/convert/pt-BR`
- Converts the transaction amount to the currency for the given locale (e.g., "pt-BR" for Brazilian Real)

---

## Running Tests
```sh
mvn test
```

---

## Project Structure
- `domain/` - Core business logic, models, and ports (interfaces).
- `application/` - Use case implementations (services that orchestrate domain logic).
- `infrastructure/` - External-facing adapters (e.g., REST controllers, database repositories, external API clients).
- `test/` - Unit and integration tests

---

## Notes
- Sorting parameters must be property names, e.g. `sort=transactionDate,desc`
- All monetary values use `BigDecimal`
- Currency conversion uses a live external API for exchange rates.
- Integration tests for external clients use Spring's test slicing (`@SpringBootTest(classes=...)`) to remain fast and isolated from the database context.

---

## License
MIT License
