import React, { useEffect, useMemo, useRef, useState } from "react";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import esLocale from "@fullcalendar/core/locales/es";

import axios from "axios";
import "../styles/CitasCalendario.css";

import Calendaroexp from "../../../assets/calendario.png";

export default function CitasCalendario({
    reservations = [],
    setViewMode,
    viewMode,
    expertData,
    lineaData,
    talentos = [],
    equipos = [],
    onCreated
}) {
    const calendarRef = useRef(null);

    /* -----------------------------------------
       ESTADOS DEL MODAL
    ----------------------------------------- */
    const [openModal, setOpenModal] = useState(false);

    const [selectedDate, setSelectedDate] = useState("");
    const [hour, setHour] = useState("");

    const [selectedProject, setSelectedProject] = useState("");
    const [nameTalentAuto, setNameTalentAuto] = useState("");
    const [talentId, setTalentId] = useState("");

    const [equiposDisponibles, setEquiposDisponibles] = useState([]);
    const [equiposSeleccionados, setEquiposSeleccionados] = useState([]);

    /* Dropdowns premium */
    const [openProjectList, setOpenProjectList] = useState(false);
    const [openEquipList, setOpenEquipList] = useState(false);

    const [searchProject, setSearchProject] = useState("");
    const [searchEquipo, setSearchEquipo] = useState("");

    /* REFERENCIAS PARA CERRAR DROPDOWNS AL HACER CLICK FUERA */
const projectRef = useRef(null);
const equipRef = useRef(null);

/* CERRAR TODO SI HAGO CLICK FUERA */
useEffect(() => {
    function handleClickOutside(e) {
        if (
            projectRef.current &&
            !projectRef.current.contains(e.target)
        ) {
            setOpenProjectList(false);
        }

        if (
            equipRef.current &&
            !equipRef.current.contains(e.target)
        ) {
            setOpenEquipList(false);
        }
    }

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
        document.removeEventListener("mousedown", handleClickOutside);
    };
}, []);

    /* -----------------------------------------
       MAPEO DE EVENTOS
    ----------------------------------------- */
    const events = useMemo(() => {
        return reservations.map(r => ({
            id: String(r.id),
            title: r.talentName,
            start: r.start,
            end: r.end,
            backgroundColor: r.backgroundColor,
            borderColor: r.borderColor
        }));
    }, [reservations]);

    /* -----------------------------------------
       CAMBIO DE VISTA
    ----------------------------------------- */
    useEffect(() => {
        if (calendarRef.current) {
            calendarRef.current.getApi().changeView(viewMode);
        }
    }, [viewMode]);

    /* -----------------------------------------
       FILTRAR TALENTOS POR PROYECTO
    ----------------------------------------- */
    function filtrarTalentosPorProyecto(proyecto) {
        const lista = reservations
            .filter(r => r.associateProject === proyecto)
            .map(r => ({
                id: r.talent,
                name: r.talentName
            }));

        return [...new Map(lista.map(t => [t.id, t])).values()];
    }

    /* -----------------------------------------
       EQUIPOS DISPONIBLES
    ----------------------------------------- */
    function equiposValidosLista() {
        return equipos.filter(
            eq => eq.status === "DISPONIBLE" || eq.status === "COMPARTIDO"
        );
    }

    /* -----------------------------------------
       CREAR CITA
    ----------------------------------------- */
    async function handleCreateReservation(e) {
        e.preventDefault();

        if (!selectedProject) return alert("Seleccione un proyecto");
        if (!talentId) return alert("No hay talento para este proyecto");
        if (!hour) return alert("Seleccione una hora");

        const dateTimeStart = `${selectedDate}T${hour}:00`;
        const endDateTime = `${selectedDate}T${
            Number(hour.split(":")[0]) + 1
        }:00`;

        const expertId = expertData.id;
        const nameExpert = `${expertData.name} ${expertData.lastname}`;

        const talento = talentos.find(t => t.id == talentId);
        const nameTalent = talento?.name || "";

        const payload = {
            dateTimeStart,
            endDateTime,
            expert: expertId,
            talent: talentId,
            nameTalent,
            nameExpert,
            status: "SOLICITADA",
            serviceLineId: lineaData.id,
            associateProject: selectedProject
        };

        try {
            const api = axios.create({
                baseURL: import.meta.env.VITE_API_BASE_URL,
                headers: { "Content-Type": "application/json" }
            });

            api.interceptors.request.use(config => {
                const token = localStorage.getItem("token");
                if (token) config.headers.Authorization = `Bearer ${token}`;
                return config;
            });

            const res = await api.post("/reservations/create", payload);
            const newId = res.data.id;

            /* asignar recursos */
            for (const eq of equiposSeleccionados) {
                await api.post(
                    `/reservation/resources/assign/${newId}`,
                    [eq]
                );
            }

            alert("Cita creada correctamente");

            /* Reset modal */
            setOpenModal(false);
            setSelectedProject("");
            setNameTalentAuto("");
            setTalentId("");
            setSelectedDate("");
            setHour("");
            setEquiposSeleccionados([]);

            if (onCreated) onCreated();
        } catch (err) {
            console.error(err);
            alert("Error creando la cita");
        }
    }

    /* -----------------------------------------
       LISTA FILTRADA DE PROYECTOS
    ----------------------------------------- */
    const proyectosUnicos = [
        ...new Set(reservations.map(r => r.associateProject))
    ].filter(p => p && p.trim() !== "");

    const proyectosFiltrados = proyectosUnicos.filter(p =>
        p.toLowerCase().includes(searchProject.toLowerCase())
    );

    /* -----------------------------------------
       LISTA FILTRADA DE EQUIPOS
    ----------------------------------------- */
    const equiposFiltrados = equiposValidosLista().filter(eq =>
        eq.name.toLowerCase().includes(searchEquipo.toLowerCase())
    );

    /* -----------------------------------------
       RENDER
    ----------------------------------------- */
    return (
        <>
            <div className="xp-cal-card">
                <div className="xp-cal-header-row">
                    <div className="xp-cal-title">
                        <img src={Calendaroexp} className="xp-gc-icon" alt="" />
                        <h2>Gestión de Citas</h2>
                    </div>

                    <div className="xp-tabs">
                        <button
                            className={`xp-tab ${
                                viewMode === "dayGridMonth" ? "active" : ""
                            }`}
                            onClick={() => {
                                calendarRef.current
                                    .getApi()
                                    .changeView("dayGridMonth");
                                setViewMode("dayGridMonth");
                            }}
                        >
                            Mes
                        </button>

                        <button
                            className={`xp-tab ${
                                viewMode === "timeGridWeek" ? "active" : ""
                            }`}
                            onClick={() => {
                                calendarRef.current
                                    .getApi()
                                    .changeView("timeGridWeek");
                                setViewMode("timeGridWeek");
                            }}
                        >
                            Semana
                        </button>

                        <button
                            className={`xp-tab ${
                                viewMode === "timeGridDay" ? "active" : ""
                            }`}
                            onClick={() => {
                                calendarRef.current
                                    .getApi()
                                    .changeView("timeGridDay");
                                setViewMode("timeGridDay");
                            }}
                        >
                            Día
                        </button>
                    </div>

                    <button
                        className="xp-add-btn"
                        onClick={() => {
                            const api = calendarRef.current.getApi();
                            const hoy = new Date()
                                .toISOString()
                                .slice(0, 10);
                            api.changeView("timeGridDay", hoy);
                            setSelectedDate(hoy);
                            setOpenModal(true);
                        }}
                    >
                        Añadir cita
                    </button>
                </div>

                <div className="xp-cal-shell">
                    <FullCalendar
                        ref={calendarRef}
                        plugins={[
                            dayGridPlugin,
                            timeGridPlugin,
                            interactionPlugin
                        ]}
                        locale={esLocale}
                        events={events}
                        height="auto"
                        initialView={viewMode}
                        headerToolbar={{
                            left: "prev",
                            center: "title today",
                            right: "next"
                        }}
                    />
                </div>
            </div>

            {/* ================= MODAL ================= */}
            {openModal && (
                <div className="xp-overlay-premium">
                    <div className="xp-modal-premium">
                        <div className="xp-modal-header-premium">
                            <h2 className="xp-title-premium">Crear cita</h2>

                            <button
                                className="xp-close-premium"
                                onClick={() => setOpenModal(false)}
                            >
                                ✕
                            </button>
                        </div>

                        <form
                            className="xp-form-premium"
                            onSubmit={handleCreateReservation}
                        >
                            {/* PROYECTO — dropdown premium */}
                            <div className="xp-field-premium" ref={projectRef}>
                                <label className="xp-label-premium">Proyecto asociado</label>

                                <div
                                    className={`xp-dropdown-wrapper ${openProjectList ? "open" : ""}`}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setOpenProjectList(!openProjectList);
                                        setOpenEquipList(false); // → cerrar equipos
                                    }}
                                >
                                    <span className="xp-icon-premium">📁</span>

                                    <input
                                        className="xp-dropdown-text"
                                        placeholder="Seleccione un proyecto..."
                                        value={selectedProject}
                                        onChange={(e) => {
                                            const val = e.target.value;
                                            setSelectedProject(val);
                                            setSearchProject(val);
                                            setOpenProjectList(true);
                                            setOpenEquipList(false); // cerrar equipos
                                        }}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            setOpenProjectList(true);
                                            setOpenEquipList(false);
                                        }}
                                    />

                                    <span className="xp-arrow">▼</span>
                                </div>

                                {openProjectList && (
                                    <div className="xp-dropdown-list">
                                        {proyectosFiltrados.map((p, i) => (
                                            <div key={i}>
                                                <div
                                                    className="xp-dropdown-item"
                                                    onClick={() => {
                                                        setSelectedProject(p);
                                                        setSearchProject(p);
                                                        setOpenProjectList(false);

                                                        const talento = filtrarTalentosPorProyecto(p)[0];
                                                        setTalentId(talento?.id || "");
                                                        setNameTalentAuto(talento?.name || "");

                                                        setEquiposDisponibles(equiposValidosLista());
                                                    }}
                                                >
                                                    {p}
                                                </div>

                                                {/* divisor */}
                                                {i < proyectosFiltrados.length - 1 && (
                                                    <div className="xp-divider"></div>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>

                            {/* TALENTO */}
                            <div className="xp-field-premium">
                                <label className="xp-label-premium">Talento</label>
                                <div className="xp-input-wrapper-premium disabled">
                                    <span className="xp-icon-premium">🧑🏻‍💻</span>
                                    <input
                                        type="text"
                                        className="xp-input-premium"
                                        value={nameTalentAuto}
                                        disabled
                                        placeholder="El talento se asigna automáticamente"
                                    />
                                </div>
                            </div>

                            {/* FECHA */}
                            <div className="xp-field-premium">
                                <label className="xp-label-premium">Fecha</label>
                                <div className="xp-input-wrapper-premium">
                                    <span className="xp-icon-premium">📅</span>
                                    <input
                                        type="date"
                                        className="xp-input-premium"
                                        value={selectedDate}
                                        onChange={(e) =>
                                            setSelectedDate(e.target.value)
                                        }
                                    />
                                </div>
                            </div>

                            {/* HORA */}
                            <div className="xp-field-premium">
                                <label className="xp-label-premium">Hora</label>
                                <div className="xp-input-wrapper-premium">
                                    <span className="xp-icon-premium">⏰</span>
                                    <input
                                        type="time"
                                        className="xp-input-premium"
                                        value={hour}
                                        onChange={(e) =>
                                            setHour(e.target.value)
                                        }
                                    />
                                </div>
                            </div>

                            {/* EQUIPOS — dropdown premium (hacia arriba) */}
                            <div className="xp-field-premium" ref={equipRef}>
                                <label className="xp-label-premium">Equipos disponibles</label>

                                <div
                                    className={`xp-dropdown-wrapper ${openEquipList ? "open" : ""}`}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setOpenEquipList(!openEquipList);
                                        setOpenProjectList(false); // cerrar proyectos
                                    }}
                                >
                                    <span className="xp-icon-premium">🧰</span>

                                    <input
                                        className="xp-dropdown-text"
                                        placeholder="Seleccione un equipo..."
                                        value={
                                            equiposSeleccionados.length
                                                ? equiposDisponibles.find(eq => eq.id == equiposSeleccionados[0])?.name
                                                : searchEquipo
                                        }
                                        onChange={(e) => {
                                            const val = e.target.value;
                                            setSearchEquipo(val);
                                            setOpenEquipList(true);
                                            setOpenProjectList(false);
                                        }}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            setOpenEquipList(true);
                                            setOpenProjectList(false);
                                        }}
                                    />

                                    <span className="xp-arrow">▲</span>
                                </div>

                                {openEquipList && (
                                    <div className="xp-dropdown-list up">
                                        {equiposFiltrados.map((eq, i) => (
                                            <div key={eq.id}>
                                                <div
                                                    className="xp-dropdown-item"
                                                    onClick={() => {
                                                        setEquiposSeleccionados([eq.id]);
                                                        setSearchEquipo(eq.name);
                                                        setOpenEquipList(false);
                                                    }}
                                                >
                                                    {eq.name} ({eq.status})
                                                </div>

                                                {/* divisor */}
                                                {i < equiposFiltrados.length - 1 && (
                                                    <div className="xp-divider"></div>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                            <button type="submit" className="xp-btn-premium">
                                Crear Cita
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </>
    );
}
