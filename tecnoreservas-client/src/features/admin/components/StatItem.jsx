import React, { useState, useEffect } from 'react';
import '../styles/AdminStatistics.css';

const StatItem = ({ data, type, onSelectItem }) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    // Pequeño delay para asegurar que el componente se renderice primero
    const timer = setTimeout(() => setIsVisible(true), 10);
    return () => clearTimeout(timer);
  }, [data, type]);

  const handleItemClick = (item) => {
    if (onSelectItem) {
      onSelectItem(item, type);
    }
  };

  if (!data || data.length === 0) {
    return <div className="stat-item-card">No hay datos disponibles</div>;
  }

  return (
    <div className={`stat-item-list ${!isVisible ? 'fade-out' : ''}`}>
      <h6 className="stat-list-title">
        {type === 'talents' && 'Talentos'}
        {type === 'experts' && 'Expertos'}
        {type === 'appointments' && 'Reservas'}
      </h6>
      <ul className="stat-list">
        {data.map((item) => (
          <li 
            key={item.id} 
            className="stat-list-item"
            onClick={() => handleItemClick(item)}
          >
            {item.name || item.username || 'Sin nombre'}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default StatItem;