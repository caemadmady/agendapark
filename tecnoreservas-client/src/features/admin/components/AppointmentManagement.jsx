import React, { useEffect, useState } from "react";
import { FaCalendarAlt } from "react-icons/fa";
import AdminAppointmentCard from "./AdminAppointmentCard";
import "../styles/AppointmentManagement.css";
import {  updateAppointmentStatus } from "../services/admin.api.jsx";
import { useOutletContext } from "react-router-dom";
import { SpinnerAp } from "./SpinnerAp";
import { Spinner } from "./Spinner";

const AppointmentManagement = () => {
  const { talents, experts, isLoading, appointments: allAppointments } = useOutletContext();
  const [appointments, setAppointments] = useState([]);

  useEffect(() => {/* 
    console.log("Citas actualizadas:", allAppointments)
    console.log("Talentos:", talents)
    console.log("Expertos:", experts); */
  }, [allAppointments, talents, experts]);

  const handleConfirm = async (appointmentId) => {
    try {
      await updateAppointmentStatus(appointmentId, "CONFIRMADA");
      setAppointments((prev) =>
        prev.map((apt) =>
          apt.id === appointmentId ? { ...apt, status: "CONFIRMADA" } : apt
        )
      );
    } catch (err) {
      console.error("Error al confirmar cita:", err);
    }
  };

  const handleCancel = async (appointmentId) => {
    try {
      await updateAppointmentStatus(appointmentId, "Cancelada");
      setAppointments((prev) =>
        prev.map((apt) =>
          apt.id === appointmentId ? { ...apt, status: "Cancelada" } : apt
        )
      );
    } catch (err) {
      console.error("Error al cancelar cita:", err);
    }
  };

  return (
    <div className="appointment-management-container">
      <header className="management-header card-style">
        <h4><FaCalendarAlt /> Todas las Citas</h4>
      </header>

      <div className="appointment-list">
        {allAppointments?.length && !isLoading ? (
          allAppointments.map((apt, index) => (
            <AdminAppointmentCard
              key={index}
              appointment={apt}
              onConfirm={handleConfirm}
              onCancel={handleCancel}
            />
          ))
        ) : (
          <div className="no-appointments-container">
            <Spinner />
          </div>
        )}
      </div>
    </div>
  );
};

export default AppointmentManagement;