import axios from 'axios';

// Detectar se está acessando por IP local ou localhost
const getBaseURL = () => {
  if (typeof window !== 'undefined') {
    const hostname = window.location.hostname;
    
    // Se acessando por IP (ex: 192.168.1.73), usar o mesmo IP para API
    if (hostname !== 'localhost' && hostname !== '127.0.0.1') {
      return `http://${hostname}:8080/api`;
    }
  }
  
  // Fallback para localhost
  return process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';
};

const api = axios.create({
  baseURL: getBaseURL(),
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;