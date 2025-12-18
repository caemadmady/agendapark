import api from "../../../services/api";

/**
 * ✅ Obtiene recursos filtrados por línea de servicio
 * @param {number} serviceLineId - ID de la línea de servicio
 */
export async function getResourcesByLine(serviceLineId) {
    try {
        const res = await api.get(`/resources`, {
            params: { serviceLineId }
        });

        const data = res.data;
        return Array.isArray(data) ? data : (data?.content || []);
    } catch (error) {
        console.error("Error obteniendo recursos:", error);
        return [];
    }
}

/**
 * ✅ Crea un historial de mantenimiento para un recurso
 */
export async function createEquipmentHistory({ resourceId, eventType, details, eventDate }) {
  try {
    const body = {
      resourceId,
      eventDate: eventDate || new Date().toISOString(),
      eventType, // "MANTENIMIENTO_PREVENTIVO", "MANTENIMIENTO_CORRECTIVO", etc.
      details,
    };

    const res = await api.post("/equipment/histories", body);
    return res.data;
  } catch (error) {
    console.error("Error creando historial de mantenimiento:", error);
    throw error;
  }
}

/**
 * Crear recurso (GENÉRICO o BIOTECNOLÓGICO)
 */
export async function createResource(payload) {
    try {
        const res = await api.post("/resources", payload);
        return res.data;
    } catch (error) {
        console.error("Error creando recurso:", error);
        throw error;
    }
}