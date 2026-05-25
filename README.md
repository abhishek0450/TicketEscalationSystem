# Enterprise Support Ticket Escalation System - Phase 1 Scaffold

This project serves as the complete Maven-based backend scaffold for an enterprise support ticket escalation system. This is a foundational project scaffold (Phase 1) containing compilation-ready directories, configuration parameters, and skeleton patterns.

> [!NOTE]
> **Phase 1 Foundation Only**: Business logic, API controllers implementations, database queries, and custom handlers are not yet fully implemented and will be developed in future phases.

---

## Technical Stack

| Component | Technology | Version | Description |
| :--- | :--- | :--- | :--- |
| Language | Java | 21 | Modern LTS Java Platform |
| Core Framework | Spring Boot | 3.3.x | Enterprise application stack |
| Data Layer | Spring Data JPA / Hibernate | 3.3.x | Relational mapping layer |
| Database | MySQL | 8.0 | High-performance relational database |
| Security | Spring Security & JJWT | 0.12.x | Token-based security and password hashing |
| API Docs | Springdoc OpenAPI (Swagger) | 2.6.x | Interactive API endpoint interface |
| Utility | Lombok | 1.18.x | Boilerplate reducer annotations |
| Build Tool | Apache Maven | 3.9+ | Dependency and packaging automation |

---

## Prerequisites

To run this project locally, ensure you have:
1. **Java Development Kit (JDK) 21** installed.
2. **Apache Maven 3.9+** (or use the included Maven wrapper `mvnw`).
3. **MySQL 8.0** local server or Docker container running.

---

## Project Structure Tree

```text
src/main/java/com/ticketing/
├── TicketingSystemApplication.java
├── config/
│   ├── AppConfig.java
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── AsyncConfig.java
├── controller/
│   ├── AuthController.java
│   ├── TicketController.java
│   ├── CommentController.java
│   ├── EscalationController.java
│   ├── DashboardController.java
│   └── AdminController.java
├── service/
│   ├── AuthService.java
│   ├── TicketService.java
│   ├── CommentService.java
│   ├── EscalationService.java
│   ├── DashboardService.java
│   ├── EmailService.java
│   └── impl/
│       ├── AuthServiceImpl.java
│       ├── TicketServiceImpl.java
│       ├── CommentServiceImpl.java
│       ├── EscalationServiceImpl.java
│       ├── DashboardServiceImpl.java
│       └── EmailServiceImpl.java
├── repository/
│   ├── UserRepository.java
│   ├── TicketRepository.java
│   ├── CommentRepository.java
│   ├── EscalationLogRepository.java
│   ├── CategoryRepository.java
│   └── SlaConfigRepository.java
├── entity/
│   ├── User.java
│   ├── Role.java
│   ├── Ticket.java
│   ├── TicketComment.java
│   ├── TicketHistory.java
│   ├── EscalationLog.java
│   ├── Category.java
│   ├── SlaConfig.java
│   └── Notification.java
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── CreateTicketRequest.java
│   │   └── AddCommentRequest.java
│   └── response/
│       ├── AuthResponse.java
│       ├── TicketResponse.java
│       ├── CommentResponse.java
│       └── DashboardStatsResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── TicketNotFoundException.java
│   ├── UserNotFoundException.java
│   ├── UnauthorizedAccessException.java
│   └── InvalidStatusTransitionException.java
├── security/
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── scheduler/
│   └── EscalationScheduler.java
└── util/
    ├── Constants.java
    └── ApiResponse.java
```

---

## Environment Variables

Copy `.env.example` into a local `.env` or set the following environment variables:

| Variable Name | Default Value | Description |
| :--- | :--- | :--- |
| `DB_URL` | `jdbc:mysql://localhost:3306/ticketing_db` | Connection URL for MySQL database |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | `root` | Database password |
| `JWT_SECRET` | `your-very-long-secret-key-minimum-256-bits` | 256-bit signature key for JWT tokens |
| `JWT_EXPIRATION` | `86400000` | Expiration time of token in milliseconds (24 Hours) |
| `MAIL_HOST` | `smtp.gmail.com` | SMTP host |
| `MAIL_PORT` | `587` | SMTP port |
| `MAIL_USERNAME` | `your@gmail.com` | SMTP username |
| `MAIL_PASSWORD` | `yourpassword` | SMTP password / app password |

---

## Getting Started

### 1. Run Database (Docker Compose)
To start a MySQL server instantly via Docker:
```bash
docker-compose up -d mysql
```

### 2. Build the Application
Ensure all dependencies are downloaded and the project compiles clean:
```bash
mvn clean install -DskipTests
```

### 3. Run Locally
Execute the Spring Boot runner:
```bash
mvn spring-boot:run
```

Once started, verify:
* **Swagger Documentation UI**: `http://localhost:8080/swagger-ui.html`
* **Actuator Health Status**: `http://localhost:8080/actuator/health`
