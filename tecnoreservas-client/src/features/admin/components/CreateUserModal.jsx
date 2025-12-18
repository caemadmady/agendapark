import React, { useState, useEffect } from "react";
import { FaUserPlus, FaTimes } from "react-icons/fa";
import "../styles/CreateUserModal.css";
import { createUser, getAllLines } from "../services/admin.api";

const CreateUserModal = ({ isOpen, onClose, onUserCreated, lines }) => {
  const [formData, setFormData] = useState({
    name: "", lastName: "", email: "", username: "", password: "",
    userRole: "Talento", projectLine: ""
  });
  const [loadingLines, setLoadingLines] = useState(false);
  const [saving, setSaving] = useState(false);

  const roleIsExperto = formData.userRole === "Experto";

  useEffect(() => {
    if (isOpen) {
      setFormData({
        name: "", lastName: "", email: "", username: "", password: "",
        userRole: "Talento", projectLine: ""
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
    if (name === "userRole" && value !== "Experto") {
      setFormData((p) => ({ ...p, userRole: value, projectLine: "" }));
      return;
    }
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (roleIsExperto && !formData.projectLine) {
      alert("Selecciona la línea de proyecto para el rol Experto.");
      return;
    }
    try {
      setSaving(true);
      const created = await createUser(formData);
      onUserCreated?.(created);
      onClose();
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
            <label htmlFor="lastName">Apellidos</label>
            <input id="lastName" name="lastName" value={formData.lastName}
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
            <label htmlFor="role">Rol</label>
            <select id="role" name="userRole" value={formData.role} onChange={handleChange}>
              <option value="Talento">Talento</option>
              <option value="Experto">Experto</option>
              <option value="Recepción">Recepción</option>
              <option value="Administrador">Administrador</option>
            </select>
          </div>

          {roleIsExperto && (
            <div className="form-group">
              <label htmlFor="projectLine">Línea de Proyecto *</label>
              <select id="projectLine" name="projectLine" value={formData.projectLine}
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