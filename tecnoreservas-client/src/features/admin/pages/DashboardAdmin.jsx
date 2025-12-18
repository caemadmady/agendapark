import React, {  useEffect } from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { FaUsers, FaCalendarAlt, FaChartBar } from 'react-icons/fa';
import { useAdmin } from '../hooks/useAdmin';

// Importamos los estilos desde la carpeta centralizada
import '../../../features/admin/styles/DashboardAdmin.css';
import { getTalent, getExpert, getAdminAppointments, getAllLines } from '../services/admin.api';

const DashboardAdmin = () => {
  const { talents, loadTalents, 
          experts, loadExperts,
          totalUsers, setTotalUsers,
          appointments, setAppointments,
          lines, setLines,
          isLoading, setIsLoading } = useAdmin();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const talents = await getTalent();
        const experts = await getExpert();
        const lines = await getAllLines();

        //asignar roles a los usuarios
        const updateTalents = talents.map(talent => ({ ...talent, role: 'Talento' }));
        const updateExperts = experts.map(expert => ({ ...expert, role: 'Experto' }));
        // asignar la linea a cada reserva
        
        const appointments = await getAdminAppointments();
        setAppointments(appointments);
        loadTalents(updateTalents);
        loadExperts(updateExperts);
        setTotalUsers(updateTalents.length + updateExperts.length);
        setLines(lines);
      } catch (err) {
        console.error("Error cargando talentos:", err);
        loadTalents([]);
      }
    };
    fetchData();
  }, [loadTalents, loadExperts, setTotalUsers, setAppointments]);

  useEffect(() => {
    totalUsers !== 0 ? setIsLoading(false) : setIsLoading(true);
  }, [totalUsers, setIsLoading, appointments]);

  return (
    <div className="dashboard-container">
     
      {/* Pestañas de Navegación */}
      <section className="tabs-section-admin">
        <NavLink to="/admin/users" className="tab-button-admin">
          <FaUsers /> Usuarios
        </NavLink>
        <NavLink to="/admin/appointments" className="tab-button-admin">
          <FaCalendarAlt /> Citas
        </NavLink>
        <NavLink to="/admin/statistics" className="tab-button-admin">
          <FaChartBar /> Estadísticas
        </NavLink>
      </section>
      
      {/* Contenido dinámico (UserManagement, etc.) se renderiza aquí */}
      <main className="admin-content-area">
        <Outlet context={{ talents, experts, appointments, lines, isLoading }} />
      </main>
    </div>
  );
};

export default DashboardAdmin;