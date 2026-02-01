# TicketPulse

> 🇧🇷 **Para ler a versão em Português, [clique aqui](README.pt-br.md).**

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=flat-square&logo=docker&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Lock-DC382D?style=flat-square&logo=redis&logoColor=white)

TicketPulse is a high-performance ticket sales system designed to demonstrate advanced backend engineering techniques for handling high concurrency, race conditions, and asynchronous processing.

Built with the **Java 25** (LTS) and **Spring Boot 4.0.2** ecosystem, this project addresses the "Double Spending" problem in ticket reservations using distributed locking and event-driven architecture.

## Architecture Overview

The system utilizes a synchronous locking mechanism for data integrity and an asynchronous event bus for non-critical operations.

```mermaid
graph TD
    User((User)) -->|HTTP POST /buy| API[TicketPulse API]
    
    subgraph "Synchronous Processing"
        API -->|1. Acquire Lock (SETNX)| Redis[(Redis)]
        API -->|2. Transaction Commit| DB[(PostgreSQL)]
    end
    
    subgraph "Asynchronous Processing"
        API -->|3. Publish Event| RabbitMQ{RabbitMQ}
        RabbitMQ -->|Route| Queue[Queue: Sales]
        Queue -->|Consume| Consumer[Notification Service]
        Consumer -->|Simulate| Email[Email Sender]
        
        Consumer -.->|Error| DLX{DLX}
        DLX -.-> DLQ[Dead Letter Queue]
    end
```

## Technical Implementation

### Concurrency Control
To prevent multiple users from purchasing the same ticket simultaneously, the system implements a **Distributed Lock** pattern using Redis.
*   **Mechanism:** Uses `RedisTemplate` with `setIfAbsent` (SETNX) to create an atomic lock with a defined Time-To-Live (TTL).
*   **Outcome:** Database transactions are only initiated if the lock is successfully acquired, guaranteeing data consistency under high load.

### Resilience and Reliability
*   **Asynchronous Processing:** Sales transactions are decoupled from notification logic using RabbitMQ, ensuring low latency for the user.
*   **Dead Letter Queue (DLQ):** A resilience pattern is implemented where failed messages are automatically routed to a DLQ after a configured number of retry attempts, preventing data loss.

### Modern Stack Compatibility
*   **Java 25 & Jackson 3:** Addresses compatibility challenges between Spring Boot 4 and the new `tools.jackson` namespace by implementing a custom `MessageConverter`. This ensures seamless JSON serialization/deserialization across the AMQP infrastructure.

## Technology Stack

*   **Language:** Java 25 (LTS)
*   **Framework:** Spring Boot 4.0.2
*   **Database:** PostgreSQL 16
*   **Caching & Locking:** Redis 7
*   **Messaging:** RabbitMQ 3.12
*   **Testing:** JUnit 5, Mockito, Testcontainers
*   **Documentation:** OpenAPI 3 (Swagger)

## Getting Started

### Prerequisites
*   Java 25+
*   Docker & Docker Compose
*   Maven

### Installation and Execution

1.  **Start Infrastructure**
    The project utilizes `spring-boot-docker-compose` for seamless environment setup. Running the application will automatically provision PostgreSQL, Redis, and RabbitMQ containers.

    ```bash
    ./mvnw spring-boot:run
    ```

    Alternatively, infrastructure can be managed manually:
    ```bash
    docker compose up -d
    ```

2.  **API Documentation**
    Access the interactive API documentation via Swagger UI:
    [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Testing Strategy

Integration tests are implemented using **Testcontainers** to ensure reliability against real infrastructure components.

```bash
./mvnw test
```

## Project Structure

```
src/main/java/com/jhonataswillian/ticketpulse
├── controller      # REST Controllers (API Layer)
├── domain          # JPA Entities and Domain Logic
├── dto             # Data Transfer Objects (Java Records)
├── infra           # Infrastructure Configuration (RabbitMQ, OpenAPI)
├── repository      # Data Access Layer
└── service         # Business Logic and Transaction Management
```

## License

This project is licensed under the MIT License.

---

Developed by **Jhonatas Willian**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/jhonataswillian/)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/jhonataswillian)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:jhonatas.willian.dev@gmail.com)
