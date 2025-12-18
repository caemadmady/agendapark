// main.jsx
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

// CSS Globales
import "./styles/variables.css"

// IMPORTA AuthProvider
import AuthProvider from "./auth/AuthContext.jsx";
console.log("API BASE:", import.meta.env.VITE_API_BASE_URL);
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <App/>
    </AuthProvider>
  </StrictMode>

);
