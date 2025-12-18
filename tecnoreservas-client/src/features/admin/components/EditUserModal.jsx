import React, { useState, useEffect } from "react";
import { FaUserEdit, FaTimes } from "react-icons/fa";
import "../styles/CreateUserModal.css";
import { getAllLines, updateUser } from "../services/admin.api.jsx";

const EditUserModal = ({ isOpen, onClose, userToEdit, onUserUpdated }) => {
  const [formData, setFormData] = useState({
    name: "", email: "", username: "", password: "",
    role: "Talento", projectLine: ""
  });
  const [lines, setLines] = useState([]);
  const [loadingLines, setLoadingLines] = useState(false);
  const [saving, setSaving] = useState(false);

  const roleIsExperto = formData.role === "Experto";

  useEffect(() => {
    if (isOpen && userToEdit) {
      setFormData({
        name: userToEdit.name ?? "",
        email: userToEdit.email ?? "",
        username: userToEdit.username ?? "",
        password: "",
        role: userToEdit.role ?? "Talento",
        projectLine: userToEdit.linea?.id ?? userToEdit.line ?? userToEdit.serviceLineId ?? "",
      });
    }
  }, [isOpen, userToEdit]);

  useEffect(() => {
    if (!isOpen || !roleIsExperto || lines.length) return;
    (async () => {
      try {
        setLoadingLines(true);
        const data = await getAllLines();
        setLines(data || []);
      } catch (e) {
        console.error("Error cargando líneas:", e);
        setLines([]);
      } finally {
        setLoadingLines(false);
      }
    })();
  }, [isOpen, roleIsExperto, lines.length]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "role" && value !== "Experto") {
      setFormData((p) => ({ ...p, role: value, projectLine: "" }));
      return;
    }
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!userToEdit?.id) return;
    if (roleIsExperto && !formData.projectLine) {
      alert("Selecciona la línea de proyecto para el rol Experto.");
      return;
    }
    try {
      setSaving(true);
      const updated = await updateUser(userToEdit.id, formData);
      onUserUpdated?.(userToEdit.id, updated);
      onClose();
    } catch (err) {
      console.error("Error actualizando usuario:", err);
      alert(err?.response?.data?.message || "No se pudo actualizar el usuario.");
    } finally {
      setSaving(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <header className="modal-header">
          <h3><FaUserEdit /> Editar Usuario</h3>
          <button onClick={onClose} className="close-button"><FaTimes /></button>
        </header>

        <form onSubmit={handleSubmit} className="modal-form">
          <div className="form-group">
            <label htmlFor="edit-name">Nombre Completo</label>
            <input id="edit-name" name="name" value={formData.name}
              onChange={handleChange} required type="text" />
          </div>

          <div className="form-group">
            <label htmlFor="edit-email">Email</label>
            <input id="edit-email" name="email" value={formData.email}
              onChange={handleChange} required type="email" />
          </div>

          <div className="form-group">
            <label htmlFor="edit-username">Usuario</label>
            <input id="edit-username" name="username" value={formData.username}
              onChange={handleChange} required type="text" />
          </div>

          <div className="form-group">
            <label htmlFor="edit-password">Contraseña</label>
            <input id="edit-password" name="password" value={formData.password}
              onChange={handleChange} placeholder="Dejar en blanco para no cambiar"
              type="password" autoComplete="new-password" />
          </div>

          <div className="form-group">
            <label htmlFor="edit-role">Rol</label>
            <select id="edit-role" name="role" value={formData.role} onChange={handleChange}>
              <option value="Talento">Talento</option>
              <option value="Experto">Experto</option>
              <option value="Recepción">Recepción</option>
              <option value="Administrador">Administrador</option>
            </select>
          </div>

          {roleIsExperto && (
            <div className="form-group">
              <label htmlFor="edit-projectLine">Línea de Proyecto *</label>
              <select id="edit-projectLine" name="projectLine" value={formData.projectLine}
                onChange={handleChange} required disabled={loadingLines}>
                <option value="" disabled>
                  {loadingLines ? "Cargando líneas..." : "Seleccionar línea"}
                </option>
                {lines.map((l) => (
                  <option key={l.id} value={l.id}>{l.label}</option>
                ))}
              </select>
            </div>
          )}

          <footer className="modal-footer">
            <button type="button" className="btn-secondary" onClick={onClose} disabled={saving}>
              Cancelar
            </button>
            <button type="submit" className="btn-primary" disabled={saving}>
              {saving ? "Actualizando..." : "Actualizar Usuario"}
            </button>
          </footer>
        </form>
      </div>
    </div>
  );
};

export default EditUserModal;