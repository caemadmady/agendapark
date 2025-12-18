// src/services/api.js
import axios from "axios";

// Base URL desde .env o proxy
const BASE =
  (import.meta.env.VITE_API_BASE_URL && import.meta.env.VITE_API_BASE_URL.replace(/\/+$/, "")) ||
  "/api";

// Helpers de token
export function getToken() {
  const data = localStorage.getItem("userData");
  return data ? JSON.parse(data) : null;
}
export function setToken(t) {
  if (t) localStorage.setItem("userData", JSON.stringify(t));
  else localStorage.removeItem("userData");
}

// Instancia Axios
const api = axios.create({ baseURL: BASE });

// Interceptor de request: agrega token
api.interceptors.request.use((config) => {
  const userData = getToken();
  if (userData?.token) config.headers.Authorization = `Bearer ${userData.token}`;
  return config;
});

// Interceptor de response: maneja 401
api.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error?.response?.status === 401) {
      // Limpiar token y disparar evento global en vez de redirigir directo
      setToken(null);
      localStorage.removeItem("role");
      window.dispatchEvent(new CustomEvent("logout"));
    }
    return Promise.reject(error);
  }
);

// Export default + named
export default api;
