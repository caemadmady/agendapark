import React from 'react';
import '../../styles/StatCard.css';

// El componente vuelve a recibir 'icon' y 'color' como props
const StatCard = ({ icon, value, label, color }) => {
  
  // Estilos dinámicos para el fondo y el borde de la tarjeta
  const cardStyle = {
    background: `linear-gradient(135deg, ${color}1A, #ffffff)`, // Usa el color con baja opacidad para el gradiente
    borderColor: `${color}99` // Usa el color con opacidad media para el borde
  };

  // Estilos dinámicos para el ícono
  const iconStyle = {
    background: `linear-gradient(135deg, ${color}, ${color}B3)`, // Gradiente sólido para el ícono
    color: 'white'
  };

  // Estilos para el texto, usando el color base
  const textStyle = {
    color: color
  }

  return (
    <div className="stat-card" style={cardStyle}>
      <div className="stat-card-icon" style={iconStyle}>
        {icon}
      </div>
      <div className="stat-card-info">
        <span className="stat-card-value">{value}</span>
        <span className="stat-card-label" style={textStyle}>{label}</span>
      </div>
    </div>
  );
};

export default StatCard;