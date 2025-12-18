/* eslint-disable react-refresh/only-export-components */
import React, { createContext, useContext, useEffect, useState } from "react";
import { apiFetch, getToken, setToken as persistToken } from "../lib/api.js";
import { mapRole, mapLine } from "./auth.utils.js";

// ---- Contexto
const AuthContext = createContext(null);

// ---- Decodificar el JWT
function decodeToken(token) {
  try {
    const payload = JSON.parse(atob(token.split(".")[1])); // Decodifica el payload del JWT
    return payload;
  } catch (error) {
    console.error("Error al decodificar el token:", error);
    return null;
  }
}

// ---- Provider (default export)
function AuthProvider({ children }) {
  const [me, setMe] = useState(null);      // { name, username, role, linea }
  const [loadingMe, setLoadingMe] = useState(true);

  useEffect(() => {
    const userData = getToken(); 
    if (userData?.token) {
      const payload = decodeToken(userData.token);
      if (payload && payload.exp * 1000 > Date.now()) { // Verifica si el token no ha expirado
        setMe({
          name: userData.userDto.name,
          username: userData.userDto.username,
          role: mapRole(userData.userDto.userRole),
          linea: mapLine(userData.userDto),
        });
      } else {
        persistToken(null); // Elimina el userData si ha expirado
      }
    }
    setLoadingMe(false); // Finaliza la carga inicial
  }, []);

  const login = async ({ username, password }) => {
    const resp = await apiFetch("/login", { method: "POST", body: { username, password } });
    const userData = { token: resp?.token, userDto: resp?.userDto };
    console.log("userData:", userData);
    if (!userData.token) throw new Error("Respuesta de login inválida (sin token).");
    if (!userData.userDto) throw new Error("Respuesta de login inválida (sin userDto).");

    persistToken(userData); // Guarda el token en localStorage
    const user = {
        name: userData.userDto.name,
        username: userData.userDto.username,
        role: mapRole(userData.userDto.userRole),
        linea: mapLine(userData.userDto),
    };
    console.log("Usuario autenticado:", user);
    setMe(user); // Actualiza el estado
    return user; 
  };

  const logout = () => {
    persistToken(null); // Elimina el token de localStorage
    setMe(null); // Limpia el estado del usuario
  };

  return (
    <AuthContext.Provider value={{ me, loadingMe, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export default AuthProvider;
export { AuthContext };

// ---- Hook para usar el contexto de autenticación
export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth debe usarse dentro de <AuthProvider>");
  return ctx;
};