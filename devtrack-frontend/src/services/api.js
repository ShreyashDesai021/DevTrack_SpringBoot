import axios from 'axios';

// Base URL (Render + Local support)
const API_BASE_URL =
  process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Axios instance
const api = axios.create({
  baseURL: `${API_BASE_URL}/api`, // IMPORTANT
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach JWT token automatically
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

// ================= AUTH =================
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
};

// ================= TASK =================
export const taskAPI = {
  getAll: () => api.get('/tasks'),
  getById: (id) => api.get(`/tasks/${id}`),
  create: (data) => api.post('/tasks', data),
  update: (id, data) => api.put(`/tasks/${id}`, data),
  delete: (id) => api.delete(`/tasks/${id}`),
};

// ================= SESSION =================
export const sessionAPI = {
  getAll: () => api.get('/sessions'),
  getById: (id) => api.get(`/sessions/${id}`),
  create: (data) => api.post('/sessions', data),
  delete: (id) => api.delete(`/sessions/${id}`),
};

// ================= ANALYTICS =================
export const analyticsAPI = {
  get: () => api.get('/analytics'),
};

export default api;
