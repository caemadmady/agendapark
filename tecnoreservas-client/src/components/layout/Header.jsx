// src/components/layout/Header.jsx
import { useNavigate } from 'react-router-dom';
import { FaUserCircle, FaSignOutAlt, FaSync } from 'react-icons/fa';
import { useAuth } from '../../auth/AuthContext.jsx';
import tecnoparqueLogo from '../../assets/diamantelogo.png';
import '../../styles/Header.css';

// Función faltante
const capitalize = (str) =>
  str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();

const Header = () => {
  const { me, logout, loadingMe } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  if (loadingMe) {
    return (
      <header className="app-header">
        <div className="header-left">
          <img src={tecnoparqueLogo} alt="Tecnoparque SENA Logo" className="header-logo" />
          <div className="header-title-container">
            <h1 className="header-title">Tecnoparque SENA</h1>
            <p className="header-subtitle">Sistema de Agendamiento</p>
          </div>
        </div>
        <div className="header-right">Cargando sesión...</div>
      </header>
    );
  }

  if (!me) return null;

  const roleLabel = me.role ? capitalize(me.role) : null;
  const displayName = me.name || me.username;

  return (
    <header className="app-header">
      <div className="header-left">
        <img src={tecnoparqueLogo} alt="Tecnoparque SENA Logo" className="header-logo" />
        <div className="header-title-container">
          <h1 className="header-title">Tecnoparque SENA</h1>
          <p className="header-subtitle">Sistema de Agendamiento</p>
        </div>
        {me.role && <span className="role-badgee">{me.role}</span>}
      </div>

      <div className="header-right">
        <div className="user-info">
          <FaUserCircle className="user-icon" />
          <span className="user-name">{displayName}</span>
        </div>

        {me.role === "ADMIN" && (
          <button
            className="header-action-button reset-db-btn"
            onClick={() => console.log("Reset DB triggered")}
          >
            <FaSync />
            <span className="btn-label">Reset DB</span>
          </button>
        )}

        <button className="logout-button" onClick={handleLogout}>
          <FaSignOutAlt className="logout-icon" />
          <span className="btn-label">Salir</span>
        </button>
      </div>
    </header>
  );
};

export default Header;
