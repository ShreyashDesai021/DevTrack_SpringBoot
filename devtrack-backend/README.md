# DevTrack Backend - Developer Productivity Tracker

A Spring Boot REST API for tracking developer tasks and coding sessions with comprehensive testing.

## 📋 Features

- ✅ **Task Management** - CRUD operations for developer tasks
- ✅ **Coding Session Tracking** - Track time spent on projects
- ✅ **Dashboard Statistics** - Aggregated productivity metrics
- ✅ **Global Exception Handling** - @ControllerAdvice with custom exceptions
- ✅ **Comprehensive Testing** - MockMvc + JUnit tests
- ✅ **Clean Architecture** - Controller → Service → Repository pattern

## 🏗️ Architecture

```
Controller Layer  →  Service Layer  →  Repository Layer  →  Database
     ↓                    ↓                   ↓
  REST API          Business Logic        JPA/Hibernate      PostgreSQL
```

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL** (production)
- **H2** (testing)
- **JUnit 5** + **MockMvc**
- **Maven**

## 📦 Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL (for local development)

### Setup

1. **Clone the repository**
   ```bash
   cd devtrack-backend
   ```

2. **Configure database**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/devtrack
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

3. **Create PostgreSQL database**
   ```sql
   CREATE DATABASE devtrack;
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The API will be available at: `http://localhost:8080`

## 🧪 Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=TaskControllerTest
mvn test -Dtest=SessionServiceTest
```

### Run with coverage
```bash
mvn test jacoco:report
```

### Test Coverage
- **18 total tests**
- **MockMvc tests**: 14 (controller layer)
- **JUnit tests**: 18 (service layer)
- Covers: CRUD operations, exception handling, validation, business logic

## 📡 API Endpoints

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update task |
| DELETE | `/api/tasks/{id}` | Delete task |
| PATCH | `/api/tasks/{id}/status` | Update task status |

### Sessions

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/sessions` | Get all sessions |
| GET | `/api/sessions/{id}` | Get session by ID |
| POST | `/api/sessions` | Create new session |
| DELETE | `/api/sessions/{id}` | Delete session |

### Dashboard

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dashboard/stats` | Get summary statistics |

## 📝 Example Requests

### Create Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Build REST API",
    "description": "Create Spring Boot backend",
    "status": "PENDING"
  }'
```

### Create Coding Session
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -d '{
    "projectName": "DevTrack Backend",
    "startTime": "2024-04-01T10:00:00",
    "endTime": "2024-04-01T12:30:00"
  }'
```

### Get Dashboard Stats
```bash
curl http://localhost:8080/api/dashboard/stats
```

**Response:**
```json
{
  "totalTasks": 15,
  "completedTasks": 8,
  "totalCodingHours": 24.5,
  "totalSessions": 12
}
```

## 🧩 Project Structure

```
src/
├── main/java/com/devtrack/
│   ├── controller/         # REST endpoints
│   │   ├── TaskController.java
│   │   ├── SessionController.java
│   │   └── DashboardController.java
│   ├── service/           # Business logic
│   │   ├── TaskService.java
│   │   └── SessionService.java
│   ├── repository/        # Database access
│   │   ├── TaskRepository.java
│   │   └── SessionRepository.java
│   ├── model/            # Entities
│   │   ├── Task.java
│   │   └── CodingSession.java
│   ├── dto/              # Data Transfer Objects
│   │   ├── TaskDTO.java
│   │   ├── SessionDTO.java
│   │   ├── DashboardStatsDTO.java
│   │   └── ErrorResponse.java
│   ├── exception/        # Custom exceptions
│   │   ├── GlobalExceptionHandler.java  (@ControllerAdvice)
│   │   ├── ResourceNotFoundException.java
│   │   └── ValidationException.java
│   └── DevTrackApplication.java
│
└── test/java/com/devtrack/
    ├── controller/       # MockMvc tests
    │   ├── TaskControllerTest.java
    │   └── SessionControllerTest.java
    └── service/         # JUnit tests
        ├── TaskServiceTest.java
        └── SessionServiceTest.java
```

## 🚀 Deployment

### Railway (Recommended)

1. Create Railway account at railway.app
2. Create new project
3. Add PostgreSQL database
4. Deploy Spring Boot app
5. Set environment variables:
   ```
   SPRING_DATASOURCE_URL=${DATABASE_URL}
   ```

### Environment Variables for Production

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=...
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

## 🧠 Key Features for Evaluation

### 1. @ControllerAdvice (Global Exception Handling)
Located in `GlobalExceptionHandler.java`:
- Handles `ResourceNotFoundException` (404)
- Handles `ValidationException` (400)
- Handles validation errors from `@Valid`
- Provides consistent error responses

### 2. MockMvc Testing
14 tests covering:
- All CRUD endpoints
- Exception handling
- HTTP status codes
- JSON response validation

### 3. JUnit Testing
18 tests covering:
- Service layer business logic
- Repository interactions
- Validation logic
- Edge cases

### 4. Business Logic
- Automatic duration calculation in CodingSession
- Task status validation (PENDING/DONE only)
- End time must be after start time
- Dashboard aggregation queries

## 🐛 Troubleshooting

### Tests failing with database connection error
Solution: Tests use H2 in-memory database automatically. No PostgreSQL needed for tests.

### Port 8080 already in use
Solution: Change port in `application.properties`:
```properties
server.port=8081
```

### CORS errors from frontend
Solution: Controllers already have `@CrossOrigin(origins = "*")`. For production, restrict to specific domain.

## 📚 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [MockMvc Testing](https://spring.io/guides/gs/testing-web/)

## 👥 Author

Built as a college project demonstrating:
- Spring Boot REST API development
- Clean architecture patterns
- Comprehensive testing (MockMvc + JUnit)
- Global exception handling
- Database design with JPA

---

**API Base URL (Local):** `http://localhost:8080`  
**Version:** 1.0.0  
**Last Updated:** April 2024
