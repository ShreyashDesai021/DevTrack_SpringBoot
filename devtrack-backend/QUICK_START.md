# 🚀 DevTrack Backend v2.0 - QUICK START GUIDE

**For: Ann | JWT Authentication + Rich Metadata + Analytics**  
**Timeline: 3-Day Project**

---

## ⚡ FASTEST PATH TO RUNNING APP

### Step 1: Setup PostgreSQL Database (5 minutes)

**Option A: Railway (Recommended - No local install)**
1. Go to railway.app
2. Sign up free
3. Create new project → Add PostgreSQL
4. Copy the connection URL from Railway

**Option B: Local PostgreSQL**
```bash
# Create database
psql -U postgres
CREATE DATABASE devtrack;
\q
```

### Step 2: Configure Application (2 minutes)

Edit `src/main/resources/application.properties`:

```properties
# Update these lines with your database info
spring.datasource.url=jdbc:postgresql://localhost:5432/devtrack
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

# JWT Secret (IMPORTANT: Change this for production!)
jwt.secret=your-super-secret-key-minimum-256-bits-for-security
jwt.expiration=86400000
```

### Step 3: Build and Test (10 minutes)

```bash
# Navigate to project
cd devtrack-backend

# Build project
mvn clean install

# Run tests (CRITICAL - shows it works!)
mvn test
```

**Expected output:**
```
Tests run: 20+, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Step 4: Run the Application (1 minute)

```bash
mvn spring-boot:run
```

**Expected output:**
```
Started DevTrackApplication in 3.456 seconds
Tomcat started on port(s): 8080
```

### Step 5: Test with Postman/curl (5 minutes)

#### **A. Register a User**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ann",
    "email": "ann@example.com",
    "password": "test123"
  }'
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

**📌 COPY THE TOKEN!** You need it for all other requests.

#### **B. Create a Task (with JWT)**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "title": "Build REST API",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "category": "Backend",
    "estimatedMinutes": 120
  }'
```

#### **C. Create a Coding Session (with JWT)**
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "projectName": "DevTrack Backend",
    "summary": "Implemented JWT auth",
    "durationMinutes": 180,
    "sessionDate": "2024-04-07",
    "workType": "Feature",
    "outcome": "Completed",
    "difficulty": "Hard",
    "tags": "Spring Security,JWT,BCrypt"
  }'
```

#### **D. Get Analytics (with JWT)**
```bash
curl -X GET http://localhost:8080/api/analytics \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

---

## 🎓 WHAT TO SHOW YOUR EVALUATORS

### 1. **JWT Authentication** ⭐

**Show them:**
- `JwtUtil.java` - Token generation
- `JwtAuthenticationFilter.java` - Request interception
- `SecurityConfig.java` - Spring Security configuration

**Explain:**
> "This application uses JWT (JSON Web Tokens) for stateless authentication. When a user logs in, they receive a JWT token that must be included in the Authorization header for all protected endpoints. The JwtAuthenticationFilter intercepts every request, validates the token, and sets the security context."

**Demo:**
1. Register a user → Get JWT token
2. Make request WITHOUT token → 401 Unauthorized
3. Make request WITH token → Success

### 2. **BCrypt Password Hashing** ⭐

**Show them:**
- `AuthService.java` - Lines where passwords are hashed

**Explain:**
> "Passwords are never stored in plain text. We use BCrypt, a strong one-way hashing algorithm. During registration, the password is hashed using BCrypt. During login, we use BCrypt's matching function to verify the password without ever decrypting the stored hash."

**Code to point out:**
```java
// Registration
user.setPassword(passwordEncoder.encode(request.getPassword()));

// Login
passwordEncoder.matches(request.getPassword(), user.getPassword())
```

### 3. **User-Scoped Data** ⭐

**Show them:**
- `TaskService.java` - User validation in getTaskById
- `TaskRepository.java` - User-scoped queries

**Explain:**
> "Every task and session belongs to a specific user. The services validate that the authenticated user owns the data before allowing access. This ensures complete data isolation between users."

**Demo:**
1. Create task as User A
2. Try to access it as User B → 404 Not Found
3. Show it works for User A

### 4. **Rich Metadata** ⭐

**Show them:**
- `Task.java` - All the metadata fields
- `CodingSession.java` - Work type, outcome, difficulty, tags

**Explain:**
> "Beyond basic CRUD, we track rich metadata that enables advanced analytics. Tasks have categories, time estimates vs actuals, and completion timestamps. Sessions have work types, outcomes, difficulty levels, and tags for detailed insights."

### 5. **Analytics Engine** ⭐

**Show them:**
- `AnalyticsService.java` - Derived metrics calculation
- GET `/api/analytics` response

**Explain:**
> "The analytics engine derives insights from the raw data. It calculates success rate (percentage of tasks completed), coding streaks (consecutive days with sessions), time distribution by project/category/work type, and estimation accuracy by comparing estimated vs actual time."

**Demo:**
```bash
# Create some tasks and sessions, then:
curl -X GET http://localhost:8080/api/analytics \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. **@ControllerAdvice** ⭐

**Show them:**
- `GlobalExceptionHandler.java`

**Explain:**
> "Using @ControllerAdvice, we centralize exception handling across all controllers. This provides consistent error responses. For example, ResourceNotFoundException automatically returns 404, AuthenticationException returns 401, and ValidationException returns 400 with field-level errors."

### 7. **Comprehensive Testing** ⭐

**Run and show:**
```bash
mvn test
```

**Explain:**
> "We have comprehensive test coverage including:
> - AuthServiceTest: BCrypt password hashing and JWT generation
> - TaskServiceTest: User scoping and metadata handling
> - AuthControllerTest: Registration and login endpoints
> - TaskControllerTest: JWT-protected CRUD operations
> - SessionControllerTest: Session management with metadata"

---

## 📊 DATABASE SCHEMA EXPLANATION

When they ask "Show me the database":

```sql
-- Users table (BCrypt passwords)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),  -- BCrypt hashed
    created_at TIMESTAMP
);

-- Tasks table (rich metadata)
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    title VARCHAR(255),
    status VARCHAR(20),     -- PENDING, IN_PROGRESS, DONE
    priority VARCHAR(20),   -- LOW, MEDIUM, HIGH
    category VARCHAR(50),   -- Backend, Frontend, etc.
    estimated_minutes INTEGER,
    actual_minutes INTEGER,
    completed_at TIMESTAMP,
    created_at TIMESTAMP
);

-- Coding sessions table (rich metadata)
CREATE TABLE coding_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    project_name VARCHAR(255),
    summary TEXT,
    duration_minutes INTEGER,
    session_date DATE,
    work_type VARCHAR(50),   -- Feature, Bugfix, etc.
    outcome VARCHAR(50),     -- Completed, In Progress, etc.
    difficulty VARCHAR(20),  -- Easy, Medium, Hard
    tags TEXT,              -- Comma-separated
    created_at TIMESTAMP
);
```

---

## 🔥 IMPRESSIVE FEATURES TO HIGHLIGHT

### 1. **Stateless Authentication**
"JWT tokens are stateless - the server doesn't store session data. This makes the app horizontally scalable."

### 2. **Password Security**
"We use BCrypt with automatic salt generation. Even if the database is compromised, passwords cannot be recovered."

### 3. **Data Isolation**
"Complete user data isolation at the service layer. Users cannot access each other's data even if they know the IDs."

### 4. **Derived Analytics**
"Analytics are calculated on-demand from the raw data. Success rate, streaks, and time distributions are all derived metrics."

### 5. **Estimation Tracking**
"The system tracks how accurate users are at estimating task duration, helping them improve planning over time."

---

## 🐛 TROUBLESHOOTING

### "Unauthorized" Error
**Problem:** Getting 401 Unauthorized on protected endpoints  
**Solution:** 
1. Make sure you registered/logged in
2. Copy the JWT token from the response
3. Include it in headers: `Authorization: Bearer YOUR_TOKEN`
4. Token expires in 24 hours - login again if needed

### "User not found"
**Problem:** Login fails with "User not found"  
**Solution:** Register first using `/api/auth/register`

### Database Connection Error
**Problem:** Can't connect to PostgreSQL  
**Solution:**
1. Check PostgreSQL is running: `pg_isready`
2. Verify database exists: `psql -l`
3. Check credentials in `application.properties`

### Tests Failing
**Problem:** Some tests fail  
**Solution:**
1. Tests use H2 in-memory database (no PostgreSQL needed)
2. Run: `mvn clean test`
3. Check for compilation errors first

---

## 📝 QUICK API REFERENCE

### Public Endpoints (No JWT)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login (returns JWT)

### Protected Endpoints (JWT Required)
- `GET /api/tasks` - Get user's tasks
- `POST /api/tasks` - Create task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task
- `GET /api/sessions` - Get user's sessions
- `POST /api/sessions` - Create session
- `DELETE /api/sessions/{id}` - Delete session
- `GET /api/analytics` - Get analytics

**All protected endpoints require:**
```
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## 🚀 DEPLOYMENT (DAY 3)

### Backend to Railway
1. Push code to GitHub
2. Connect Railway to GitHub repo
3. Railway auto-detects Spring Boot
4. Add PostgreSQL database
5. Set environment variables:
   ```
   SPRING_DATASOURCE_URL=${DATABASE_URL}
   JWT_SECRET=your-production-secret-256-bits
   ```
6. Deploy!

### Frontend to Vercel
1. Set API base URL: `REACT_APP_API_URL=https://your-backend.railway.app`
2. Deploy React app
3. Test full flow: Register → Login → Create Task → View Analytics

---

## 💡 PRESENTATION TIPS

### Opening Statement
> "I've built DevTrack, a full-stack developer productivity tracker with JWT authentication, rich metadata tracking, and a comprehensive analytics engine. It demonstrates secure authentication with BCrypt password hashing, user-scoped data architecture, and derived analytics from tracked coding sessions."

### When They Ask About Security
> "Security is implemented at multiple layers. Passwords are hashed with BCrypt before storage. All protected endpoints require JWT authentication. Data isolation is enforced at the service layer - users can only access their own tasks and sessions. The JWT filter intercepts every request and validates the token before allowing access."

### When They Ask About the Database
> "The database has three main tables: users (with BCrypt-hashed passwords), tasks (with rich metadata like categories, estimates, and actuals), and coding sessions (with work type, outcome, difficulty, and tags). All task and session data is scoped to the authenticated user via foreign key relationships."

### When They Ask About Testing
[Run `mvn test` and show]:
> "We have comprehensive test coverage including service layer tests for business logic, controller layer tests using MockMvc for HTTP endpoints, and specific tests for JWT authentication and BCrypt password hashing. All tests pass."

---

## 🍪 YOU'VE GOT THIS, ANN!

**What you have:**
✅ JWT authentication with Spring Security  
✅ BCrypt password hashing  
✅ User-scoped data architecture  
✅ Rich metadata (categories, estimates, work types, tags)  
✅ Analytics engine (success rate, streaks, time distribution)  
✅ @ControllerAdvice exception handling  
✅ Comprehensive testing  
✅ Clean architecture  

**This is production-grade work.**

Now go show them what you built! 🚀🐥
