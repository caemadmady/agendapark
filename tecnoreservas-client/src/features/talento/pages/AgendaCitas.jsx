// src/features/talento/pages/AgendaCitas.jsx
import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import esLocale from "@fullcalendar/core/locales/es";


export default function AgendaCitas({ citas: externalCitas, loading }) {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    async function loadEvents() {
      try {
        const data = externalCitas ?? (await getCitasUsuario());
        const arr = Array.isArray(data) ? data : Array.isArray(data?.content) ? data.content : [];
        const formatted = arr.map((cita) => {
          // Asegurar ISO para FullCalendar -> si backend devuelve "yyyy-MM-dd HH:mm:ss" convertimos a "yyyy-MM-ddTHH:mm:ss"
          const startRaw = cita.dateTimeStart || cita.start;
          const endRaw = cita.endDateTime || cita.end;
          const start = startRaw ? startRaw.replace(" ", "T") : null;
          const end = endRaw ? endRaw.replace(" ", "T") : null;

          return {
            id: cita.id,
            title: cita.serviceLine?.name || cita.serviceLine || cita.expertName || "Cita",
            start,
            end,
          };
        });

        setEvents(formatted);
      } catch (error) {
        console.error("Error al cargar calendario:", error);
        setEvents([]);
      }
    }

    loadEvents();
  }, [externalCitas]);

  return (
    <div className="agenda-wrapper">
      <h2 className="agenda-title">📅 Mi Agenda</h2>

      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        locale={esLocale}
        headerToolbar={{
          left: "prev,next today",
          center: "title",
          right: "dayGridMonth,timeGridWeek,timeGridDay",
        }}
        buttonText={{ today: "Hoy", month: "Mes", week: "Semana", day: "Día" }}
        events={events}
        height="auto"
      />
    </div>
  );
}
