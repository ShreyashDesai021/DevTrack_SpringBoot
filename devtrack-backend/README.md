# DevTrack Backend v2.0 - Developer Productivity Tracker

A Spring Boot REST API with **JWT authentication**, rich metadata tracking, and comprehensive analytics for developer productivity.

## 🎯 Features

- ✅ **JWT Authentication** - Secure user registration and login with BCrypt password hashing
- ✅ **User-Scoped Data** - Each user sees only their own tasks and sessions
- ✅ **Rich Task Metadata** - Category, estimates, actuals, completion tracking
- ✅ **Rich Session Metadata** - Work type, outcome, difficulty, tags
- ✅ **Analytics Engine** - Success rate, streaks, time distribution, estimation accuracy
- ✅ **@ControllerAdvice** - Global exception handling
- ✅ **Comprehensive Testing** - MockMvc + JUnit tests
- ✅ **Clean Architecture** - Controller → Service → Repository pattern

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│         React Frontend (with JWT)               │
│   - Login/Register                              │
│   - Task Dashboard                              │
│   - Session Tracker                             │
│   - Analytics View                              │
└─────────────────┬───────────────────────────────┘
                  │ REST API (JWT Bearer Token)
┌─────────────────▼───────────────────────────────┐
│    Spring Boot Backend + Spring Security        │
│                                                  │
│  ┌────────────────────────────────────────────┐ │
│  │    JWT Authentication Filter               │ │
│  │    Validates Bearer tokens                 │ │
│  └────────────────────────────────────────────┘ │
│                                                  │
│  ┌────────────────────────────────────────────┐ │
│  │    @ControllerAdvice                       │ │
│  │    Global Exception Handler                │ │
│  └────────────────────────────────────────────┘ │
│                                                  │
│  Controllers (JWT Protected)                    │
│  - AuthController (Public)                      │
│  - TaskController (Protected)                   │
│  - SessionController (Protected)                │
│  - AnalyticsController (Protected)              │
│                                                  │
│  Services (Business Logic)                      │
│  - AuthService (BCrypt + JWT)                   │
│  - TaskService (User-scoped CRUD)               │
│  - SessionService (User-scoped CRUD)            │
│  - AnalyticsService (Derived metrics)           │
│                                                  │
│  Repositories (JPA)                             │
│  - UserRepository                               │
│  - TaskRepository (User-scoped queries)         │
│  - SessionRepository (Analytics queries)        │
│                                                  │
│  ┌────────────────────────────────────────────┐ │
│  │       PostgreSQL Database                  │ │
│  │   - users (BCrypt passwords)               │ │
│  │   - tasks (with metadata)                  │ │
│  │   - coding_sessions (with metadata)        │ │
│  └────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────┘
```

## 🗄️ Database Schema

### USERS
```sql
id              BIGSERIAL PRIMARY KEY
name            VARCHAR(100)
email           VARCHAR(255) UNIQUE
password        VARCHAR(255)  -- BCrypt hashed
created_at      TIMESTAMP
```

### TASKS
```sql
id                  BIGSERIAL PRIMARY KEY
user_id             BIGINT REFERENCES users(id)
-- Core fields
title               VARCHAR(255)
status              VARCHAR(20)  -- PENDING, IN_PROGRESS, DONE
priority            VARCHAR(20)  -- LOW, MEDIUM, HIGH
-- Metadata fields
category            VARCHAR(50)  -- Backend, Frontend, Database, etc.
estimated_minutes   INTEGER
actual_minutes      INTEGER
completed_at        TIMESTAMP
created_at          TIMESTAMP
```

### CODING_SESSIONS
```sql
id                  BIGSERIAL PRIMARY KEY
user_id             BIGINT REFERENCES users(id)
-- Core fields
project_name        VARCHAR(255)
summary             TEXT
duration_minutes    INTEGER
session_date        DATE
-- Metadata fields
work_type           VARCHAR(50)  -- Feature, Bugfix, Refactor, Learning
outcome             VARCHAR(50)  -- Completed, In Progress, Blocked, Prototype
difficulty          VARCHAR(20)  -- Easy, Medium, Hard, Very Hard
tags                TEXT         -- Comma-separated: "React,Redux,API"
created_at          TIMESTAMP
```

## 🔌 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user (returns JWT) |

### Tasks (JWT Protected)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks for authenticated user |
| GET | `/api/tasks/{id}` | Get task by ID (user-scoped) |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update task |
| DELETE | `/api/tasks/{id}` | Delete task |

### Sessions (JWT Protected)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/sessions` | Get all sessions for authenticated user |
| GET | `/api/sessions/{id}` | Get session by ID (user-scoped) |
| POST | `/api/sessions` | Create new session |
| DELETE | `/api/sessions/{id}` | Delete session |

### Analytics (JWT Protected)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/analytics` | Get complete analytics for authenticated user |

## 📝 Example API Usage

### 1. Register User
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "Ann",
  "email": "ann@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "Ann",
  "email": "ann@example.com"
}
```

### 2. Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "ann@example.com",
  "password": "password123"
}
```

**Response:** Same as registration

### 3. Create Task (with JWT)
```bash
POST http://localhost:8080/api/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Build REST API",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "category": "Backend",
  "estimatedMinutes": 120
}
```

### 4. Create Coding Session (with JWT)
```bash
POST http://localhost:8080/api/sessions
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "projectName": "DevTrack Backend",
  "summary": "Implemented JWT authentication and user scoping",
  "durationMinutes": 180,
  "sessionDate": "2024-04-07",
  "workType": "Feature",
  "outcome": "Completed",
  "difficulty": "Hard",
  "tags": "Spring Security,JWT,BCrypt"
}
```

### 5. Get Analytics (with JWT)
```bash
GET http://localhost:8080/api/analytics
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:**
```json
{
  "successRate": 75.5,
  "currentStreak": 5,
  "longestStreak": 12,
  "timeByCategory": {
    "Backend": 15,
    "Frontend": 8,
    "Database": 3
  },
  "timeByProject": {
    "DevTrack": 520,
    "Portfolio Site": 180
  },
  "timeByWorkType": {
    "Feature": 450,
    "Bugfix": 150,
    "Refactor": 100
  },
  "topProject": "DevTrack",
  "topCategory": "Backend",
  "estimationAccuracy": 82.3,
  "averageEstimationError": 15
}
```

## 🔐 JWT Authentication Flow

1. **Register/Login**: User receives JWT token
2. **Store Token**: Frontend stores token (localStorage or memory)
3. **Authenticated Requests**: Include token in Authorization header
   ```
   Authorization: Bearer <jwt-token>
   ```
4. **Token Validation**: JwtAuthenticationFilter validates every request
5. **User Context**: Controller extracts user email from Authentication object
6. **Data Scoping**: Services filter data by authenticated user

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=TaskControllerTest

# Tests included:
# - AuthServiceTest (BCrypt password hashing, JWT generation)
# - TaskServiceTest (User scoping, metadata handling)
# - AuthControllerTest (Registration, login endpoints)
# - TaskControllerTest (JWT-protected CRUD operations)
```

## 🚀 Setup & Deployment

### 1. Database Setup

**Option A: Railway (Recommended)**
- Go to railway.app
- Create PostgreSQL database
- Copy DATABASE_URL

**Option B: Local PostgreSQL**
```bash
createdb devtrack
```

### 2. Configure Database

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/devtrack
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

# JWT Secret (change for production!)
jwt.secret=your-secret-key-minimum-256-bits
jwt.expiration=86400000
```

### 3. Build & Run

```bash
# Install dependencies
mvn clean install

# Run application
mvn spring-boot:run
```

Application starts on: `http://localhost:8080`

### 4. Test Authentication

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Ann","email":"ann@example.com","password":"test123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ann@example.com","password":"test123"}'
```

## 📊 Analytics Metrics Explained

### Success Rate
Percentage of tasks marked as DONE out of total tasks created.

### Current Streak
Number of consecutive days (from today backwards) with at least one coding session.

### Longest Streak
Longest consecutive streak of coding days in the last 90 days.

### Time Distribution
Breakdown of time spent by:
- **Category**: From tasks (Backend, Frontend, etc.)
- **Project**: From sessions
- **Work Type**: From sessions (Feature, Bugfix, etc.)

### Estimation Accuracy
How accurate are your time estimates:
- Compares `estimatedMinutes` vs `actualMinutes` for completed tasks
- Lower error = higher accuracy
- Helps improve planning over time

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT authentication)
- **Spring Data JPA**
- **PostgreSQL** (production)
- **H2** (testing)
- **JJWT 0.12.3** (JWT library)
- **BCrypt** (password hashing)
- **JUnit 5** + **MockMvc**
- **Maven**

## 🔒 Security Features

1. **BCrypt Password Hashing**: Passwords never stored in plain text
2. **JWT Tokens**: Stateless authentication (24-hour expiration)
3. **User Data Isolation**: Each user can only access their own data
4. **Input Validation**: @Valid annotations on all inputs
5. **CORS Configuration**: Configurable for production
6. **Exception Handling**: No sensitive data in error responses

## 🐛 Troubleshooting

### "Unauthorized" errors
- Check JWT token is included: `Authorization: Bearer <token>`
- Token might be expired (24 hours validity)
- Re-login to get new token

### "User not found" errors
- Register first using `/api/auth/register`
- Verify email is correct

### Database connection errors
- Check PostgreSQL is running
- Verify credentials in `application.properties`
- Database must exist (create with `createdb devtrack`)

## 📚 Project Structure

```
src/
├── main/java/com/devtrack/
│   ├── controller/
│   │   ├── AuthController.java         # Registration & Login
│   │   ├── TaskController.java         # Task CRUD (JWT protected)
│   │   ├── SessionController.java      # Session CRUD (JWT protected)
│   │   └── AnalyticsController.java    # Analytics (JWT protected)
│   │
│   ├── service/
│   │   ├── AuthService.java            # BCrypt + JWT logic
│   │   ├── TaskService.java            # User-scoped task logic
│   │   ├── SessionService.java         # User-scoped session logic
│   │   └── AnalyticsService.java       # Derived metrics calculation
│   │
│   ├── security/
│   │   ├── JwtUtil.java                # JWT generation & validation
│   │   └── JwtAuthenticationFilter.java # Request interceptor
│   │
│   ├── model/
│   │   ├── User.java                   # User entity
│   │   ├── Task.java                   # Task entity (with metadata)
│   │   └── CodingSession.java          # Session entity (with metadata)
│   │
│   ├── repository/
│   │   ├── UserRepository.java         # User queries
│   │   ├── TaskRepository.java         # User-scoped task queries
│   │   └── SessionRepository.java      # Analytics queries
│   │
│   ├── dto/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── AuthResponse.java
│   │   ├── TaskDTO.java
│   │   ├── SessionDTO.java
│   │   └── AnalyticsDTO.java
│   │
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java  # @ControllerAdvice
│   │   ├── ResourceNotFoundException.java
│   │   ├── ValidationException.java
│   │   └── AuthenticationException.java
│   │
│   └── config/
│       └── SecurityConfig.java          # Spring Security config
│
└── test/java/com/devtrack/
    ├── controller/
    │   ├── AuthControllerTest.java
    │   └── TaskControllerTest.java
    └── service/
        ├── AuthServiceTest.java
        └── TaskServiceTest.java
```

## 👥 Author

Built as a college full-stack project demonstrating:
- JWT authentication with Spring Security
- BCrypt password hashing
- User-scoped data architecture
- Rich metadata tracking
- Analytics engine
- Clean architecture patterns
- Comprehensive testing

---

**Version:** 2.0.0  
**Last Updated:** April 2024  
**API Base URL:** `http://localhost:8080`
