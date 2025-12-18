// src/features/experto/components/SidebarPanel.jsx
import React, { useState } from "react";
import "../styles/SidebarPanel.css";
import { Bell, Search } from "lucide-react";
import TalentoModal from "./TalentoModal";

export default function SidebarPanel({
  equipos = [],
  talentos = [],
  notificaciones = [],
  onTalentCreated,
}) {
  const [openNoti, setOpenNoti] = useState(false);
  const [openResourceModal, setOpenResourceModal] = useState(false); // futuro
  const [openTalentModal, setOpenTalentModal] = useState(false);

  const [searchEquipos, setSearchEquipos] = useState("");
  const [searchTalentos, setSearchTalentos] = useState("");
  const [activeFilter, setActiveFilter] = useState("TODOS");

  /* =========================================================
     FILTRADO DE EQUIPOS
  ========================================================= */
  const filteredEquipos = equipos.filter((eq) => {
    const texto = searchEquipos.toLowerCase();

    if (activeFilter !== "TODOS" && eq.status?.toUpperCase() !== activeFilter) {
      return false;
    }

    return eq.name?.toLowerCase().includes(texto);
  });

  /* =========================================================
     FILTRADO DE TALENTOS
  ========================================================= */
  const talentosFiltrados = talentos.filter((t) => {
    const texto = searchTalentos.toLowerCase();

    return (
      (t.name + " " + t.lastname).toLowerCase().includes(texto) ||
      t.email?.toLowerCase().includes(texto) ||
      t.username?.toLowerCase().includes(texto) ||
      t.associatedProject?.toLowerCase().includes(texto) ||
      t.projectPhase?.toLowerCase().includes(texto)
    );
  });

  return (
    <div className="xp-side-panel">
      {/* =========================================================
         TARJETA — NOTIFICACIONES
      ========================================================= */}
      <div className="xp-card">
        <div className="xp-noti-header">
          <h3 className="xp-card-title">Notificaciones</h3>

          <button className="xp-bell-btn" onClick={() => setOpenNoti(true)}>
            <Bell size={20} />
            {notificaciones.length > 0 && (
              <span className="xp-noti-badge">{notificaciones.length}</span>
            )}
          </button>
        </div>

        <div className="xp-scroll">
          {notificaciones.length === 0 ? (
            <p className="xp-empty">Sin notificaciones</p>
          ) : (
            notificaciones.map((n) => (
              <div key={n.id} className="xp-item">
                {n.message || n.mensaje}
              </div>
            ))
          )}
        </div>
      </div>

      {/* ========== MODAL NOTIFICACIONES ========== */}
      {openNoti && (
        <div className="xp-modal-overlay" onClick={() => setOpenNoti(false)}>
          <div className="xp-modal" onClick={(e) => e.stopPropagation()}>
            <h2 className="xp-modal-title">Notificaciones</h2>

            {notificaciones.length === 0 ? (
              <p className="xp-modal-empty">No tienes notificaciones</p>
            ) : (
              notificaciones.map((n) => (
                <div key={n.id} className="xp-modal-item">
                  {n.message || n.mensaje}
                </div>
              ))
            )}

            <button
              className="xp-modal-close"
              onClick={() => setOpenNoti(false)}
            >
              Cerrar
            </button>
          </div>
        </div>
      )}

      {/* =========================================================
         TARJETA — EQUIPOS DE LÍNEA
      ========================================================= */}
      <div className="xp-card">
        <div className="xp-equipos-top">
          <h3 className="xp-card-title">Equipos</h3>

          <div className="xp-equipos-search">
            <div className="xp-search-wrapper">
              <Search className="xp-search-icon" size={18} />
              <input
                type="text"
                placeholder="Buscar..."
                value={searchEquipos}
                onChange={(e) => setSearchEquipos(e.target.value)}
                className="xp-search"
              />
            </div>
          </div>

          <button
            className="xp-add-resource-btn"
            onClick={() => setOpenResourceModal(true)}
          >
            + Añadir
          </button>
        </div>

        <div className="xp-equipos-filtros">
          {[
            { label: "Disponibles", value: "DISPONIBLE", color: "green" },
            { label: "No Disponibles", value: "NO_DISPONIBLE", color: "red" },
            { label: "Compartido", value: "COMPARTIDO", color: "purple" },
            { label: "Mantenimiento", value: "MANTENIMIENTO", color: "orange" },
          ].map((f) => (
            <div
              key={f.value}
              className={`xp-pill ${activeFilter === f.value ? "active" : ""}`}
              onClick={() => setActiveFilter(f.value)}
            >
              <span className={`dot ${f.color}`} />
              {f.label}
            </div>
          ))}
        </div>

        <div className="xp-equipos-list">
          {filteredEquipos.length === 0 ? (
            <p className="xp-empty">No hay equipos en esta línea</p>
          ) : (
            filteredEquipos.map((eq) => (
              <div key={eq.id} className="xp-equipo-item">
                <div className="xp-equipo-left">
                  <span>{eq.name}</span>
                </div>

                <span className={`xp-equipo-status ${eq.status}`}>
                  {eq.status.replace("_", " ")}
                </span>
              </div>
            ))
          )}
        </div>
      </div>

      {/* =========================================================
         TARJETA — TALENTOS DE LA LÍNEA
      ========================================================= */}
      <div className="xp-card">
        <div className="xp-talents-top">
          <h3 className="xp-card-title">Talentos</h3>

          <div className="xp-talents-search">
            <div className="xp-search-wrapper">
              <Search className="xp-search-icon" size={18} />
              <input
                type="text"
                placeholder="Buscar..."
                value={searchTalentos}
                onChange={(e) => setSearchTalentos(e.target.value)}
                className="xp-search"
              />
            </div>
          </div>

          <button
            className="xp-add-resource-btn"
            onClick={() => setOpenTalentModal(true)}
          >
            + Añadir
          </button>
        </div>

        <div className="xp-scroll">
          {talentosFiltrados.length === 0 ? (
            <p className="xp-empty">Sin resultados</p>
          ) : (
            talentosFiltrados.map((t) => (
              <div
                key={t.id}
                className={`xp-talent-item ${
                  t.status !== "ACTIVO" ? "inactive" : ""
                }`}
              >
                <div className="xp-talent-main">
                  <span className="xp-talent-name">
                    {t.name} {t.lastname}
                  </span>

                  <span className="xp-talent-email">{t.email}</span>

                  <span className={`xp-status-tag ${t.status}`}>
                    {t.status}
                  </span>
                </div>

                <div className="xp-talent-project">
                  Proyecto: <strong>{t.associatedProject}</strong>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* =========================================================
         MODAL CREAR TALENTO
      ========================================================= */}
      {openTalentModal && (
        <TalentoModal
          open={openTalentModal}
          setOpen={setOpenTalentModal}
          onCreated={onTalentCreated}
        />
      )}
    </div>
  );
}
