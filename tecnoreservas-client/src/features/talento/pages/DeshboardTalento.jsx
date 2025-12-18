// src/features/talento/pages/DashboardTalento.jsx
import React, { useEffect, useMemo, useState } from "react";
import { Calendar, CheckCircle, Bell } from "lucide-react";
import "../styles/talento.css";

import AgendaCitas from "../pages/AgendaCitas";
import MisCitas from "../pages/MisCitas";

// APIs
import { getMyUpcomingReservations, createReservation } from "../services/bookings.api";
import { combineDateTime, addMinutes, toIsoLocal } from "../../../lib/datetime";
import { getMyNotificationsCount } from "../services/notifications.api";

const LINEAS = [
  { id: "tics", nombre: "TICs" },
  { id: "biotec", nombre: "Biotecnología" },
  { id: "ingdis", nombre: "Ingeniería y Diseño" },
  { id: "elect", nombre: "Electrónica" },
];

export default function DashboardTalento() {
  const [showForm, setShowForm] = useState(false);

  // Formulario
  const [lineaId, setLineaId] = useState("");
  const [experto, setExperto] = useState("");
  const [fecha, setFecha] = useState("");
  const [hora, setHora] = useState("");
  const [duracion, setDuracion] = useState("60");
  const [notas, setNotas] = useState("");

  // Datos API
  const [expertsAll, setExpertsAll] = useState([]);
  const [upcoming, setUpcoming] = useState([]);
  const [loadingUpcoming, setLoadingUpcoming] = useState(false);
  const [saving, setSaving] = useState(false);

  // KPIs
  const [completed, setCompleted] = useState(0);
  const [notifications, setNotifications] = useState(0);

  useEffect(() => {
    (async () => {
      try {
        const ex = await (async function () {
          try {
            return [];
          } catch {
            return [];
          }
        })();
        setExpertsAll(Array.isArray(ex) ? ex : []);
      } catch {
        setExpertsAll([]);
      }
    })();
  }, []);

  async function loadUpcoming() {
    setLoadingUpcoming(true);
    try {
      const data = await getMyUpcomingReservations();
      const list = Array.isArray(data) ? data : Array.isArray(data?.content) ? data.content : [];
      setUpcoming(list);
      const completadas = list.filter((c) => c.status === "COMPLETED" || c.reservationStatus === "COMPLETED").length;
      setCompleted(completadas);
    } finally {
      setLoadingUpcoming(false);
    }
  }

  async function loadNotifications() {
    try {
      const count = await getMyNotificationsCount();
      setNotifications(count.notifications.length || 0);
    } catch { 
      setNotifications(0);
    }
  }

  useEffect(() => {
    loadUpcoming();
    loadNotifications();
  }, []);

  const expertos = useMemo(() => {
    if (!lineaId) return expertsAll;
    return (expertsAll || []).filter((e) => {
      const tag = (
        e?.line?.projectLine ||
        e?.line?.id ||
        e?.projectLine ||
        ""
      )
        .toString()
        .toLowerCase();
      return tag === lineaId.toLowerCase();
    });
  }, [expertsAll, lineaId]);

  const proximaCita = useMemo(() => {
    if (!upcoming.length) return null;
    const sorted = [...upcoming].sort(
      (a, b) => new Date(a.dateTimeStart || a.start) - new Date(b.dateTimeStart || b.start)
    );
    return sorted[0];
  }, [upcoming]);

  function resetForm() {
    setLineaId("");
    setExperto("");
    setFecha("");
    setHora("");
    setDuracion("60");
    setNotas("");
  }

  function validar() {
    if (!lineaId) return "Selecciona una línea de proyecto.";
    if (!experto) return "Selecciona un experto.";
    if (!fecha) return "Selecciona una fecha.";
    if (!hora) return "Selecciona una hora.";

    const fechaSel = new Date(fecha);
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    if (fechaSel < hoy) return "La fecha debe ser igual o posterior a hoy.";

    return "";
  }

  async function agendar() {
    const error = validar();
    if (error) {
      alert(error);
      return;
    }

    const start = combineDateTime(fecha, hora);
    const end = addMinutes(start, Number(duracion));

    const dto = {
      dateTimeStart: toIsoLocal(start),
      endDateTime: toIsoLocal(end),
      expert: Number(experto),
      talent: null,
      notes: notas || "",
    };

    setSaving(true);
    try {
      await createReservation(dto);
      setShowForm(false);
      resetForm();
      await loadUpcoming();
      alert("Cita creada exitosamente.");
    } catch (e) {
      alert(e.message || "Error al crear la cita");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="dashboard-container">
      <section className="stats-section">
        <div className="stat-card purple">
          <Calendar size={55} />
          <div>
            <h3>{upcoming.length}</h3>
            <p>Citas Programadas</p>
          </div>
        </div>
        <div className="stat-card green">
          <CheckCircle size={55} />
          <div>
            <h3>{completed}</h3>
            <p>Citas Completadas</p>
          </div>
        </div>
        <div className="stat-card orange">
          <Bell size={55} />
          <div>
            <h3>{notifications}</h3>
            <p>Notificaciones Nuevas</p>
          </div>
        </div>
      </section>

      <section className="calendar-wrapper">
        <div className="calendar-section">
          <AgendaCitas citas={upcoming} loading={loadingUpcoming} onNuevaCita={() => setShowForm(true)} />
        </div>

        <div className="side-panel">
          <div className="panel-card">
            <h2>Notificaciones</h2>
            {proximaCita ? (
              <>
                <p className="notif-date">
                  {new Date(proximaCita.dateTimeStart || proximaCita.start).toLocaleDateString("es-ES", {
                    day: "2-digit",
                    month: "long",
                  })}
                </p>
                <p className="notif-title">Recordatorio de cita</p>
                <p className="notif-text">
                  Tienes una cita programada con{" "}
                  <strong>{proximaCita.expertName || proximaCita.serviceLine?.name || "un experto"}</strong> a las{" "}
                  {new Date(proximaCita.dateTimeStart || proximaCita.start).toLocaleTimeString("es-ES", {
                    hour: "2-digit",
                    minute: "2-digit",
                  })}
                  .
                </p>
              </>
            ) : (
              <p className="notif-empty">No tienes notificaciones por ahora.</p>
            )}
          </div>

          <div className="panel-card">
            <h2>Resumen de Citas</h2>
            <p className="summary-text">
              Gestiona tus citas programadas y agenda nuevas reuniones con los expertos del ecosistema.
            </p>
            <button className="btn-primary" onClick={() => setShowForm(true)}>
              Crear nueva cita
            </button>
          </div>
        </div>
      </section>

      {/* Mostrar MisCitas como modal */}
      {showForm && (
        <MisCitas 
          showForm={showForm}
          setShowForm={setShowForm}
        />
      )}
    </div>
  );
}
 