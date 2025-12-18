// src/routes/RecepcionRoutes.jsx
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import DashboardRecepcion from '../features/recepcion/pages/DashboardRecepcion';

export default function RecepcionRoutes() {
    console.log("🏢 RecepcionRoutes MONTADO"); // ✅ Log de debug

    return (
        <Routes>
            {/* Ruta principal del dashboard */}
            <Route path="/" element={<DashboardRecepcion />} />

            {/* Otras rutas de recepción si las tienes */}
            {/* <Route path="/citas" element={<CitasRecepcion />} /> */}

            {/* Redirección por defecto */}
            <Route path="*" element={<Navigate to="/recepcion" replace />} />
        </Routes>
    );
}