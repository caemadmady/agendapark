import React, { useEffect, useState } from "react";
import { useAuth } from "../../../auth/AuthContext";
import "../styles/experto.css";

// Services
import {
  getExpertSession,
  getServiceLineById,
  LINE_IMAGES
} from "../services/experto.api";

import {
  getReservationsByLine,
  confirmReservation,
  cancelReservation,
  normalizeReservation,
  getStatusColor
} from "../services/reservas.service";

import { getResourcesByLine } from "../services/recursos.service";
import { getTalentsByLine } from "../services/talentos.service";

// Componentes UI
import CitasCalendario from "../components/CitasCalendario";
import CitasPendientes from "../components/CitasPendientes";
import SidebarPanel from "../components/SidebarPanel";

export default function DashboardExperto() {
  const { me } = useAuth(); // lo sigues teniendo por si quieres mostrar el nombre arriba

  const [expertData, setExpertData] = useState(null);
  const [lineaData, setLineaData] = useState(null);
  const [reservas, setReservas] = useState([]);

  const [equipos, setEquipos] = useState([]);
  const [talentos, setTalentos] = useState([]);
  const [calendarView, setCalendarView] = useState("dayGridMonth");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [q, setQ] = useState("");

    // ===========================================================
    // FUNCIÓN PARA PRÓXIMA CITA FUTURA
    // ===========================================================
    function getProximaCita(reservas) {
        const ahora = new Date();

        const futuras = reservas
            .filter(r => new Date(r.start) > ahora)
            .sort((a, b) => new Date(a.start) - new Date(b.start));

        if (futuras.length === 0) return null;

        return futuras[0];
    }

  // ===========================================================
  // CARGA INICIAL (NO dependemos de "me", sólo del token)
  // ===========================================================
  useEffect(() => {
    async function loadExpertData() {
      try {
        setLoading(true);
        setError("");

        // 1. EXPERTO EN SESIÓN
        const expert = await getExpertSession();
        console.log("Expert session:", expert);
        setExpertData(expert);

        const serviceLineId = expert?.lineId;
        console.log("📌 serviceLineId:", serviceLineId);

        if (!serviceLineId) {
          setLineaData(null);
          setError("El experto no tiene una línea asociada");
          return;
        }

        // 2. LÍNEA
        const lineaRes = await getServiceLineById(serviceLineId);
        console.log("Línea desde backend:", lineaRes);

        if (!lineaRes) {
          setLineaData(null);
          setError("No se pudo obtener la línea desde el backend");
          return;
        }

        setLineaData(lineaRes); // { id, name, raw }

        // 3. RESERVAS
        const reservasData = await getReservationsByLine(serviceLineId);
        console.log("Reservas por línea:", reservasData);

        const reservasNormalizadas = reservasData.map((r) => {
          const nr = normalizeReservation(r);

          return {
            ...nr,
            title: `${nr.talentName} - ${nr.expertName}`,
            backgroundColor: getStatusColor(nr.status),
            borderColor: getStatusColor(nr.status),
          };
        });

        setReservas(reservasNormalizadas);

        // 4. TALENTOS

        const talentosLinea = await getTalentsByLine(serviceLineId);
        setTalentos(talentosLinea);

        // 5. EQUIPOS
        const equiposList = await getResourcesByLine(serviceLineId);
        setEquipos(equiposList);
      } catch (err) {
        console.error("❌ Error en DashboardExperto:", err);
        setError("Error al cargar datos del experto");
      } finally {
        setLoading(false);
      }
    }

    // llamamos SIEMPRE al montar el componente
    loadExpertData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // 👈 sin dependencia de "me"

  // ===========================================================
  // ACCIONES RÁPIDAS (CONFIRMAR / CANCELAR)
  // ===========================================================
  async function handleQuickAction(id, action) {
    try {
      if (action === "confirmed") await confirmReservation(id);
      else if (action === "canceled") await cancelReservation(id);

      const serviceLineId = expertData?.lineId;

      if (serviceLineId) {
        const reservasData = await getReservationsByLine(serviceLineId);

        const mapped = reservasData.map((r) => {
          const nr = normalizeReservation(r);
          return {
            ...nr,
            title: `${nr.talentName} - ${nr.expertName}`,
            backgroundColor: getStatusColor(nr.status),
            borderColor: getStatusColor(nr.status)
          };
        });

        setReservas(mapped);
      }
    } catch (err) {
      console.error("Error:", err);
      alert("Error al procesar la acción");
    }
  }

  // ===========================================================
  // LOADING Y ERROR
  // ===========================================================
  if (loading) {
    return (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
          flexDirection: "column",
          gap: "1rem"
        }}
      >
        <div
          style={{
            width: "50px",
            height: "50px",
            border: "4px solid #f3f3f3",
            borderTop: "4px solid #3498db",
            borderRadius: "50%",
            animation: "spin 1s linear infinite"
          }}
        ></div>
        <p style={{ fontSize: "1.4rem" }}>Cargando datos del experto...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ padding: "2rem", textAlign: "center" }}>
        <p style={{ color: "#ef4444", fontSize: "1.2rem", marginBottom: "1rem" }}>
          {error}
        </p>
        <button
          onClick={() => window.location.reload()}
          style={{
            padding: "0.75rem 1.5rem",
            background: "#3b82f6",
            color: "white",
            border: "none",
            borderRadius: "0.5rem"
          }}
        >
          Reintentar
        </button>
      </div>
    );
  }

  if (!lineaData) {
    return (
      <p style={{ padding: "2rem", fontSize: "1.4rem" }}>
        No se pudo cargar la información de la línea.
      </p>
    );
  }

// ===========================================================
// RENDER FINAL
// ===========================================================
const proxima = getProximaCita(reservas);

return (
  <div className="xp-page">

    {/* Header Línea */}
    <section className="xp-line-strip">
      <div className="xp-line-left">
        <img
          src={LINE_IMAGES[lineaData.id]}
          className="xp-line-img"
          alt=""
        />
        <h2 className="xp-line-name">{lineaData.name}</h2>
      </div>

      {/* Próxima cita */}
      <div className="xp-next-box">
        <span className="xp-next-label">Próxima cita:</span>

        <span className="xp-next-time xp-tooltip-wrapper">
          {proxima
            ? `${new Date(proxima.start).toLocaleTimeString("es-ES", {
                hour: "2-digit",
                minute: "2-digit",
              })} con ${proxima.talentName}`
            : "No hay citas próximas"}

          {/* === TOOLTIP === */}
          {proxima && (
            <div className="xp-tooltip">
              <div className="xp-tooltip-arrow"></div>

              <p><strong>Proyecto:</strong> {proxima.associateProject || "Sin proyecto"}</p>
              <p><strong>Talento:</strong> {proxima.talentName}</p>
              <p>
                <strong>Fecha:</strong>{" "}
                {new Date(proxima.start).toLocaleDateString("es-ES")}
              </p>
              <p>
                <strong>Hora inicio:</strong>{" "}
                {new Date(proxima.start).toLocaleTimeString("es-ES", {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </p>
              <p>
                <strong>Hora fin:</strong>{" "}
                {new Date(proxima.end).toLocaleTimeString("es-ES", {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </p>
              <p><strong>Equipo:</strong> {proxima.equipo || "No asignado"}</p>
            </div>
          )}
        </span>
      </div>
    </section>

      {/* GRID */}
      <div className="xp-grid">
        <CitasCalendario
          reservations={reservas}
          viewMode={calendarView}
          setViewMode={setCalendarView}
          expertData={expertData}
          lineaData={lineaData}
          talentos={talentos}
          equipos={equipos}
          onCreated={async () => {
            const serviceLineId = expertData?.lineId;
            if (serviceLineId) {
              const reservasData = await getReservationsByLine(serviceLineId);
              const mapped = reservasData.map((r) => {
                const nr = normalizeReservation(r);
                return {
                  ...nr,
                  title: `${nr.talentName} - ${nr.expertName}`,
                  backgroundColor: getStatusColor(nr.status),
                  borderColor: getStatusColor(nr.status)
                };
              });
              setReservas(mapped);
            }
          }}
        />
        {/* SIDEBAR A LA DERECHA */}
        <SidebarPanel
          equipos={equipos}
          talentos={talentos}
          notificaciones={[]} // luego aquí le pasas las reales
        />
      </div>

      {/* TABLA DE PENDIENTES */}
      <CitasPendientes
        reservations={reservas}
        q={q}
        setQ={setQ}
        onQuickAction={handleQuickAction}
      />
  </div>
);
}