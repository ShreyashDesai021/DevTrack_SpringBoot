# 🚀 DevTrack Frontend - QUICK START

**Get the frontend running in 5 minutes!**

---

## ⚡ FASTEST PATH

### 1. Install Dependencies (2 minutes)

```bash
cd devtrack-frontend
npm install
```

### 2. Configure Backend URL (30 seconds)

Create `.env` file:
```bash
cp .env.example .env
```

The file contains:
```
REACT_APP_API_URL=http://localhost:8080/api
```

**That's it! This connects to your Docker backend.**

### 3. Start Frontend (30 seconds)

```bash
npm start
```

**Frontend opens automatically at:** `http://localhost:3000`

---

## 🎯 COMPLETE WORKFLOW

### **Step 1: Make sure backend is running**

```bash
# In backend directory
docker-compose up
```

Backend should be running on `http://localhost:8080`

### **Step 2: Start frontend**

```bash
# In frontend directory
npm start
```

Frontend opens on `http://localhost:3000`

### **Step 3: Test the app**

1. **Register**: Click "Register" → Create account
2. **Dashboard**: See overview (will be empty first time)
3. **Create Task**: Go to Tasks → Click "+ New Task"
4. **Log Session**: Go to Sessions → Click "+ Log Session"
5. **View Analytics**: Go to Analytics → See your metrics

---

## 📦 WHAT YOU GET

### **Pages:**
- ✅ Login/Register (JWT authentication)
- ✅ Dashboard (quick overview)
- ✅ Tasks (full CRUD with metadata)
- ✅ Sessions (tracking with metadata)
- ✅ Analytics (derived metrics)

### **Features:**
- ✅ JWT token management
- ✅ Protected routes
- ✅ Form validation
- ✅ Error handling
- ✅ Clean UI
- ✅ Responsive design

---

## 🐳 USING DOCKER (Optional)

If you want to run frontend in Docker:

```bash
# Build
docker build -t devtrack-frontend .

# Run
docker run -p 3000:80 \
  -e REACT_APP_API_URL=http://localhost:8080/api \
  devtrack-frontend
```

---

## 🚀 DEPLOYMENT TO VERCEL

### **Step 1: Push to GitHub**

```bash
cd devtrack-frontend
git init
git add .
git commit -m "DevTrack frontend"
git remote add origin YOUR_REPO_URL
git push -u origin main
```

### **Step 2: Deploy on Vercel**

1. Go to vercel.com
2. Click "New Project"
3. Import your GitHub repo
4. Vercel auto-detects React
5. Add environment variable:
   - Name: `REACT_APP_API_URL`
   - Value: `https://your-backend.railway.app/api`
6. Click "Deploy"

**Done!** Your frontend is live.

---

## 🔧 CONFIGURATION

### **Environment Variables**

For **local development**:
```env
REACT_APP_API_URL=http://localhost:8080/api
```

For **production** (Vercel):
```env
REACT_APP_API_URL=https://your-backend.railway.app/api
```

**Note:** Backend URL must end with `/api`

---

## 🐛 TROUBLESHOOTING

### "Cannot connect to backend"
**Check:**
1. Backend is running: `docker-compose ps`
2. Backend URL in `.env` is correct
3. Backend accessible at `http://localhost:8080`

### "Login fails"
**Check:**
1. Registered user exists
2. Password is correct (min 6 chars)
3. Check browser console for errors

### "CORS error"
**Solution:**
Backend already has CORS configured for `*`.
If still getting errors, restart backend:
```bash
docker-compose restart backend
```

### "Page blank after login"
**Check:**
1. Open browser console (F12)
2. Look for API errors
3. Verify token in localStorage

---

## 📱 TESTING THE APP

### **1. Register**
- Name: Ann
- Email: ann@test.com
- Password: test123

### **2. Create Task**
- Title: Build REST API
- Status: IN_PROGRESS
- Priority: HIGH
- Category: Backend
- Estimated: 180 minutes

### **3. Log Session**
- Project: DevTrack Backend
- Duration: 120 minutes
- Work Type: Feature
- Outcome: Completed
- Difficulty: Hard
- Tags: Spring Boot,JWT,PostgreSQL

### **4. View Analytics**
- See success rate
- Check coding streaks
- View time distribution

---

## 💡 TIPS

### **Development**
- Frontend runs on port 3000
- Backend runs on port 8080
- Hot reload enabled (changes update automatically)

### **Testing**
- Create multiple tasks with different statuses
- Log sessions on different dates
- Check analytics updates in real-time

### **Production**
- Build with `npm run build`
- Deploy to Vercel
- Set REACT_APP_API_URL to Railway backend

---

## ✅ SUCCESS CHECKLIST

- [ ] Backend running on Docker
- [ ] Frontend installed (`npm install`)
- [ ] .env file created with backend URL
- [ ] Frontend started (`npm start`)
- [ ] Registered new account
- [ ] Created task successfully
- [ ] Logged session successfully
- [ ] Analytics showing data

**When all checked, you're ready to deploy!** 🎉

---

## 🐥 YOU'RE DONE!

Frontend is **complete** and **ready**.

**Next steps:**
1. Test locally (follow checklist above)
2. Deploy backend to Railway
3. Deploy frontend to Vercel
4. Test production setup

**Everything works. Everything is ready. Go build!** 🚀
