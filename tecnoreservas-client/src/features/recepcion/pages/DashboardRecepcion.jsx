// src/features/recepcion/pages/DashboardRecepcion.jsx
import React, { useEffect, useMemo, useState } from "react";
import { FaCalendarDay, FaClock, FaUsers, FaCogs, FaChartBar, FaUserTie, FaSearch } from "react-icons/fa";
import StatCard from "../../../components/ui/StatCard";
import AppointmentCard from "../components/AppointmentCard";
import { getTodayDate } from "../../../lib/datetime.js";
import axios from "axios";
import "../styles/DashboardRecepcion.css";
import api from "../../../services/api.js";

const DashboardRecepcion = () => {
    // --- ESTADO ---
    const [appointments, setAppointments] = useState([]);
    const [talents, setTalents] = useState([]);
    const [experts, setExperts] = useState([]);
    const [lines, setLines] = useState([]);
    //const [equipment, setEquipment] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [activeTab, setActiveTab] = useState('all');
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedLine, setSelectedLine] = useState('all');
    const [selectedStatus, setSelectedStatus] = useState('all');


    // --- CARGAR DATOS ---
    useEffect(() => {
        let active = true;


        async function fetchData() {
            try {
                setLoading(true);
                setError("");

                console.log("📡 Cargando datos del backend...");

                // ✅ Endpoints correctos según los controladores
                const [appointmentsRes, talentsRes, expertsRes, linesRes, equipmentRes] = await Promise.allSettled([
                    api.get("/reservations/all"),     // ✅ ReservationController - GET /reservations/all
                    api.get("/talents/all"),           // ✅ TalentController - GET /talents/all
                    api.get("/experts/all"),           // ✅ ExpertController - GET /experts/all
                    api.get("/service/lines/all"),         // ✅ ServiceLineController - GET /service/lines/**
                    //api.get("/resources"),             // ✅ ResourceController (asumiendo)
                ]);

                if (!active) return;

                // ========== EXTRAER DATOS ==========
                const extractData = (response, label) => {
                    if (response.status !== "fulfilled") {
                        console.warn(`⚠️ ${label} no se pudo cargar:`, response.reason?.message);
                        return [];
                    }

                    const data = response.value?.data;
                    console.log(`📋 ${label} - Estructura:`, data);

                    // Si ya es un array
                    if (Array.isArray(data)) {
                        console.log(`✅ ${label} - Array directo con ${data.length} items`);
                        return data;
                    }

                    // Si viene en .content (paginado de Spring Boot)
                    if (data?.content && Array.isArray(data.content)) {
                        console.log(`✅ ${label} - Paginado (.content) con ${data.content.length} items`);
                        return data.content;
                    }

                    console.warn(`⚠️ ${label} - Estructura no reconocida:`, data);
                    return [];
                };

                // ========== MAPEAR TALENTS (TalentDto) ==========
                const mapTalent = (dto) => {
                    console.log("🔄 Mapeando talent:", dto);

                    return {
                        id: dto.id,
                        name: dto.name || dto.fullName || dto.username || "N/A",
                        email: dto.email || "N/A",
                        username: dto.username,
                        line: dto.serviceLine?.name || dto.projectLine || "N/A",
                        status: dto.status || dto.active
                    };
                };

                // ========== MAPEAR EXPERTS (ExpertDto) ==========
                const mapExpert = (dto) => {
                    console.log("🔄 Mapeando expert:", dto);

                    return {
                        id: dto.id,
                        name: dto.name || dto.fullName || dto.username || "N/A",
                        email: dto.email || "N/A",
                        username: dto.username,
                        line: dto.serviceLine?.name || dto.projectLine || "N/A",
                        status: dto.status || dto.active
                    };
                };

                // ========== MAPEAR APPOINTMENTS (ReservationDto) ==========
                const mapAppointment = (dto) => {
                    console.log("🔄 Mapeando appointment:", dto);

                    return {
                        id: dto.id,
                        // Fechas
                        date: dto.dateTimeStart?.split('T')[0] || dto.date,
                        dateTimeStart: dto.dateTimeStart,
                        endDateTime: dto.endDateTime,

                        // Nombres - según el DTO

                        talentName: dto.talent || dto.talentDto?.name || dto.talent?.name || "N/A",
                        expertName: dto.expert || dto.expertDto?.name || dto.expert?.name || "N/A",

                        // Línea de servicio
                        line: dto.serviceLineName || dto.serviceLine?.name || dto.projectLine || "N/A",

                        // Estado - según el enum del backend
                        status: dto.status || dto.reservationStatus || "N/A",

                        // Notas
                        notes: dto.notes || dto.observations || "",

                        // Equipos/recursos
                        //equipment: dto.resources || dto.equipment || []
                    };
                };



                // ========== PROCESAR DATOS ==========
                const appointmentsData = extractData(appointmentsRes, "Appointments").map(mapAppointment);
                const talentsData = extractData(talentsRes, "Talents").map(mapTalent);
                const expertsData = extractData(expertsRes, "Experts").map(mapExpert);
                let linesData = extractData(linesRes, "Lines");
                console.log("Lineas procesadas: ",linesData)
                //const equipmentData = extractData(equipmentRes, "Equipment");

                // Si lines viene como objetos, extraer los nombres
                if (linesData.length > 0 && typeof linesData[0] === 'object') {
                    linesData = linesData.map(l => l.name || l.serviceLine || l.label || "").filter(Boolean);
                }
                // Buscar nombre del talento por id y asignarlo a la reserva
                appointmentsData.forEach(appointment => {
                    appointment.talentName = talentsData.find((t) => t.id === appointment.talentName).name;
                    //appointment.line = linesData.find((t) => t.data.id === appointment.expertName).name;
                    appointment.expertName = expertsData.find((t) => t.id === appointment.expertName).name;
                    console.log("la linea es: ",appointment.line);
                    //console.log("la linea es: ",appointment.line);
                })


                // Derivar líneas si no existen
                if (!linesData.length) {
                    console.log("⚠️ Derivando líneas desde otros datos...");
                    const allLines = new Set([
                        ...appointmentsData.map(a => a.line),
                        ...talentsData.map(t => t.line),
                        ...expertsData.map(e => e.line)
                    ]);
                    linesData = Array.from(allLines).filter(Boolean);
                }

                /*console.group("✅ DATOS PROCESADOS");
                console.log("Appointments:", appointmentsData.length, "items");
                console.log("  └─ Primero:", appointmentsData[0]);
                console.log("Talents:", talentsData.length, "items");
                console.log("  └─ Primero:", talentsData[0]);
                console.log("Experts:", expertsData.length, "items");
                console.log("  └─ Primero:", expertsData[0]);
                console.log("Lines:", linesData);
                console.log("Equipment:", equipmentData.length, "items");
                console.groupEnd();*/

                // ========== ACTUALIZAR ESTADO ==========

                setAppointments(appointmentsData);
                setTalents(talentsData);
                setExperts(expertsData);
                setLines(linesData);
                //setEquipment(equipmentData);

            } catch (err) {
                console.error("❌ Error crítico:", err);
                setError(err?.response?.data?.message || err?.message || "Error al cargar datos");
            } finally {
                if (active) setLoading(false);
            }
        }

        fetchData();
        return () => { active = false; };
    }, []);

    // --- CÁLCULOS DERIVADOS ---
    const today = getTodayDate();

    const todayAppointments = useMemo(() =>
            appointments.filter(apt => apt.date === today),
        [appointments, today]
    );

    const upcomingAppointments = useMemo(() =>
            appointments.filter(apt => {
                const status = apt.status?.toUpperCase();
                return status === "PROGRAMADA" || status === "SCHEDULED" || status === "CONFIRMADA" || status === "CONFIRMED";
            }),
        [appointments]
    );

    /*const availableEquipment = useMemo(() =>
            equipment.filter(e => {
                const available = e.available ?? e.disponible ?? e.isAvailable;
                return available === true || available === "true" || available === 1;
            }).length,
        [equipment]
    );*/

    const filteredAppointments = useMemo(() => {
        return appointments.filter(apt => {
            const matchesSearch = searchTerm === '' ||
                apt.talentName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                apt.expertName.toLowerCase().includes(searchTerm.toLowerCase());

            const matchesLine = selectedLine === 'all' || apt.line === selectedLine;
            const matchesStatus = selectedStatus === 'all' || apt.status === selectedStatus;

            return matchesSearch && matchesLine && matchesStatus;
        });
    }, [appointments, searchTerm, selectedLine, selectedStatus]);

    const lineStats = useMemo(() => {
        return lines.map(line => {
            const lineExperts = experts.filter(e => e.line === line);
            const lineAppointments = appointments.filter(a => a.line === line);

            return {
                name: line,
                experts: lineExperts.length,
                appointments: lineAppointments.length
            };
        });
    }, [lines, experts, appointments]);

    // --- FUNCIONES DE RENDERIZADO ---
    const getContentHeaderClass = () => {
        if (activeTab === 'today') return 'content-header-green';
        if (activeTab === 'stats') return 'content-header-purple';
        return 'content-header-blue';
    };

    const renderTabContent = () => {
        if (activeTab === 'today') {
            return (
                <div className="appointments-list-section card-style card-border-green">
                    <h4 className={getContentHeaderClass()}>Citas de Hoy ({todayAppointments.length})</h4>
                    {todayAppointments.length > 0 ? (
                        todayAppointments.map(app => <AppointmentCard key={app.id} appointment={app} />)
                    ) : (
                        <p className="empty-message">No hay citas programadas para hoy.</p>
                    )}
                </div>
            );
        }

        if (activeTab === 'stats') {
            return (
                <div className="statistics-section card-style card-border-purple">
                    <h4 className={getContentHeaderClass()}><FaChartBar /> Estadísticas por Línea de Proyecto</h4>
                    {lineStats.length > 0 ? (
                        lineStats.map(stat => (
                            <div key={stat.name} className="line-stat-card">
                                <h5>{stat.name}</h5>
                                <div className="line-stat-details">
                                    <span><FaUserTie /> {stat.experts} Expertos</span>
                                    <span><FaClock /> {stat.appointments} Citas</span>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p className="empty-message">No hay estadísticas disponibles.</p>
                    )}
                </div>
            );
        }

        return (
            <div className="appointments-list-section card-style card-border-blue">
                <h4 className={getContentHeaderClass()}>Todas las Citas ({filteredAppointments.length})</h4>
                {filteredAppointments.length > 0 ? (
                    filteredAppointments.map(app => <AppointmentCard key={app.id} appointment={app} />)
                ) : (
                    <p className="empty-message">No se encontraron citas con los filtros aplicados.</p>
                )}
            </div>
        );
    };

    // --- LOADING Y ERROR ---
    if (loading) {
        return (
            <div className="dashboard-container">
                <div className="loading-state" style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    minHeight: '400px',
                    flexDirection: 'column',
                    gap: '1rem'
                }}>
                    <div style={{
                        width: '50px',
                        height: '50px',
                        border: '4px solid #f3f3f3',
                        borderTop: '4px solid #3498db',
                        borderRadius: '50%',
                        animation: 'spin 1s linear infinite'
                    }}></div>
                    <p>Cargando datos del dashboard...</p>
                    <style>{`
            @keyframes spin {
              0% { transform: rotate(0deg); }
              100% { transform: rotate(360deg); }
            }
          `}</style>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="dashboard-container">
                <div className="error-state" style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    minHeight: '400px',
                    gap: '1rem',
                    padding: '2rem'
                }}>
                    <p style={{ color: '#ef4444', fontSize: '1.1rem', fontWeight: '600' }}>
                        ⚠️ Error: {error}
                    </p>
                    <button
                        onClick={() => window.location.reload()}
                        style={{
                            padding: '0.75rem 1.5rem',
                            background: '#3b82f6',
                            color: 'white',
                            border: 'none',
                            borderRadius: '0.5rem',
                            cursor: 'pointer',
                            fontWeight: '600'
                        }}
                    >
                        Reintentar
                    </button>
                </div>
            </div>
        );
    }

    // --- RENDER PRINCIPAL ---
    return (
        <div className="dashboard-container">
            <section className="stats-grid">
                <StatCard
                    icon={<FaCalendarDay />}
                    value={todayAppointments.length}
                    label="Citas Hoy"
                    color="#3b82f6"
                />
                <StatCard
                    icon={<FaClock />}
                    value={upcomingAppointments.length}
                    label="Citas Programadas"
                    color="#10b981"
                />
                <StatCard
                    icon={<FaUsers />}
                    value={talents.length}
                    label="Talentos Totales"
                    color="#8b5cf6"
                />
                {/*<StatCard
                    icon={<FaCogs />}
                    value={availableEquipment}
                    label="Equipos Disponibles"
                    color="#f59e0b"
                />*/}
            </section>

            <section className="filter-section card-style">
                <h3 className="section-title"><FaSearch /> Búsqueda y Filtros</h3>
                <div className="filter-controls">
                    <div className="filter-box filter-box-blue">
                        <label className="filter-label label-blue">Buscar por Nombre</label>
                        <input
                            type="text"
                            placeholder="Buscar talento o experto..."
                            className="search-input"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <div className="filter-box filter-box-purple">
                        <label className="filter-label label-purple">Línea de Proyecto</label>
                        <select
                            className="filter-select"
                            value={selectedLine}
                            onChange={(e) => setSelectedLine(e.target.value)}
                        >
                            <option value="all">Todas las líneas</option>
                            {lines.map(line => (
                                <option key={line} value={line}>{line}</option>
                            ))}
                        </select>
                    </div>

                    {/*<div className="filter-box filter-box-green">
                        <label className="filter-label label-green">Estado</label>
                        <select
                            className="filter-select"
                            value={selectedStatus}
                            onChange={(e) => setSelectedStatus(e.target.value)}
                        >
                            <option value="all">Todos los estados</option>
                            <option value="Programada">Programada</option>
                            <option value="Confirmada">Confirmada</option>
                            <option value="Completada">Completada</option>
                            <option value="Cancelada">Cancelada</option>
                        </select>
                    </div>*/}
                </div>
            </section>

            <section className="tabs-section">
                <button
                    className={`tab-button ${activeTab === 'all' ? 'active active-all' : ''}`}
                    onClick={() => setActiveTab('all')}
                >
                    Todas las Citas
                </button>
                <button
                    className={`tab-button ${activeTab === 'today' ? 'active active-today' : ''}`}
                    onClick={() => setActiveTab('today')}
                >
                    Citas de Hoy
                </button>
                {/*<button
                    className={`tab-button ${activeTab === 'stats' ? 'active active-stats' : ''}`}
                    onClick={() => setActiveTab('stats')}
                >
                    Estadísticas
                </button>*/}
            </section>

            {renderTabContent()}
        </div>
    );
};

export default DashboardRecepcion;
