import React, { useEffect, useMemo, useState } from 'react';
import { FaChartBar } from 'react-icons/fa';
import StatItem from './StatItem';
import { StatisticsModal } from './StatisticsModal';
import '../styles/AdminStatistics.css';
import { useOutletContext } from 'react-router-dom';
import { Spinner } from './Spinner';
import Button from './button/Button';

export const AdminStatistics = () => {

  const { talents, experts, isLoading, appointments: allAppointments, lines } = useOutletContext();
  const [selectedLineId, setSelectedLineId] = useState(null);
  const [displayType, setDisplayType] = useState(null); // 'talents', 'experts', 'appointments'
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [selectedItemType, setSelectedItemType] = useState(null);

  useEffect(() => {
    console.log("lineas actualizadas:", lines)
    console.log("Citas actualizadas:", allAppointments)
    console.log("Talentos:", talents)
    console.log("Expertos:", experts);
  }, [lines]);

  const handleTalentsByLine = (lineId) => {
    setSelectedLineId(lineId);
    setDisplayType('talents');
  };

  const handleExpertsByLine = (lineId) => {
    setSelectedLineId(lineId);
    setDisplayType('experts');
  };

  const handleAppointmentsByLine = (lineId) => {
    // Filtra las citas por línea, intentando con diferentes nombres de propiedad
    const filteredAppointments = allAppointments?.filter(appointment => appointment.serviceLineId === lineId);      
    
    // Abre el modal incluso si no hay citas
    setSelectedItem(filteredAppointments || []);
    setSelectedItemType('appointments');
    setIsModalOpen(true);
  };

  const handleSelectItem = (item, type) => {
    setSelectedItem(item);
    setSelectedItemType(type);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedItem(null);
    setSelectedItemType(null);
  };

  return (
    <>
    <div className="admin-statistics-container card-style">
      <header className="management-header">
        <h4><FaChartBar /> Estadísticas por Línea de Proyecto</h4>
      </header>

      <div className="statistics-content">
        {lines?.length && !isLoading ?
          (lines.map((line, idx) => (
            <section key={line.id} className={`line-section-card bg-gradient-${line.id}`}>
              <header className="line-header">
                <h5 className="line-title" style={line.id === 2 ? {minWidth: "300px"} : {}}>{line.name}</h5>
                
                {/* Contenedor de Acciones para la alineación vertical perfecta */}
                <div className="line-actions-container"> 
                    <Button className='btn-statistics' onClick={() => handleTalentsByLine(line.id)}>Talentos</Button>
                    <Button className='btn-statistics' onClick={() => handleExpertsByLine(line.id)}>Expertos</Button>
                    <Button className='btn-statistics' onClick={() => handleAppointmentsByLine(line.id)}>Total Reservas</Button>
                </div>

                <span className="line-key-badge">{`Linea # ${idx + 1}`}</span>
              </header>

              {/**Aqui va a renderizar el componente StatItem con la lista de todos los talentos, expertos y reservas para la línea actual que se renderiza cuando se da click en los botones correspondientes */}
              <div className="stats-items-container">
                {selectedLineId === line.id && displayType === 'talents' && (
                  <StatItem 
                    data={talents?.filter(talent => talent.lineProjectId === line.id)} 
                    type="talents"
                    onSelectItem={handleSelectItem}
                  />
                )}
                {selectedLineId === line.id && displayType === 'experts' && (
                  <StatItem 
                    data={experts?.filter(expert => expert.lineId === line.id)} 
                    type="experts"
                    onSelectItem={handleSelectItem}
                  />
                )}                
              </div>
               
            </section>
          ))) : (
            <div className="no-appointments-container">
              <Spinner />
            </div>)}
      </div>
    </div>
    <StatisticsModal 
      isOpen={isModalOpen} 
      onClose={handleCloseModal} 
      statisticsData={selectedItem} 
      itemType={selectedItemType}
    />

    </>
  );
};

