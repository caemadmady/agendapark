// Importamos los estilos desde la carpeta centralizada
import '../../../features/admin/styles/DashboardAdmin.css';
import { Spinner } from './Spinner';

// Componente para las tarjetas de estadísticas (diseño específico para el admin)
 export const AdminStatCard = ({ title, value, subtitle, icon, color, isLoading }) => (
  <div className={`admin-stat-card card-style card-border-${color}`}>
    <div className="card-info">
      <p className="card-title">{title}</p>
      <div className="card-value">{isLoading ? <Spinner /> : value}</div>
      <p className="card-subtitle">{isLoading ? "Por favor espera" : subtitle}</p>
    </div>
    <div className={`card-icon-wrapper icon-bg-${color}`}>{icon}</div>
  </div>
);