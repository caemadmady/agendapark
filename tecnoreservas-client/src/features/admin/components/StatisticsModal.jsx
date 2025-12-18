import { useEffect, useState } from "react";
import "../styles/CreateUserModal.css";
import { FaChartBar, FaTimes } from 'react-icons/fa';
import LineChartComp from "./grafics/LineChartComp";

export const StatisticsModal = ({ isOpen, onClose, statisticsData, itemType }) => {
  if (!isOpen) return null;

  const getTitle = () => {
    switch(itemType) {
      case 'talents':
        return 'Talento';
      case 'experts':
        return 'Experto';
      case 'appointments':
        return 'Reservas';
      default:
        return 'Estadísticas';
    }
  };

  const isAppointmentList = itemType === 'appointments' && Array.isArray(statisticsData);

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <header className="modal-header">
          <h3>
            <FaChartBar /> 
            {getTitle()}
          </h3>
          <button onClick={onClose} className="close-button"><FaTimes /></button>
        </header>
        <div className="modal-body">
          {isAppointmentList ? (
            // Contenedor para diagramas/indicadores de reservas
            <div className="appointments-indicators">
              {/* Aquí irán los diagramas e indicadores */}
              <LineChartComp data={statisticsData}/>
              <p>Total de reservas: {statisticsData.length}</p>
            </div>
          ) : statisticsData ? (
            <div className="statistics-details">
              <p><strong>Nombre:</strong> {statisticsData.name || statisticsData.username || 'N/A'}</p>
              <p><strong>Correo:</strong> {statisticsData.email || 'N/A'}</p>
              {statisticsData.specialty && <p><strong>Especialidad:</strong> {statisticsData.specialty}</p>}
              {statisticsData.skills && <p><strong>Habilidades:</strong> {statisticsData.skills}</p>}
            </div>
          ) : (
            <p>No hay datos disponibles</p>
          )}
        </div>
      </div>
    </div>
  );
}