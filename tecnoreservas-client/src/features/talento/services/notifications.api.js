// src/features/talento/services/notifications.api.js
import { apiFetch } from "../../../lib/api";
import api from "../../../services/api";

// Ajusta ruta si tu backend la expone en otro path
export async function getMyNotificationsCount() {
  // Mi suposición: /api/notifications/count (si no existe, ajusta a tu ruta real)
  try {
    const {data} = await api.get("/notifications");
    console.log('Notificaciones:', data);
    return data;
  } catch (e) {
    console.warn("No se pudo obtener count de notificaciones:", e);
    return 0;
  }
}
