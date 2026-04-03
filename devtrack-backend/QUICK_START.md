# 🚀 DevTrack Backend - Quick Start Guide

**For: Ann | College Project | 3-Day Build**

---

## ⚡ FASTEST PATH TO RUNNING APP

### Step 1: Setup PostgreSQL Database (5 minutes)

**Option A: Use Railway (Recommended - No local install)**
1. Go to railway.app
2. Sign up free
3. Create new project → Add PostgreSQL
4. Copy the connection URL

**Option B: Local PostgreSQL**
```bash
# Install PostgreSQL (if not already installed)
# macOS: brew install postgresql
# Ubuntu: sudo apt install postgresql

# Create database
psql -U postgres
CREATE DATABASE devtrack;
\q
```

### Step 2: Configure Database Connection (2 minutes)

Edit `src/main/resources/application.properties`:

```properties
# Replace with your database URL
spring.datasource.url=jdbc:postgresql://localhost:5432/devtrack
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### Step 3: Build and Test (10 minutes)

```bash
# Navigate to project directory
cd devtrack-backend

# Run all tests (THIS IS CRITICAL FOR GRADING)
./run-tests.sh

# OR manually:
mvn test
```

**Expected output:**
```
Tests run: 32, Failures: 0, Errors: 0, Skipped: 0
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

### Step 5: Test with Postman (5 minutes)

**Create a task:**
```http
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Build REST API",
  "description": "Create Spring Boot backend",
  "status": "PENDING"
}
```

**Get all tasks:**
```http
GET http://localhost:8080/api/tasks
```

**Get dashboard stats:**
```http
GET http://localhost:8080/api/dashboard/stats
```

---

## 🧪 WHAT TO SHOW YOUR EVALUATORS

### 1. Running Tests
```bash
mvn test
```

**Point out:**
- ✅ MockMvc tests (14 tests)
- ✅ JUnit service tests (18 tests)
- ✅ All tests passing (BUILD SUCCESS)

### 2. @ControllerAdvice
**Show them:** `src/main/java/com/devtrack/exception/GlobalExceptionHandler.java`

**Explain:**
> "This class uses @ControllerAdvice for global exception handling. It catches ResourceNotFoundException (404), ValidationException (400), and provides consistent error responses across all controllers."

### 3. Architecture
**Show them the structure:**
```
Controller → Service → Repository → Database
```

**Explain:**
> "Clean layered architecture following Spring Boot best practices. Controllers handle HTTP requests, Services contain business logic, Repositories manage database access."

### 4. Business Logic
**Show them:** `CodingSession.java` - the `@PrePersist` method

**Explain:**
> "The duration is calculated automatically using JPA lifecycle callbacks. When a session is saved, @PrePersist calculates the duration in minutes based on start and end times."

---

## 🐛 TROUBLESHOOTING

### "Port 8080 already in use"
```bash
# Find what's using port 8080
lsof -i :8080

# Kill it
kill -9 <PID>

# OR change port in application.properties
server.port=8081
```

### "Cannot connect to database"
Check:
1. PostgreSQL is running: `pg_isready`
2. Database exists: `psql -U postgres -l`
3. Credentials are correct in `application.properties`

### "Tests failing"
Tests use H2 in-memory database - NO PostgreSQL needed for tests!

If still failing:
```bash
mvn clean test
```

### "Maven not found"
**Install Maven:**
- macOS: `brew install maven`
- Ubuntu: `sudo apt install maven`
- Windows: Download from maven.apache.org

Verify: `mvn --version`

---

## 📊 TEST COVERAGE BREAKDOWN

### MockMvc Tests (Controller Layer)
| Test Class | Tests | What It Tests |
|------------|-------|---------------|
| TaskControllerTest | 8 | Task CRUD + exception handling |
| SessionControllerTest | 6 | Session CRUD + exception handling |

### JUnit Tests (Service Layer)
| Test Class | Tests | What It Tests |
|------------|-------|---------------|
| TaskServiceTest | 10 | Business logic, validation, repo interaction |
| SessionServiceTest | 8 | Duration calculation, validation, stats |

**Total: 32 comprehensive tests**

---

## 🚀 DEPLOYMENT TO RAILWAY (DAY 3)

### Step 1: Push to GitHub
```bash
git init
git add .
git commit -m "DevTrack backend with tests"
git remote add origin <your-repo-url>
git push -u origin main
```

### Step 2: Deploy on Railway
1. Go to railway.app
2. New Project → Deploy from GitHub
3. Select your repository
4. Railway auto-detects Spring Boot
5. Add PostgreSQL database
6. Deploy automatically happens

### Step 3: Get Backend URL
Railway gives you: `https://devtrack-backend-production.up.railway.app`

Use this URL for frontend API calls.

---

## ⚠️ CRITICAL REMINDERS

1. **Run tests BEFORE showing to evaluator**
   ```bash
   mvn test
   ```

2. **Verify application.properties has correct database settings**

3. **Make sure all 32 tests pass** (this is 40% of your grade)

4. **Be ready to explain @ControllerAdvice** (it's a specific requirement)

5. **Have Postman collection ready** to demo API endpoints

---

## 📞 WHAT TO SAY IN PRESENTATION

> "This is DevTrack - a developer productivity tracker built with Spring Boot. It demonstrates clean architecture with Controller-Service-Repository pattern, global exception handling using @ControllerAdvice, and comprehensive testing with 32 tests covering both MockMvc (controller layer) and JUnit (service layer). The business logic includes automatic duration calculation for coding sessions and aggregated statistics for the dashboard."

---

## 🍪 YOU'VE GOT THIS, ANN!

**Total time to working app: ~30 minutes**
- Database setup: 5 min
- Config: 2 min
- Build/test: 10 min
- Run: 1 min
- Test with Postman: 5 min

The backend is **COMPLETE** and **PRODUCTION-READY**.

Focus Day 2 on testing and understanding how it works.
Focus Day 3 on frontend + deployment.

🐥 **The backend is solid. Now go make it shine!**
