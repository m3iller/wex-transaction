# WEX Transaction Service

A hexagonal-architecture Java/Spring Boot application for managing financial transactions, with currency conversion using historical exchange rates.

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
- PostgreSQL (see `application.properties`)
- Docker (optional, for containerized runs)

---

## Getting Started

### 1. Clone the Repository
```sh
git clone https://github.com/m3iller/wex-transaction.git
cd wex-transaction
```

### 2. Configure the Database
Edit `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/transaction_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Build the Project
```sh
mvn clean package
```

### 4. Run Locally
```sh
java -jar target/*.jar
```

---

## Running with Docker

### 1. Build the Docker image
```sh
docker build -t wex-transaction .
```

### 2. Run the Docker container
```sh
docker run -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/transaction_db \
           -e SPRING_DATASOURCE_USERNAME=postgres \
           -e SPRING_DATASOURCE_PASSWORD=postgres \
           -p 8080:8080 wex-transaction
```
> Use `host.docker.internal` for DB host if your database is running on your Mac/Windows host. On Linux, use your host IP.

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
- `domain/` - Business logic and models
- `application/` - Service layer and use cases
- `infrastructure/` - Adapters: database, API clients, controllers
- `test/` - Unit and integration tests

---

## Notes
- Sorting parameters must be property names, e.g. `sort=transactionDate,desc`
- All monetary values use `BigDecimal`
- Currency conversion uses mocked or real external API for exchange rates

---

## License
MIT License
