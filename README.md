# Ecommerce Application

A microservices-based Ecommerce system built with Spring Boot and modern architectural patterns.

## Project Structure
```
ecommerce-app/
├── config-server/         # Centralized configuration
├── gateway-service/       # API Gateway(Plan integrate KeyCloak Authen,Author, Distributed Tracing)
├── service-registry/      # Service Discovery (Eureka)
├── user-service/         # User management service(Auth JWT normal)
├── product-service/      # Product management service
├── order-service/        # Order management service (Kafka Listen to your self pattern async processing)(GRPC comunication)
└── logs/                 # Application logs
```

## Progress

### Completed Features ✅

#### Architecture & Infrastructure
- [x] Microservices Architecture Setup
  - [x] Service Registry (Eureka)
  - [x] API Gateway
  - [ ] Config Server
  - [x] Service-to-Service Communication
  - [x] Event driven using kafka
  - [ ] Observability
  - [ ] Dapr Intergation (Service Comunication, Pub/sub, State Management, Resiliency, Secret, Distributed lock)

#### User Service
- [x] Basic CRUD Operations
- [x] Authentication & Authorization
  - [x] JWT Implementation
  - [x] Role-based Access Control
- [x] Data Mapping
  - [x] ModelMapper Integration
  - [x] Request/Response DTOs
  - [x] Entity Mapping

#### Product Service
- [x] Basic CRUD Operations
- [x] Data Mapping
  - [x] ModelMapper Integration
  - [x] Request/Response DTOs
  - [x] Entity Mapping
- [x] Async Operations
  - [x] Async Configuration
  - [x] Async Update/Delete Operations
  - [x] Transaction Management

### In Progress 🚧

#### Clean Architecture
- [ ] CQRS Pattern
  - [ ] Commands with PostgreSQL + JPA
  - [ ] Queries with MongoDB + Spring Data
  - [ ] CDC using Debezium

#### Vertical Slice Architecture
- [ ] Feature-based Organization
- [ ] Minimal Dependencies
- [ ] Self-contained Features

### Planned Features 📋

#### Cross-Cutting Concerns
- [ ] Distributed Tracing
  - [ ] OpenTelemetry Integration
  - [ ] Jaeger/Zipkin Setup
- [ ] Centralized Logging
  - [ ] ELK Stack Integration
  - [ ] Log Aggregation
- [x] Circuit Breaker Pattern
  - [x] Resilience4j Integration
  - [x] Fallback Mechanisms
  - [x] Timeout Mechanisms
  - [x] Retry Mechanisms

#### Security Enhancements
- [ ] OAuth2 Implementation
- [ ] API Key Management
- [ ] Rate Limiting
- [ ] Request Validation

#### Performance & Scalability
- [ ] Caching Strategy
  - [ ] Redis Integration
  - [ ] Cache Invalidation
- [ ] Message Queue Integration
  - [ ] RabbitMQ/Kafka Setup
  - [ ] Event-driven Architecture

#### Monitoring & Operations
- [x] Health Checks
- [ ] Metrics Collection
- [ ] Alerting System
- [ ] Performance Monitoring

#### Documentation
- [ ] API Documentation
  - [x] OpenAPI/Swagger
  - [x] Postman Collections
- [ ] Architecture Documentation
- [x] Deployment Guide
- [x] Development Guide

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Cloud
- Spring Security
- Spring Data JPA
- PostgreSQL
- MongoDB
- ModelMapper
- JWT

### Infrastructure
- Docker
- Kubernetes (Planned)
- CI/CD (Planned)

## Getting Started

### Prerequisites
- Java 17
- Maven
- Docker
- PostgreSQL
- MongoDB

2. Start the services in order:
   ```bash
   # Start Config Server
   # Start Service Registry
   # Start API Gateway
   # Start Microservices
   ```

## Contributing
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
