// src/features/experto/services/talentos.service.js
import api from "../../../services/api";

/**
 * ✅ Obtiene todos los talentos
 */
export async function getAllTalents() {
  try {
    const { data } = await api.get("/talents/all");
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Error obteniendo talentos:", error);
    return [];
  }
}

/**
 * ✅ Obtiene talentos filtrados por línea
 */
export async function getTalentsByLine(serviceLineId) {
  try {
    const { data } = await api.get(`/service/lines/talents/in/line/${serviceLineId}`);

    const talentos = data.map((item, index) => ({
      id: index, // backend no da ID
      name: item.talentDto.name,
      lastname: item.talentDto.lastname,
      email: item.talentDto.email,
      username: item.talentDto.username,
      status: item.talentDto.status || "ACTIVO",
      associatedProject: item.projectDetailDto?.associatedProject ?? "Sin proyecto",
      projectPhase: item.projectDetailDto?.projectPhase ?? "Sin fase",
    }));

    return talentos;
  } catch (error) {
    console.error("Error obteniendo talentos de la línea:", error);
    return [];
  }
}

/**
 * ✅ Extrae talentos únicos desde las reservas
 * (por si luego quieres usar esto en otro lado)
 */
export function getTalentosFromReservations(reservations = []) {
  const talentosUnicos = [
    ...new Map(
      (reservations || [])
        .filter((r) => r.talent || r.talentName)
        .map((r) => {
          const talent = {
            id: r.talent?.id || r.talentId,
            name: r.nameTalent || r.talent?.name || "Sin nombre",
            email: r.talent?.email || "",
          };
          return [talent.id, talent];
        })
    ).values(),
  ];

  return talentosUnicos;
}

/**
 * Crear un nuevo talento
 */
export async function createTalent(talentData) {
  try {
    const res = await api.post("/talents/create", {
      talentDto: {
        name: talentData.name,
        lastname: talentData.lastname,
        email: talentData.email,
        username: talentData.username,
        password: talentData.password,
      },
      projectDetailDto: {
        associatedProject: talentData.associatedProject,
        projectPhase: talentData.projectPhase,
      },
    });

    return res.data;

  } catch (error) {
    console.error("Error creando talento:", error);
    throw error;
  }
}
