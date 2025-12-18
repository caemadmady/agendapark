import api from "../../../services/api.js";

// Normaliza una reserva del backend a un formato unificado para el frontend
export function normalizeReservation(r) {
  return {
    id: r.id,

    start: r.dateTimeStart,
    end: r.endDateTime,

    status: r.status || "DESCONOCIDO",

    talentName: r.nameTalent || "Talento no asignado",
    expertName: r.nameExpert || "Experto no asignado",

    // ⭐ AQUÍ VA LA CORRECCIÓN
    associateProject: r.associateProject || "",
    projectName: r.associateProject || "",

    serviceLineId: r.serviceLineId,
    raw: r
  };
}


export function getStatusColor(status) {
  if (!status) return "#6b7280"; // gris

  const s = status.toUpperCase();

  const colors = {
    "SOLICITADA": "#3b82f6",  // azul
    "SCHEDULED":  "#3b82f6",
    "CONFIRMADA": "#10b981",  // verde
    "CONFIRMED":  "#10b981",
    "CANCELADA":  "#ef4444",  // rojo
    "CANCELED":   "#ef4444",
    "CUMPLIDA":   "#8b5cf6",  // morado
    "FULFILLED":  "#8b5cf6",
    "INCUMPLIDA": "#f59e0b",  // amarillo
    "MISSED":     "#f59e0b",
  };

  return colors[s] || "#6b7280"; // default: gris
}

/**
 * ✅ Obtiene reservas por línea de servicio
 * @param {number} serviceLineId - ID de la línea de servicio
 */
export async function getReservationsByLine(serviceLineId) {
    try {
        const { data } = await api.get(`/reservations/serviceline/${serviceLineId}`);
        console.log(data);
        return data;
    } catch (error) {
        console.error("Error obteniendo reservas:", error);
        return [];
    }
}

/**
 * ✅ Obtiene todas las reservas del usuario en sesión
 */
export async function getMyReservations() {
    try {
        const { data } = await api.get('/reservations/user');
        return data;
    } catch (error) {
        console.error("Error obteniendo mis reservas:", error);
        return [];
    }
}

/**
 * ✅ Confirma una reserva
 * PATCH /reservations/confirmed/{id}
 */
export async function confirmReservation(id) {
    try {
        const { data } = await api.patch(`/reservations/confirmed/${id}`);
        return data;
    } catch (error) {
        console.error("Error confirmando reserva:", error);
        throw error;
    }
}

/**
 * ✅ Cancela una reserva
 * PATCH /reservations/canceled/{id}
 */
export async function cancelReservation(id) {
    try {
        const { data } = await api.patch(`/reservations/canceled/${id}`);
        return data;
    } catch (error) {
        console.error("Error cancelando reserva:", error);
        throw error;
    }
}

/**
 * ✅ Marca reserva como cumplida
 * PATCH /reservations/fulfilled/{id}
 */
export async function fulfillReservation(id) {
    try {
        const { data } = await api.patch(`/reservations/fulfilled/${id}`);
        return data;
    } catch (error) {
        console.error("Error marcando como cumplida:", error);
        throw error;
    }
}

/**
 * ✅ Marca reserva como incumplida
 * PATCH /reservations/missed/{id}
 */
export async function missReservation(id) {
    try {
        const { data } = await api.patch(`/reservations/missed/${id}`);
        return data;
    } catch (error) {
        console.error("Error marcando como incumplida:", error);
        throw error;
    }
}

/**
 * ✅ Actualiza estado genérico
 */
export async function updateReservationStatus(id, status) {
    const statusMap = {
        'confirmed': confirmReservation,
        'canceled': cancelReservation,
        'fulfilled': fulfillReservation,
        'missed': missReservation,
    };

    const updateFn = statusMap[status];
    if (!updateFn) {
        throw new Error(`Estado no válido: ${status}`);
    }

    return updateFn(id);
}

/**
 * ✅ Crear reserva como experto
 * POST /experts/create-reservations
 */
export async function createReservationByExpert(payload) {
    try {
        const { data } = await api.post('/experts/create-reservations', payload);
        return data;
    } catch (error) {
        console.error("Error creando reserva:", error);
        throw error;
    }
}