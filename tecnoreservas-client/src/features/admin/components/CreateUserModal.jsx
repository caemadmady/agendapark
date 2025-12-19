import React, { useState, useEffect } from "react";
import { FaUserPlus, FaTimes } from "react-icons/fa";
import "../styles/CreateUserModal.css";
import { createExpert, createUser } from "../services/admin.api";

const CreateUserModal = ({ isOpen, onClose, onUserCreated, lines }) => {
  const [formData, setFormData] = useState({
    name: "", lastname: "", email: "", username: "", password: "",
    userRole: "", line: ""
  });
  const [loadingLines, setLoadingLines] = useState(false);
  const [saving, setSaving] = useState(false);

  const roleIsExperto = formData.userRole === "EXPERT";

  useEffect(() => {
    if (isOpen) {
      setFormData({
        name: "", lastname: "", email: "", username: "", password: "",
        userRole: "", line: ""
      });
    }
  }, [isOpen]);

  useEffect(() => {
    if (!isOpen || !roleIsExperto || lines.length) return;
    (async () => {
      try {
        setLoadingLines(true);
      } catch (e) {
        console.error("Error cargando líneas:", e);
      } finally {
        setLoadingLines(false);
      }
    })();
  }, [isOpen, roleIsExperto, lines.length]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "userRole" && value !== "EXPERT") {
      setFormData((p) => ({ ...p, userRole: value }));
      return;
    }
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (roleIsExperto && !formData.line) {
      alert("Selecciona la línea de proyecto para el rol Experto.");
      return;
    }
    try {
      setSaving(true);

      // Crear una copia de formData sin la propiedad line si no es Experto
      const dataToSend = roleIsExperto ? formData : (() => {
        const { line, ...rest } = formData;
        return rest;
      })();

      if (dataToSend.userRole === "EXPERT") {
        // Llamar a la función de creación de experto
        const created = await createExpert(dataToSend);
        console.log("Usuario experto creado:", created);
        onUserCreated?.(created);
        onClose();
      }
      if (dataToSend.userRole === "TALENT") {
        // Llamar a la función de creación de talento
        alert("Lo siento, la creación de talentos solo es posible mediante el experto asignado.");
        onClose();
      }
      if (dataToSend.userRole === "SUPERADMIN" || dataToSend.userRole === "SECURITY") {
        const created = await createUser(dataToSend);
        console.log("Usuario creado:", created);
        onUserCreated?.(created);
        onClose();
      }

    } catch (err) {
      console.error("Error creando usuario:", err);
      alert(err?.response?.data?.message || "No se pudo crear el usuario.");
    } finally {
      setSaving(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <header className="modal-header">
          <h3><FaUserPlus /> Nuevo Usuario</h3>
          <button onClick={onClose} className="close-button"><FaTimes /></button>
        </header>

        <form onSubmit={handleSubmit} className="modal-form">
          <div className="form-group">
            <label htmlFor="name">Nombres</label>
            <input id="name" name="name" value={formData.name}
              onChange={handleChange} placeholder="Nombre completo" required type="text" />
          </div>

          <div className="form-group">
            <label htmlFor="lastname">Apellidos</label>
            <input id="lastname" name="lastname" value={formData.lastname}
              onChange={handleChange} placeholder="Apellido completo" required type="text" />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input id="email" name="email" value={formData.email}
              onChange={handleChange} placeholder="correo@ejemplo.com" required type="email" />
          </div>

          <div className="form-group">
            <label htmlFor="username">Usuario</label>
            <input id="username" name="username" value={formData.username}
              onChange={handleChange} placeholder="usuario" required type="text" />
          </div>

          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <input id="password" name="password" value={formData.password}
              onChange={handleChange} placeholder="contraseña" required type="password" autoComplete="new-password" />
          </div>

          <div className="form-group">
            <label htmlFor="userRole">Rol</label>
            <select id="userRole" name="userRole" value={formData.userRole} onChange={handleChange}>
              <option value="TALENT">Talento</option>
              <option value="EXPERT">Experto</option>
              <option value="SECURITY">Recepción</option>
              <option value="SUPERADMIN">Administrador</option>
            </select>
          </div>

          {roleIsExperto && (
            <div className="form-group">
              <label htmlFor="line">Línea de Proyecto *</label>
              <select id="line" name="line" value={formData.line}
                onChange={handleChange} required disabled={loadingLines}>
                <option value="" disabled>
                  {loadingLines ? "Cargando líneas..." : "Seleccionar línea"}
                </option>
                {lines.map((l) => (
                  <option key={l.id} value={l.id}>{l.name}</option>
                ))}
              </select>
            </div>
          )}

          <footer className="modal-footer">
            <button type="button" className="btn-secondary" onClick={onClose} disabled={saving}>
              Cancelar
            </button>
            <button type="submit" className="btn-primary" disabled={saving}>
              {saving ? "Creando..." : "Crear Usuario"}
            </button>
          </footer>
        </form>
      </div>
    </div>
  );
};

export default CreateUserModal;