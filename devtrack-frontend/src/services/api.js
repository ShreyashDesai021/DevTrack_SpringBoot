import axios from 'axios';

// Base URL
const API_BASE_URL =
  process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Axios instance
const api = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach token automatically
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

// AUTH
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
};

// TASK
export const taskAPI = {
  getAll: () => api.get('/tasks'),
};

// SESSION
export const sessionAPI = {
  getAll: () => api.get('/sessions'),
};

// ANALYTICS
export const analyticsAPI = {
  get: () => api.get('/analytics'),
};

export default api;
