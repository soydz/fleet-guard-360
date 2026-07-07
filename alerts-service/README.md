# Alert Management Microservice

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![GraphQL](https://img.shields.io/badge/GraphQL-Enabled-e10098.svg)](https://graphql.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)](https://www.postgresql.org/)

A robust microservice for managing alert types and priority levels in the FleetGuard360 ecosystem. This service provides comprehensive CRUD operations for alert types and priority levels through a GraphQL API with a simplified and efficient architecture.

## 🏗️ Architecture Overview

This microservice follows **Clean Architecture** principles with clear separation of concerns:

```
src/main/java/com/fleetguard360/alert_management/
├── presentation/          # API layer (Controllers, DTOs, Exception Handlers)
│   ├── controller/        # GraphQL Controllers
│   ├── DTO/              # Data Transfer Objects (organized by domain)
│   │   ├── tipoalerta/   # Alert Type DTOs
│   │   └── nivelprioridad/ # Priority Level DTOs
│   └── advice/           # Global Exception Handler
├── service/              # Business Logic layer
│   ├── interfaces/       # Service Contracts
│   ├── implementation/   # Service Implementations
│   └── exception/        # Custom Business Exceptions
├── persistence/          # Data Access layer
│   ├── entity/          # JPA Entities
│   └── repository/      # JPA Repositories
└── configuration/        # Configuration layer
    └── mapper/          # MapStruct Mappers
```

## 🚀 Features

### Core Entities

#### 1. **TipoAlerta (Alert Type)** - Complete Alert Configuration
- **Self-contained alert definition** with all necessary configuration
- **Priority Level Integration**: Each alert type has an associated priority level
- **Responsible Area**: Defines which area should handle the alert (conductor, mechanic, technical support, logistics operator, security)
- **Simplified Model**: No separate configuration needed - everything is defined in the alert type

#### 2. **NivelPrioridad (Priority Level)** - Simple Priority Catalog
- **Streamlined priority levels** for alerts
- **Clean design**: Simple name-based priority system
- **Reusable**: Multiple alert types can share the same priority level

### Key Capabilities
- ✅ **Complete CRUD Operations** for both entities via GraphQL API
- ✅ **Comprehensive Data Validation** with Spring Boot Validation
- ✅ **Business Logic Validation** (duplicate prevention, referential integrity)
- ✅ **Global Exception Handling** with structured error responses
- ✅ **Automatic Mapping** between entities and DTOs using MapStruct
- ✅ **Transaction Management** with proper isolation levels
- ✅ **Comprehensive Logging** for monitoring and debugging
- ✅ **Database Relationships** with proper JPA configurations

## 📊 Data Model

### Entity Relationships
```
TipoAlerta (Alert Type) - Complete Alert Configuration
├── id: Integer (PK)
├── nombre: String (unique)
├── descripcion: String
├── nivelPrioridad: NivelPrioridad (ManyToOne)
└── tipoEncargado: TipoEncargado (enum)

NivelPrioridad (Priority Level) - Simple Priority Catalog
├── id: Integer (PK)
└── nombre: String (unique)

TipoEncargado (Enum) - Responsible Areas
├── CONDUCTOR
├── MECANICO
├── SOPORTE_TECNICO
├── OPERADOR_LOGISTICA
└── SEGURIDAD
```

## 🛠️ Technology Stack

- **Java 21** - Programming language
- **Spring Boot 3.5.5** - Application framework
- **Spring Data JPA** - Data persistence layer
- **GraphQL** - API query language
- **MapStruct** - Bean mapping framework
- **PostgreSQL** - Database (recommended)
- **Lombok** - Boilerplate code reduction
- **SLF4J + Logback** - Logging framework

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- PostgreSQL database
- Gradle 8.x+ or use included wrapper

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd alert-management
   ```

2. **Configure database**
   ```properties
   # application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/alert_management
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access GraphQL Playground**
   ```
   http://localhost:8080/graphiql
   ```

## 📝 API Usage

### GraphQL Endpoints

#### Creating a Complete Alert Type
```graphql
mutation {
  createTipoAlerta(input: {
    nombre: "Engine Failure"
    descripcion: "Critical engine system failure alert"
    nivelPrioridadId: 1
    tipoEncargado: MECANICO
  }) {
    id
    nombre
    descripcion
    nivelPrioridad {
      id
      nombre
    }
    tipoEncargado
  }
}
```

#### Querying Alert Types with Complete Information
```graphql
query {
  tipoAlertas {
    id
    nombre
    descripcion
    nivelPrioridad {
      nombre
    }
    tipoEncargado
  }
}
```

#### Creating Priority Levels
```graphql
mutation {
  createNivelPrioridad(input: {
    nombre: "Critical"
  }) {
    id
    nombre
  }
}
```

## 🏗️ Project Structure

### DTOs Organization
```
DTO/
├── tipoalerta/
│   ├── TipoAlertaCreateRequest.java
│   ├── TipoAlertaUpdateRequest.java
│   └── TipoAlertaResponse.java
└── nivelprioridad/
    ├── NivelPrioridadCreateRequest.java
    ├── NivelPrioridadUpdateRequest.java
    └── NivelPrioridadResponse.java
```

### Service Layer
- **Interfaces**: Define service contracts for TipoAlerta and NivelPrioridad
- **Implementations**: Contain business logic with comprehensive validation
- **Exceptions**: Custom business exceptions for specific error scenarios

### Validation Features
- **Field Validation**: Using Spring Boot Validation annotations
- **Business Logic Validation**: Duplicate prevention, relationship validation
- **Error Handling**: Structured error responses with meaningful messages

## 🧪 Development

### Building the Project
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Generating Documentation
```bash
./gradlew javadoc
```

## 📈 Business Rules

### Alert Type Management
- ✅ Alert type names must be unique (case-insensitive)
- ✅ Each alert type must have an associated priority level
- ✅ Each alert type must specify a responsible area (enum)
- ✅ Alert types are self-contained - no additional configuration needed
- ✅ Cannot delete alert types if they would cause referential integrity issues

### Priority Level Management
- ✅ Priority level names must be unique (case-insensitive)
- ✅ Cannot delete priority levels referenced by alert types
- ✅ Simple, reusable priority catalog

## 🔧 Configuration

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/alert_management
spring.datasource.username=${DB_USERNAME:alert_user}
spring.datasource.password=${DB_PASSWORD:alert_password}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# GraphQL Configuration
spring.graphql.graphiql.enabled=true
spring.graphql.graphiql.path=/graphiql

# Logging Configuration
logging.level.com.fleetguard360.alert_management=DEBUG
logging.level.org.springframework.web=INFO
```

## 📋 TODO / Roadmap

- [ ] Add integration tests
- [ ] Implement caching layer (Redis)
- [ ] Add audit logging
- [ ] Implement event publishing (Kafka/RabbitMQ)
- [ ] Add API rate limiting
- [ ] Create database migration scripts
- [ ] Add monitoring and health checks
- [ ] Implement bulk operations
- [ ] Add search and filtering capabilities

## 💡 Architecture Benefits

### Simplified Model
- **No separate configuration entity** - everything is defined in TipoAlerta
- **Reduced complexity** - fewer entities and relationships to manage
- **Self-contained alerts** - each alert type has all necessary information

### Enum-based Responsible Areas
- **Type safety** - compile-time validation of responsible areas
- **Performance** - no additional database lookups needed
- **Consistency** - standardized set of responsible areas across the system

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions, please contact the FleetGuard360 development team or create an issue in the repository.

---

**FleetGuard360 Alert Management Microservice** - Simplified, efficient alert management.
