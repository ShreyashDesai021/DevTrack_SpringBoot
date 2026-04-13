# DevTrack Frontend - React Application

React frontend for DevTrack developer productivity tracker with JWT authentication.

## 🚀 Features

- ✅ JWT Authentication (Login/Register)
- ✅ Protected Routes
- ✅ Task Management (with rich metadata)
- ✅ Session Tracking (with rich metadata)
- ✅ Analytics Dashboard (derived metrics)
- ✅ Clean, professional UI
- ✅ Responsive design

## 📦 Technologies

- React 18
- React Router DOM (routing)
- Axios (HTTP client)
- CSS (styling)

## 🏃 Quick Start

### 1. Install Dependencies

```bash
npm install
```

### 2. Configure API URL

Create `.env` file:
```bash
cp .env.example .env
```

Edit `.env`:
```
REACT_APP_API_URL=http://localhost:8080/api
```

### 3. Start Development Server

```bash
npm start
```

Application runs on: `http://localhost:3000`

## 🐳 Docker Development

### With Docker Compose (includes backend)

```bash
# In project root (where docker-compose.yml is)
docker-compose up
```

### Standalone Frontend Container

```bash
# Build
docker build -t devtrack-frontend .

# Run
docker run -p 3000:3000 \
  -e REACT_APP_API_URL=http://localhost:8080/api \
  devtrack-frontend
```

## 📂 Project Structure

```
src/
├── components/
│   └── Navbar.js              # Navigation bar
├── context/
│   └── AuthContext.js         # JWT authentication context
├── pages/
│   ├── Login.js               # Login page
│   ├── Register.js            # Registration page
│   ├── Dashboard.js           # Dashboard overview
│   ├── Tasks.js               # Task management
│   ├── Sessions.js            # Session tracking
│   └── Analytics.js           # Analytics dashboard
├── services/
│   └── api.js                 # Backend API service
├── App.js                     # Main app with routing
├── index.js                   # Entry point
└── index.css                  # Styling
```

## 🔐 Authentication Flow

1. User registers/logs in
2. Backend returns JWT token
3. Token stored in localStorage
4. Token included in all API requests
5. Protected routes check for valid token

## 📡 API Integration

All API calls go through `src/services/api.js`:

```javascript
import { taskAPI, sessionAPI, analyticsAPI } from './services/api';

// Tasks
taskAPI.getAll()
taskAPI.create(data)
taskAPI.update(id, data)
taskAPI.delete(id)

// Sessions
sessionAPI.getAll()
sessionAPI.create(data)
sessionAPI.delete(id)

// Analytics
analyticsAPI.get()
```

## 🚀 Deployment

### Vercel (Recommended)

```bash
# Install Vercel CLI
npm i -g vercel

# Deploy
vercel

# Set environment variable
vercel env add REACT_APP_API_URL
# Enter your Railway backend URL: https://your-backend.railway.app/api
```

### Manual Build

```bash
# Build for production
npm run build

# Serve build folder
npx serve -s build
```

## 🎨 Pages

### Login/Register
- JWT authentication
- Form validation
- Error handling
- Auto-redirect to dashboard

### Dashboard
- Quick stats overview
- Recent tasks
- Recent sessions
- Links to all sections

### Tasks
- Full CRUD operations
- Rich metadata:
  - Category
  - Estimated time
  - Actual time
  - Priority
  - Status
- Inline editing
- Delete confirmation

### Sessions
- Log coding sessions
- Rich metadata:
  - Work type
  - Outcome
  - Difficulty
  - Tags
- Visual session cards
- Duration formatting

### Analytics
- Success rate
- Coding streaks
- Time distribution (by project, category, work type)
- Focus areas
- Estimation accuracy

## 🔧 Configuration

### Environment Variables

```env
REACT_APP_API_URL=http://localhost:8080/api
```

For production:
```env
REACT_APP_API_URL=https://your-backend.railway.app/api
```

### API Base URL

The API URL is configured in `src/services/api.js`:
```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
```

## 🐛 Troubleshooting

### CORS Errors
- Backend must allow frontend origin
- Check backend CORS configuration

### API Connection Issues
- Verify REACT_APP_API_URL is correct
- Check backend is running
- Open browser console for errors

### Login Fails
- Check backend is running
- Verify credentials
- Check network tab for API response

### Protected Routes Redirect to Login
- Token might be expired (24 hours)
- Login again to get new token
- Check localStorage for token

## 📝 Scripts

```bash
npm start        # Start development server
npm run build    # Build for production
npm test         # Run tests
```

## 🎯 Features by Page

### Login/Register
- JWT token management
- LocalStorage persistence
- Auto-navigation on success
- Error display

### Dashboard
- Task count (total, completed, pending)
- Total coding hours
- Recent tasks table
- Recent sessions table

### Tasks
- Create task with all metadata
- Edit existing tasks
- Delete with confirmation
- Status badges (PENDING, IN_PROGRESS, DONE)
- Priority badges (LOW, MEDIUM, HIGH)
- Category filtering

### Sessions
- Log session with metadata
- Duration input (minutes)
- Work type dropdown
- Outcome selection
- Difficulty rating
- Tags (comma-separated)
- Visual cards with all info

### Analytics
- Success rate percentage
- Current/longest streak
- Time by project (bar charts)
- Time by category (count)
- Time by work type (bar charts)
- Top project/category
- Estimation accuracy

## 👥 Author

Built as a college full-stack project with:
- JWT authentication
- React + React Router
- Axios for API calls
- Clean, professional UI

---

**Version:** 2.0.0  
**Frontend:** React 18  
**Backend:** Spring Boot with JWT  
**Deployment:** Vercel (frontend) + Railway (backend)
