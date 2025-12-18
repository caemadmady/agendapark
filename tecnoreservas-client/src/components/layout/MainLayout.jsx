
import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from './Header'; 
import '../../styles/MainLayout.css';

const MainLayout = () => {
  // Datos de prueba para el usuario. En el futuro, esto vendrá del estado de autenticación.
  const mockUser = {
    name: 'Ana Rodríguez',
  };
  const userRole = 'Recepción';

  return (
    <div className="app-layout">
      {/* Usamos el componente Header y le pasamos los datos */}
      <Header user={mockUser} role={userRole} />
      
      <main className="app-content">
        <Outlet /> 
      </main>
    </div>
  );
};

export default MainLayout;