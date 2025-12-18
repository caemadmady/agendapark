import React, { useState } from "react";
import "../styles/CitasPendientes.css";
import lupacitapend from "../../../assets/lupa.png";

export default function CitasPendientes({
  reservations = [],
  q,
  setQ,
  onQuickAction
}) {
  const [showModal, setShowModal] = useState(false);
  const [selected, setSelected] = useState(null);

  // Abrir modal y pasar la reserva seleccionada
  const openCancelModal = (reserva) => {
    setSelected(reserva);
    setShowModal(true);
  };

  const confirmCancel = () => {
    if (!selected) return;
    onQuickAction(selected.id, "canceled");
    setShowModal(false);
  };

  // 🔎 Filtro: SOLO reservas SOLICITADAS + búsqueda
  const filtered = reservations
    .filter(r => r.status === "SOLICITADA")
    .filter(r => {
      const term = q.toLowerCase();
      if (!term) return true;
      return (
        (r.talentName || "").toLowerCase().includes(term) ||
        (r.projectName || "").toLowerCase().includes(term)
      );
    });

  return (
    <div className="xp-pend-card">
      {/* ENCABEZADO */}
      <div className="xp-pend-header">
        <h3 className="xp-pend-title">Citas Pendientes</h3>

        <div className="xp-search-box">
          <img src={lupacitapend} alt="Buscar" className="xp-search-icon" />
          <input
            type="text"
            placeholder="Buscar por nombre o proyecto..."
            value={q}
            onChange={(e) => setQ(e.target.value)}
            className="xp-search-input"
          />
        </div>
      </div>

      {/* TABLA */}
      <div className="xp-pend-table-wrapper">
        <table className="xp-pend-table">
          <thead>
            <tr>
              <th>Talento</th>
              <th>Proyecto</th>
              <th>Fecha</th>
              <th>Hora</th>
              <th>Equipos</th>
              <th>Acciones</th>
            </tr>
          </thead>

          <tbody>
            {filtered.length === 0 ? (
              <tr>
                <td colSpan="6" className="xp-pend-empty">
                  No hay citas solicitadas
                </td>
              </tr>
            ) : (
              filtered.map((r) => {
                const start = new Date(r.start);
                const end = new Date(r.end);

                return (
                  <tr key={r.id}>
                    {/* Talento */}
                    <td>{r.talentName}</td>

                    {/* Proyecto */}
                    <td>{r.projectName || "—"}</td>

                    {/* Fecha */}
                    <td>{start.toLocaleDateString("es-CO")}</td>

                    {/* Hora */}
                    <td>
                      {start.toLocaleTimeString("es-CO", {
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                      {" - "}
                      {end.toLocaleTimeString("es-CO", {
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </td>

                    {/* Equipos (no hay info en el endpoint, así que por ahora texto fijo) */}
                    <td>No aplica</td>

                    {/* Acciones */}
                    <td>
                      <button
                        className="xp-btn xp-confirm"
                        onClick={() => onQuickAction(r.id, "confirmed")}
                      >
                        Confirmar
                      </button>

                      <button
                        className="xp-btn xp-cancel"
                        onClick={() => openCancelModal(r)}
                      >
                        Cancelar
                      </button>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>

      {/* MODAL DE CONFIRMACIÓN */}
      {showModal && selected && (
        <div className="xp-modal-overlay">
          <div className="xp-modal-box">
            <h3>¿Cancelar esta cita?</h3>

            <div className="xp-modal-mini">
              <p><strong>Talento:</strong> {selected.talentName}</p>
              <p><strong>Proyecto:</strong> {selected.projectName || "—"}</p>
              <p>
                <strong>Fecha:</strong>{" "}
                {new Date(selected.start).toLocaleDateString("es-CO")}
              </p>
              <p>
                <strong>Hora:</strong>{" "}
                {new Date(selected.start).toLocaleTimeString("es-CO", {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </p>
            </div>

            <div className="xp-modal-actions">
              <button className="xp-btn xp-confirm" onClick={confirmCancel}>
                Sí, cancelar
              </button>

              <button
                className="xp-btn xp-cancel"
                onClick={() => setShowModal(false)}
              >
                No, volver
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
