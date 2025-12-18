// src/features/experto/components/TalentoModal.jsx
import React, { useState } from "react";
import "../styles/SidebarPanel.css";
import { createTalent } from "../services/talentos.service";

export default function TalentoModal({ open, setOpen, onCreated }) {
  const [form, setForm] = useState({
    name: "",
    lastname: "",
    email: "",
    username: "",
    password: "",
    associatedProject: "",
    projectPhase: "PLANEACION",
  });

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      await createTalent(form);

      alert("Talento creado exitosamente");
      onCreated?.();
      setOpen(false);

      setForm({
        name: "",
        lastname: "",
        email: "",
        username: "",
        password: "",
        associatedProject: "",
        projectPhase: "PLANEACION",
      });
    } catch (err) {
      console.error(err);
      alert("Error creando talento");
    }
  }

  // ⛔ si no está abierto, no se renderiza
  if (!open) return null;

  return (
    <div className="xp-modal-overlay" onClick={() => setOpen(false)}>
      <div
        className="xp-talent-modal"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="xp-talent-modal-title">Añadir Talento</h2>

        <form className="xp-talent-grid" onSubmit={handleSubmit}>
          {/* COLUMNA 1 */}
          <div className="xp-col">
            <div className="xp-field">
              <label>Nombre</label>
              <input
                name="name"
                value={form.name}
                onChange={handleChange}
                required
              />
            </div>

            <div className="xp-field">
              <label>Apellido</label>
              <input
                name="lastname"
                value={form.lastname}
                onChange={handleChange}
                required
              />
            </div>

            <div className="xp-field">
              <label>Email</label>
              <input
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          {/* COLUMNA 2 */}
          <div className="xp-col">
            <div className="xp-field">
              <label>Username</label>
              <input
                name="username"
                value={form.username}
                onChange={handleChange}
                required
              />
            </div>

            <div className="xp-field">
              <label>Contraseña</label>
              <input
                name="password"
                type="password"
                value={form.password}
                onChange={handleChange}
                required
              />
            </div>

            <div className="xp-field">
              <label>Proyecto Asociado</label>
              <input
                name="associatedProject"
                value={form.associatedProject}
                onChange={handleChange}
              />
            </div>

            <div className="xp-field">
              <label>Fase del proyecto</label>
              <select
                name="projectPhase"
                value={form.projectPhase}
                onChange={handleChange}
              >
                <option value="PLANEACION">Planeación</option>
                <option value="DISEÑO">Diseño</option>
                <option value="PROTOTIPO">Prototipo</option>
                <option value="PRUEBAS">Pruebas</option>
                <option value="IMPLEMENTACION">Implementación</option>
              </select>
            </div>
          </div>

          {/* BOTONES */}
          <div className="xp-btns">
            <button
              type="button"
              className="xp-btn-cancel"
              onClick={() => setOpen(false)}
            >
              Cancelar
            </button>

            <button type="submit" className="xp-btn-submit">
              Crear talento
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
