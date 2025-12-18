// src/features/recepcion/components/AppointmentCard.jsx
import React from 'react';
import { FaRegCalendarAlt, FaRegClock, FaCog, FaUser, FaUserTie, FaCheckCircle } from 'react-icons/fa';
import '../styles/AppointmentCard.css';

const AppointmentCard = ({ appointment }) => {
  const { status, line, date, time, duration, talentName, expertName, equipment } = appointment;

  return (
    <div className="appointment-card">
      <header className="appointment-card-header">
        <span className="badge status-badge">
          <FaCheckCircle /> {status}
        </span>
        <span className="badge line-badge">{line}</span>
      </header>

      <div className="appointment-card-body">
        <div className="details-column">
          <p className="detail-item"><FaRegCalendarAlt className="detail-icon" /> {date}</p>
          <p className="detail-item"><FaRegClock className="detail-icon" /> {time} ({duration})</p>
        </div>
        <div className="details-column people-details">
          <p className="detail-item"><FaUser className="detail-icon" /> Talento: {talentName}</p>
          <p className="detail-item"><FaUserTie className="detail-icon" /> Experto: {expertName}</p>
        </div>
      </div>
    </div>
  );
};

export default AppointmentCard;