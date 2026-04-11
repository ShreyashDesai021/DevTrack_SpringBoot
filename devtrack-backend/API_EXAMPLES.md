# DevTrack API Examples - Complete Testing Guide

This file contains complete API examples for testing the DevTrack backend.

## 🔧 Setup

Base URL: `http://localhost:8080`

**Important:** Save the JWT token from registration/login and use it in all protected endpoints!

---

## 1️⃣ AUTHENTICATION

### Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ann",
    "email": "ann@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInN1YiI6ImFubkBleGFtcGxlLmNvbSIsImlhdCI6MTcxMjUwMDAwMCwiZXhwIjoxNzEyNTg2NDAwfQ.xyz123",
  "type": "Bearer",
  "userId": 1,
  "name": "Ann",
  "email": "ann@example.com"
}
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ann@example.com",
    "password": "password123"
  }'
```

**Expected Response:** Same as registration

---

## 2️⃣ TASK MANAGEMENT (JWT Required)

**⚠️ Replace YOUR_JWT_TOKEN with the actual token from login!**

### Create Task with Full Metadata
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Build REST API with JWT",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "category": "Backend",
    "estimatedMinutes": 180
  }'
```

### Create Another Task (for testing analytics)
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Design React Frontend",
    "status": "PENDING",
    "priority": "MEDIUM",
    "category": "Frontend",
    "estimatedMinutes": 120
  }'
```

### Create Completed Task (with actual time)
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Setup PostgreSQL Database",
    "status": "DONE",
    "priority": "HIGH",
    "category": "Database",
    "estimatedMinutes": 60,
    "actualMinutes": 75
  }'
```

### Get All Tasks
```bash
curl -X GET http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Single Task
```bash
curl -X GET http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update Task (Mark as Done)
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "status": "DONE",
    "actualMinutes": 200
  }'
```

### Delete Task
```bash
curl -X DELETE http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 3️⃣ CODING SESSIONS (JWT Required)

### Create Session - Feature Development
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "projectName": "DevTrack Backend",
    "summary": "Implemented JWT authentication with Spring Security and BCrypt password hashing",
    "durationMinutes": 240,
    "sessionDate": "2024-04-07",
    "workType": "Feature",
    "outcome": "Completed",
    "difficulty": "Hard",
    "tags": "Spring Security,JWT,BCrypt,Authentication"
  }'
```

### Create Session - Bug Fix
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "projectName": "DevTrack Backend",
    "summary": "Fixed CORS issue preventing frontend from connecting",
    "durationMinutes": 45,
    "sessionDate": "2024-04-06",
    "workType": "Bugfix",
    "outcome": "Completed",
    "difficulty": "Easy",
    "tags": "CORS,Spring Boot,Configuration"
  }'
```

### Create Session - Learning
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "projectName": "Personal Learning",
    "summary": "Studied JPA query optimization and indexing strategies",
    "durationMinutes": 120,
    "sessionDate": "2024-04-05",
    "workType": "Learning",
    "outcome": "Prototype",
    "difficulty": "Medium",
    "tags": "JPA,Database,Performance"
  }'
```

### Create Session - Different Project (for analytics testing)
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "projectName": "Portfolio Website",
    "summary": "Built responsive landing page with Tailwind CSS",
    "durationMinutes": 90,
    "sessionDate": "2024-04-08",
    "workType": "Feature",
    "outcome": "In Progress",
    "difficulty": "Easy",
    "tags": "React,Tailwind,CSS"
  }'
```

### Get All Sessions
```bash
curl -X GET http://localhost:8080/api/sessions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Single Session
```bash
curl -X GET http://localhost:8080/api/sessions/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Delete Session
```bash
curl -X DELETE http://localhost:8080/api/sessions/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 4️⃣ ANALYTICS (JWT Required)

### Get Complete Analytics
```bash
curl -X GET http://localhost:8080/api/analytics \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response:**
```json
{
  "successRate": 66.67,
  "currentStreak": 3,
  "longestStreak": 5,
  "timeByCategory": {
    "Backend": 5,
    "Frontend": 3,
    "Database": 2
  },
  "timeByProject": {
    "DevTrack Backend": 285,
    "Portfolio Website": 90,
    "Personal Learning": 120
  },
  "timeByWorkType": {
    "Feature": 330,
    "Bugfix": 45,
    "Learning": 120
  },
  "topProject": "DevTrack Backend",
  "topCategory": "Backend",
  "estimationAccuracy": 85.5,
  "averageEstimationError": 12
}
```

---

## 5️⃣ TESTING ERROR HANDLING

### Test 401 Unauthorized (No Token)
```bash
curl -X GET http://localhost:8080/api/tasks
```

**Expected Response:**
```json
{
  "status": 401,
  "message": "Full authentication is required to access this resource",
  "timestamp": "2024-04-07T10:30:00"
}
```

### Test 404 Not Found (Invalid Task ID)
```bash
curl -X GET http://localhost:8080/api/tasks/999 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response:**
```json
{
  "status": 404,
  "message": "Task not found with id: 999",
  "timestamp": "2024-04-07T10:31:00"
}
```

### Test 400 Validation Error (Invalid Email)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test",
    "email": "invalid-email",
    "password": "test123"
  }'
```

**Expected Response:**
```json
{
  "status": 400,
  "errors": {
    "email": "Email should be valid"
  },
  "timestamp": "2024-04-07T10:32:00"
}
```

---

## 6️⃣ TESTING USER DATA ISOLATION

### Create Second User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob",
    "email": "bob@example.com",
    "password": "password123"
  }'
```

**Save Bob's token!**

### Create Task as Bob
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer BOBS_JWT_TOKEN" \
  -d '{
    "title": "Bob's Task",
    "status": "PENDING",
    "priority": "LOW",
    "category": "Testing"
  }'
```

### Try to Access Bob's Task as Ann (Should Fail)
```bash
curl -X GET http://localhost:8080/api/tasks/4 \
  -H "Authorization: Bearer ANNS_JWT_TOKEN"
```

**Expected:** 404 Not Found (Ann cannot see Bob's tasks)

### Verify Ann Only Sees Her Own Tasks
```bash
curl -X GET http://localhost:8080/api/tasks \
  -H "Authorization: Bearer ANNS_JWT_TOKEN"
```

**Expected:** Only Ann's tasks, not Bob's

---

## 7️⃣ TESTING PASSWORD SECURITY

### Try Login with Wrong Password
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ann@example.com",
    "password": "wrongpassword"
  }'
```

**Expected Response:**
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": "2024-04-07T10:35:00"
}
```

---

## 📊 FULL WORKFLOW TEST

Run these commands in order to populate the database with realistic data:

```bash
# 1. Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Ann","email":"ann@test.com","password":"test123"}'

# Save the token!
TOKEN="YOUR_TOKEN_HERE"

# 2. Create 5 tasks
curl -X POST http://localhost:8080/api/tasks -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"title":"Setup project","status":"DONE","priority":"HIGH","category":"Setup","estimatedMinutes":30,"actualMinutes":35}'

curl -X POST http://localhost:8080/api/tasks -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"title":"Build backend API","status":"DONE","priority":"HIGH","category":"Backend","estimatedMinutes":240,"actualMinutes":280}'

curl -X POST http://localhost:8080/api/tasks -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"title":"Design frontend","status":"IN_PROGRESS","priority":"MEDIUM","category":"Frontend","estimatedMinutes":180}'

curl -X POST http://localhost:8080/api/tasks -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"title":"Write tests","status":"PENDING","priority":"HIGH","category":"Testing","estimatedMinutes":120}'

curl -X POST http://localhost:8080/api/tasks -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"title":"Deploy to production","status":"PENDING","priority":"MEDIUM","category":"DevOps","estimatedMinutes":60}'

# 3. Create 5 sessions
curl -X POST http://localhost:8080/api/sessions -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"projectName":"DevTrack","summary":"Initial setup and configuration","durationMinutes":90,"sessionDate":"2024-04-01","workType":"Setup","outcome":"Completed","difficulty":"Easy","tags":"Spring Boot,PostgreSQL"}'

curl -X POST http://localhost:8080/api/sessions -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"projectName":"DevTrack","summary":"JWT authentication implementation","durationMinutes":240,"sessionDate":"2024-04-02","workType":"Feature","outcome":"Completed","difficulty":"Hard","tags":"Spring Security,JWT,BCrypt"}'

curl -X POST http://localhost:8080/api/sessions -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"projectName":"DevTrack","summary":"Built analytics engine","durationMinutes":180,"sessionDate":"2024-04-03","workType":"Feature","outcome":"Completed","difficulty":"Medium","tags":"Analytics,JPA,Queries"}'

curl -X POST http://localhost:8080/api/sessions -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"projectName":"DevTrack","summary":"Frontend React components","durationMinutes":150,"sessionDate":"2024-04-04","workType":"Feature","outcome":"In Progress","difficulty":"Medium","tags":"React,Tailwind,Components"}'

curl -X POST http://localhost:8080/api/sessions -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"projectName":"Portfolio","summary":"Personal website updates","durationMinutes":60,"sessionDate":"2024-04-05","workType":"Refactor","outcome":"Completed","difficulty":"Easy","tags":"HTML,CSS"}'

# 4. View analytics
curl -X GET http://localhost:8080/api/analytics -H "Authorization: Bearer $TOKEN"
```

---

## ✅ SUCCESS CRITERIA

After running all these examples, you should have:

- ✅ 2 registered users (Ann and Bob)
- ✅ Multiple tasks per user (different statuses, categories)
- ✅ Multiple sessions per user (different projects, work types)
- ✅ Analytics showing success rate, streaks, time distribution
- ✅ Proper error responses (401, 404, 400)
- ✅ Data isolation (users can't see each other's data)

**This proves your backend is working correctly!** 🎉
