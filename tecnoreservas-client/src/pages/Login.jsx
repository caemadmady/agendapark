import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";

import "../styles/login.css";

// Assets
import tecnoparqueLogo from "../assets/diamantelogo.png";
import Usuario from "../assets/usuario.png";
import Mostrar from "../assets/ver.png";
import Ocultar from "../assets/esconder.png";
import Ilustracion from "../assets/logotecnoo.png";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    if (!username.trim() || !password.trim()) {
      setError("Por favor completa todos los campos");
      return;
    }

    setLoading(true);

    try {
      const loggedInUser = await login({ username, password });

      // 🔥 Normalizamos el rol que devuelve el backend (por si llega en minúscula o con acento)
      const normalizedRole = (loggedInUser.role || "")
        .toUpperCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, ""); // elimina tildes

      // 🔒 Rutas seguras por rol
      const routesByRole = {
        ADMIN: "/admin",
        ADMINISTRADOR: "/admin",
        RECEPCION: "/recepcion",
        RECEPCIONISTA: "/recepcion",
        EXPERTO: "/experto",
        TALENTO: "/talento",
      };

      const path = routesByRole[normalizedRole] || "/login";
      navigate(path, { replace: true });
    } catch (err) {
      setError(err.message || "Usuario o contraseña incorrectos.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-wrapper">
      {/* Imagen lateral */}
      <div className="login-left">
        <img src={Ilustracion} alt="Tecnoparque" className="login-image" />
      </div>

      {/* Formulario */}
      <div className="login-page">
        <header className="login-branding">
          <div className="branding-logo-title">
            <img
              src={tecnoparqueLogo}
              alt="Logo Tecnoparque"
              className="branding-logo"
            />
            <div className="divider"></div>
            <div className="branding-text">
              <h1>TECNOPARQUE SENA</h1>
              <p>Nodo Bogotá</p>
            </div>
          </div>
          <h2>Sistema de Agendamiento de Citas y Equipos</h2>
        </header>

        <main className="login-card">
          <div className="login-head">
            <div className="login-brand">TECNOPARQUE SENA</div>
            <div className="login-sub">Sistema de Agendamiento</div>
          </div>

          <div className="login-title">
            <span className="icon">
              <img src={Usuario} alt="usuario" className="icono" />
            </span>
            Iniciar Sesión
          </div>

          <form onSubmit={handleLogin}>
            <div className="input-group">
              <label htmlFor="username">Usuario</label>
              <div className="input-field">
                <input
                  id="username"
                  type="text"
                  placeholder="Ingresa tu usuario"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                  disabled={loading}
                />
              </div>
            </div>

            <div className="input-group">
              <label htmlFor="password">Contraseña</label>
              <div className="input-field">
                <input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  placeholder="Ingresa tu contraseña"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  disabled={loading}
                />
                <button
                  type="button"
                  className="password-toggle-icon"
                  onClick={() => setShowPassword(!showPassword)}
                  aria-label={
                    showPassword ? "Ocultar contraseña" : "Mostrar contraseña"
                  }
                >
                  <img
                    src={showPassword ? Ocultar : Mostrar}
                    alt="toggle"
                    style={{ width: "1.5rem", height: "1.5rem", opacity: 0.6 }}
                  />
                </button>
              </div>
            </div>

            {error && <p className="error-message">{error}</p>}

            <button
              className="login-button"
              type="submit"
              disabled={loading}
            >
              {loading ? "Ingresando..." : "Ingresar"}
            </button>
          </form>
        </main>
      </div>
    </div>
  );
}