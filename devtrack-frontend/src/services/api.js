import axios from 'axios';

// ✅ Base URL (works for local + production)
const API_BASE_URL =
  process.env.REACT_APP_API_URL || 'http://localhost:8080';

// ✅ Create Axios instance
const api = axios.create({
  baseURL: `${API_BASE_URL}/api`, // always include /api here
  headers: {
    'Content-Type': 'application/json',
  },
});

// ✅ Attach JWT token automatically
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ================= AUTH API =================
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
};

// ================= TASK API =================
export const taskAPI = {
  getAll: () => api.get('/tasks'),
  getById: (id) => api.get(`/tasks/${id}`),
  create: (data) => api.post('/tasks', data),
  update: (id, data) => api.put(`/tasks/${id}`, data),
  delete: (id) => api.delete(`/tasks/${id}`),
};

// ================= SESSION API =================
export const sessionAPI = {
  getAll: () => api.get('/sessions'),
  getById: (id) => api.get(`/sessions/${id}`),
  create: (data) => api.post('/sessions', data),
  delete: (id) => api.delete(`/sessions/${id}`),
};

// ================= ANALYTICS API =================
export const analyticsAPI = {
  get: () => api.get('/analytics'),
};

// ✅ Export default instance
export default api;
