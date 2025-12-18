import api from "../../../services/api";

// ============================================
// IMPORTAR IMÁGENES DE LÍNEA
// ============================================
import tics from "../../../assets/TICs.png";
import bio from "../../../assets/Biotecnologia.png";
import ingdis from "../../../assets/Diseño.png";
import telecom from "../../../assets/Electronica.png";

// Todas las imágenes asociadas por ID
export const LINE_IMAGES = {
  1: tics,       // Tecnologías Virtuales (TIC's)
  2: telecom,    // Electrónica y Telecomunicaciones
  3: ingdis,     // Ingeniería y Diseño
  4: bio         // Biotecnología y Nanotecnología
};


// ============================================
// SERVICIO: OBTENER LÍNEA DE SERVICIO POR ID
// ============================================
export async function getServiceLineById(id) {
  try {
    const res = await api.get(`/service/lines/${id}`);
    return res.data; // { id, name, talentProjectDetails?, ... }
  } catch (e) {
    console.error("Error obteniendo línea:", e);
    return null;
  }
}

/**
 * ✅ Obtiene los datos del experto en sesión
 */
export async function getExpertSession() {
    const res = await api.get('/experts/expert/session');
    return res.data;
}

/**
 * ✅ Obtiene todos los expertos
 
export async function getAllExperts() {
    const res = await api.get('/experts/all');
    return res.data;
}*/