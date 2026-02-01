# TicketPulse

> 🇺🇸 **To read the English version, [click here](README.md).**

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=flat-square&logo=docker&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Lock-DC382D?style=flat-square&logo=redis&logoColor=white)

TicketPulse é um sistema de venda de ingressos de alta performance projetado para demonstrar técnicas avançadas de engenharia de backend para lidar com alta concorrência, condições de corrida (race conditions) e processamento assíncrono.

Construído com o ecossistema **Java 25** (LTS) e **Spring Boot 4.0.2**, este projeto aborda o problema de "Gasto Duplo" (Double Spending) em reservas de ingressos utilizando bloqueio distribuído e arquitetura orientada a eventos.

## Visão Geral da Arquitetura

O sistema utiliza um mecanismo de bloqueio síncrono para integridade dos dados e um barramento de eventos assíncrono para operações não críticas.

```mermaid
graph TD
    User((Usuário)) -->|HTTP POST /buy| API[TicketPulse API]
    
    subgraph "Processamento Síncrono"
        API -->|1. Adquirir Lock (SETNX)| Redis[(Redis)]
        API -->|2. Commit da Transação| DB[(PostgreSQL)]
    end
    
    subgraph "Processamento Assíncrono"
        API -->|3. Publicar Evento| RabbitMQ{RabbitMQ}
        RabbitMQ -->|Roteamento| Queue[Fila: Vendas]
        Queue -->|Consumir| Consumer[Serviço de Notificação]
        Consumer -->|Simular| Email[Enviador de E-mail]
        
        Consumer -.->|Erro| DLX{DLX}
        DLX -.-> DLQ[Dead Letter Queue]
    end
```

## Implementação Técnica

### Controle de Concorrência
Para impedir que múltiplos usuários comprem o mesmo ingresso simultaneamente, o sistema implementa um padrão de **Bloqueio Distribuído** (Distributed Lock) usando Redis.
*   **Mecanismo:** Utiliza `RedisTemplate` com `setIfAbsent` (SETNX) para criar um bloqueio atômico com um Tempo de Vida (TTL) definido.
*   **Resultado:** As transações de banco de dados só são iniciadas se o bloqueio for adquirido com sucesso, garantindo consistência dos dados sob alta carga.

### Resiliência e Confiabilidade
*   **Processamento Assíncrono:** As transações de venda são desacopladas da lógica de notificação usando RabbitMQ, garantindo baixa latência para o usuário.
*   **Dead Letter Queue (DLQ):** Um padrão de resiliência é implementado onde mensagens com falha são roteadas automaticamente para uma DLQ após um número configurado de tentativas de reprocessamento, prevenindo perda de dados.

### Compatibilidade com Stack Moderna
*   **Java 25 & Jackson 3:** Aborda desafios de compatibilidade entre o Spring Boot 4 e o novo namespace `tools.jackson` implementando um `MessageConverter` customizado. Isso garante serialização/deserialização JSON perfeita através da infraestrutura AMQP.

## Stack Tecnológica

*   **Linguagem:** Java 25 (LTS)
*   **Framework:** Spring Boot 4.0.2
*   **Banco de Dados:** PostgreSQL 16
*   **Cache & Bloqueio:** Redis 7
*   **Mensageria:** RabbitMQ 3.12
*   **Testes:** JUnit 5, Mockito, Testcontainers
*   **Documentação:** OpenAPI 3 (Swagger)

## Começando

### Pré-requisitos
*   Java 25+
*   Docker & Docker Compose
*   Maven

### Instalação e Execução

1.  **Iniciar Infraestrutura**
    O projeto utiliza `spring-boot-docker-compose` para configuração transparente do ambiente. Rodar a aplicação irá provisionar automaticamente os containers PostgreSQL, Redis e RabbitMQ.

    ```bash
    ./mvnw spring-boot:run
    ```

    Alternativamente, a infraestrutura pode ser gerenciada manualmente:
    ```bash
    docker compose up -d
    ```

2.  **Documentação da API**
    Acesse a documentação interativa da API via Swagger UI:
    [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Estratégia de Testes

Testes de integração são implementados usando **Testcontainers** para garantir confiabilidade contra componentes reais de infraestrutura.

```bash
./mvnw test
```

## Estrutura do Projeto

```
src/main/java/com/jhonataswillian/ticketpulse
├── controller      # Controladores REST (Camada de API)
├── domain          # Entidades JPA e Lógica de Domínio
├── dto             # Objetos de Transferência de Dados (Java Records)
├── infra           # Configuração de Infraestrutura (RabbitMQ, OpenAPI)
├── repository      # Camada de Acesso a Dados
└── service         # Lógica de Negócio e Gerenciamento de Transação
```

## Licença

Este projeto está licenciado sob a Licença MIT.

---

Desenvolvido por **Jhonatas Willian**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/jhonataswillian/)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/jhonataswillian)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:jhonatas.willian.dev@gmail.com)
