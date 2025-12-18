import React, { useEffect, useState } from 'react';
import { FaCalendarAlt, FaClock, FaTag, FaTools, FaStickyNote } from 'react-icons/fa';
import '../styles/AdminAppointmentCard.css';
import { formatDateISO, formatTime12H, getAppointmentDuration } from '../../../lib/datetime';

const AdminAppointmentCard = ({ appointment, onConfirm, onCancel }) => {

  useEffect(() => {
  }, [appointment]);

  const isPending = appointment.status === 'SOLICITADA';
  const isConfirmed = appointment.status === 'CONFIRMADA';
  const isCancelled = appointment.status === 'INCUMPLIDA';
  const isCompleted = appointment.status === 'CUMPLIDA';

  return (
    <div className={`admin-appointment-card status-border-${appointment.status.toLowerCase()}`}>
      <div className="card-main-info">
        <div className="card-details">
          <h4 className="talent-name">{appointment.nameTalent}</h4>
          <p className="expert-name">con {appointment.nameExpert}</p>
          <div className="time-info">
            <span><FaCalendarAlt /> { formatDateISO(appointment.dateTimeStart) }</span>
            <span><FaClock /> { formatTime12H(appointment.dateTimeStart) } ({getAppointmentDuration(appointment.dateTimeStart, appointment.endDateTime)})</span>
            <span><FaTag /> {`Linea ${appointment.serviceLineId}`}</span>
          </div>
          <div className="extra-info">
            <p><FaTools /> <strong>Equipos:</strong></p>
            <div className="equipment-tags">
              {"equipo 1, equipo 2, equipo 3".split(", ").map((eq, idx) => (
                <span key={idx} className="equipment-tag">{eq}</span>
              ))}
            </div>
          </div>
          <div className="extra-info">
            <p><FaStickyNote /> <strong>Nombre del Proyecto:</strong> { appointment.associateProject}</p>
          </div>
        </div>

        <div className="card-actions">
          {/* Lógica condicional para mostrar botones/badges */}
          {isPending && <span className="status-badge badge-pending">Solicitada</span>}
          {isCancelled && <span className="status-badge badge-cancelled">Incumplida</span>}
          {isCompleted && <span className="status-badge badge-completed">Cumplida</span>}
          
          {isConfirmed && <span className="status-badge badge-confirmed">Confirmada</span>}          
        </div>
      </div>
    </div>
  );
};

export default AdminAppointmentCard;