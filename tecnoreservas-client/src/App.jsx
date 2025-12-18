// src/App.jsx
import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AuthProvider from "./auth/AuthContext.jsx";
import Login from "./pages/Login.jsx";
import MainLayout from "./components/layout/MainLayout.jsx";
import RecepcionRoutes from "./routes/RecepcionRoutes.jsx";
import AdminRoutes from "./routes/AdminRoutes.jsx";
import ProtectedRoute from "./routes/ProtectedRoute.jsx";
import DashboardTalento from "./features/talento/pages/DeshboardTalento.jsx";
import DashboardExperto from "./features/experto/pages/DashboardExperto.jsx";

import "./App.css";

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* 🟢 Ruta pública de Login */}
          <Route path="/login" element={<Login />} />

          {/* 🧩 Rutas protegidas (MainLayout + vistas internas) */}
          <Route
            element={
              <ProtectedRoute>
                <MainLayout />
              </ProtectedRoute>
            }
          >
            {/* 🔒 Admin */}
            <Route
              path="/admin/*"
              element={
                <ProtectedRoute roles={["ADMIN", "ADMINISTRADOR"]}>
                  <AdminRoutes />
                </ProtectedRoute>
              }
            />

            {/* 🔒 Recepción */}
            <Route
              path="/recepcion/*"
              element={
                <ProtectedRoute roles={["SECURITY", "RECEPCION"]}>
                  <RecepcionRoutes />
                </ProtectedRoute>
              }
            />

            {/* 🔒 Talento */}
            <Route
              path="/talento"
              element={
                <ProtectedRoute roles={["TALENTO"]}>
                  <DashboardTalento />
                </ProtectedRoute>
              }
            />

            {/* 🔒 Experto */}
            <Route
              path="/experto"
              element={
                <ProtectedRoute roles={["EXPERTO"]}>
                  <DashboardExperto />
                </ProtectedRoute>
              }
            />
          </Route>

          {/* 🚪 Redirección por defecto */}
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}