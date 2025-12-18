import React, { useEffect, useState } from "react";
import api from "../../../services/api";

export default function MisCitas({ showForm, setShowForm }) {
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(true);

  //const [experts, setExperts] = useState([]);  // ⬅ NUEVO: lista de expertos
  const [talent, setTalent] = useState([]);
  const [line, setLine] = useState(null)

  const [form, setForm] = useState({
    serviceLine: "lineId",
    startDate: "",
    endDate: ""
  });


  // 🟣 CARGAR LINE DETERMINADA 
  useEffect(() => {
    async function fetchExperts() {
      try {
        const { data } = await api.get("/talents/talent/session");
        setTalent(data);
      } catch (err) {
        console.error("Error al cargar TALENTO:", err);
      }
    }
    fetchExperts();
  }, []);

  useEffect(() => {
    async function consultarLineaTalento(lineProjectId) {
      const { data } = await api.get(`/service/lines/${lineProjectId}`);
      console.log(data)
      setLine(data)
    } consultarLineaTalento(talent.lineProjectId)
  }, [talent]);
  console.log(line)

  /*   useEffect(() => {
      async function fetchTalentAndLine() {
        try {
          // 1. talento en sesión
          const talentRes = await apiFetch("/talents/talent/session");
          setTalent(talentRes);
          
          // suponiendo que el dto trae algo como lineId
          const lineId = talentRes.lineProjectId;
          if (lineId) {
            // setear serviceLine en el form
            /* setForm((prev) => ({
              ...prev,
              serviceLine: lineId,
            }));
            
            // 2. traer detalle de la línea (opcional, para mostrar nombre)
            const lineRes = await apiFetch(`/service/lines/${lineId}`);
            setLine(lineRes);
          }
        } catch (err) {
          console.error("Error al cargar talento / línea:", err);
        }
      }
      console.log("PRUEBITA: ",talent.lineProjectId)
  
      fetchTalentAndLine();
    }, []); */


  // CARGAR CITAS
  async function loadCitas() {
    try {
      setLoading(true);
      const data = await getCitasUsuario();

      const parsed = data.map((c) => ({
        id: c.id,
        serviceLine: c.expert?.name || "Sin experto",
        start: c.dateTimeStart,
        end: c.endDateTime,
        status: c.reservationStatus,
      }));

      setCitas(parsed);
    } catch (err) {
      console.error("Error al cargar citas:", err);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCitas();
  }, []);

  // CREAR CITA
  async function handleSubmit(e) {
    e.preventDefault();

    try {
      await createCita(form);
      await loadCitas();
      setShowForm(false);

      alert("Cita creada correctamente");
    } catch (err) {
      console.error("Error al crear cita:", err);
      alert("No se pudo crear la cita");
    }
  }

  return (
    <div className="tal-section">

      <h2>Mis Citas</h2>

      {/*MODAL CREAR CITA*/}
      {showForm && (
        <div className="modal-overlay">
          <div className="modal">

            <div className="modal-head">
              <h2 className="modal-title">Crear Cita</h2>
              <button className="close-btn" onClick={() => setShowForm(false)}>
                ✕
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="modal-grid">

                {/* SELECT DE LINEA (NUEVO) */}
                <div>
                  <label>Linea</label>
                  <select
                    value={form.line}
                    onChange={(e) =>
                      setForm({ ...form, line: e.target.value })
                    }
                    required
                  >
                    <option value="">Seleccione una linea</option>

                    {line && (
                      <option>
                        <p>{line.name}</p>
                      </option>
                    )}
                  </select>
                </div>

                <div>
                  <label>Fecha y hora inicio</label>
                  <input
                    type="datetime-local"
                    value={form.dateTimeStart}
                    onChange={(e) =>
                      setForm({ ...form, dateTimeStart: e.target.value })
                    }
                    required
                  />
                </div>

                <div>
                  <label>Fecha y hora fin</label>
                  <input
                    type="datetime-local"
                    value={form.endDateTime}
                    onChange={(e) =>
                      setForm({ ...form, endDateTime: e.target.value })
                    }
                    required
                  />
                </div>
              </div>

              <div className="modal-footer">
                <button
                  type="button"
                  className="cancel-btn"
                  onClick={() => setShowForm(false)}
                >
                  Cancelar
                </button>

                <button type="submit" className="submit-btn">
                  Guardar Cita
                </button>
              </div>
            </form>

          </div>
        </div>
      )}

      {/*LISTA DE CITAS*/}
      <div className="citas-list">
        {loading ? (
          <p>Cargando citas...</p>
        ) : citas.length === 0 ? (
          <div className="empty">
            <div className="icon">📭</div>
            <p className="title">No tienes citas registradas</p>
            <p className="empty-desc">Crea una cita para comenzar</p>
          </div>
        ) : (
          citas.map((cita) => (
            <div key={cita.id} className="equip">
              <div>
                <p className="equip-name">{cita.serviceLine}</p>
                <p className="equip-desc"><strong>Inicio:</strong> {cita.start}</p>
                <p className="equip-desc"><strong>Fin:</strong> {cita.end}</p>
                <p className="equip-desc"><strong>Estado:</strong> {cita.status}</p>
              </div>

              <button className="btn-ghost" onClick={() => handleDelete(cita.id)}>
                Cancelar
              </button>
            </div>
          ))
        )}
      </div>
    </div>
  );
}