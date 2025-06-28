# Intelligent Antifraud System

A Spring Boot-based intelligent antifraud system that provides real-time transaction risk assessment and decision-making capabilities. The system uses multiple risk factors including IP reputation, transaction amounts, and device fingerprinting to calculate risk scores and make automated decisions.

## 🚀 Features

- **Real-time Risk Assessment**: Calculate risk scores for transactions using multiple factors
- **Multi-tenant Support**: Evaluate transactions per tenant with isolated configurations
- **Intelligent Decision Making**: Automated decisions (APPROVE, REVIEW, BLOCK) based on risk scores
- **Event-driven Architecture**: Asynchronous processing using RabbitMQ
- **Caching Layer**: Redis-based caching for performance optimization
- **Persistent Storage**: PostgreSQL for transaction and configuration data

## 🏗️ Architecture

The system follows a clean architecture pattern with the following layers:

```
┌─────────────────────────────────────────────────────────────┐
│                    Adapter Layer                            │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │   Controller    │  │   Publisher     │  │  Consumer   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                   Application Layer                         │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │  CacheService   │  │TenantConfigSvc  │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                     Domain Layer                            │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │ RiskScoringSvc  │  │ DecisionService │  │   Models    │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                 Infrastructure Layer                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │   PostgreSQL    │  │      Redis      │  │  RabbitMQ   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Data JPA** - Database operations
- **Spring Data Redis** - Caching
- **Spring AMQP** - Message queuing
- **PostgreSQL** - Primary database
- **Redis** - Caching and session management
- **RabbitMQ** - Message broker
- **Lombok** - Code generation
- **Maven** - Build tool

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd intelligent-antifraud
```

### 2. Start Infrastructure Services

```bash
docker-compose up -d
```

This will start:
- **PostgreSQL** on port 5433
- **Redis** on port 6400
- **RabbitMQ** on port 5673 (Management UI on 15673)

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📡 API Endpoints

### Evaluate Transaction

**POST** `/evaluate/{tenantId}`

Evaluates a transaction and returns a risk assessment decision.

**Request Body:**
```json
{
  "transactionId": "txn_123456",
  "userId": "user_789",
  "amount": 150000, // in cents
  "ip": "192.168.1.100",
  "fingerprint": "device_fp_example",
  "timestamp": "2024-01-15T10:30:00"
}
```

**Response:**
```json
{
  "decision": "APPROVE",
  "riskScore": 25
}
```

**Possible Decisions:**
- `APPROVE` - Transaction is approved
- `REVIEW` - Transaction requires manual review
- `BLOCK` - Transaction is blocked

## 🔧 Configuration

The application configuration is defined in `application.properties`:

```properties
# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6400

# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/antifraud
spring.datasource.username=postgres
spring.datasource.password=postgres

# RabbitMQ Configuration
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5673
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## 🧪 Testing

Run the test suite:

```bash
./mvnw test
```

The project includes comprehensive tests for:
- Risk scoring services
- Cache operations
- Controller endpoints
- Domain services

## 📊 Risk Scoring Algorithm

The system calculates risk scores based on multiple factors:

1. **IP Reputation** (40 points) - Recent risk associated with IP address
2. **Transaction Amount** - Risk based on amount thresholds and patterns
3. **Device Fingerprint** - Risk associated with device characteristics

The final score is capped at 100 points, and decisions are made based on score ranges:
- 0-30: APPROVE
- 31-70: REVIEW
- 71-100: BLOCK

## 🏗️ Project Structure

```
src/
├── main/java/com/jorgehabib/intelligent_antifraud/
│   ├── adapter/                    # REST controllers and messaging
│   │   └── AntifraudController.java
│   ├── application/                # Application services
│   │   └── service/
│   │       ├── CacheService.java
│   │       └── TenantConfigService.java
│   ├── config/                     # Configuration classes
│   │   ├── RabbitConfig.java
│   │   └── RedisConfig.java
│   ├── domain/                     # Business logic and models
│   │   ├── model/
│   │   │   ├── TenantConfig.java
│   │   │   ├── Transaction.java
│   │   │   └── TransactionResponse.java
│   │   └── service/
│   │       ├── DecisionService.java
│   │       ├── FingerprintRiskService.java
│   │       ├── RiskScoringService.java
│   │       └── TransactionAmountRiskService.java
│   └── infrastructure/             # External integrations
│       ├── db/
│       │   ├── postgres/
│       │   └── redis/
│       └── messaging/
│           ├── TransactionEventConsumer.java
│           ├── TransactionEventPublisher.java
│           └── TransactionFeedbackQueuePublisher.java
└── test/                           # Test classes
```

## 🔄 Event Flow

1. **Transaction Request** → Controller receives transaction data
2. **Risk Assessment** → RiskScoringService calculates risk score
3. **Decision Making** → DecisionService determines action
4. **Event Publishing** → Transaction event published to RabbitMQ
5. **Response** → Decision and score returned to client

## 🐳 Docker Support

The application can be containerized using the provided Docker Compose configuration. The infrastructure services (PostgreSQL, Redis, RabbitMQ) are already configured for containerized deployment.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Jorge Habib** - *Initial work*

## 🆘 Support

For support and questions, please open an issue in the repository or contact the development team.
