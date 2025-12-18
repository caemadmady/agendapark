// src/features/talento/services/citas.api.js
import { apiFetch } from "../../../lib/api";
import api from "../../../services/api";

// Devuelve array de ReservationDto o pageable.content
export async function getCitasUsuario() {
  const { data } = await api.get("/reservations/user");
  return data;
}

// Crear reserva
// espera un objeto con { dateTimeStart, endDateTime, expert, talent?, notes? }
// dateTimeStart/endDateTime en formato "yyyy-MM-dd HH:mm:ss"
export async function createCita(cita) {
  const { data } = await api.post("/reservations/create", cita);
  return data;
}

// Cambia estado a canceled (PATCH)
export async function deleteCita(id) {
  const { data } = await api.patch(`/reservations/canceled/${id}`);
  return data;
}

// Obtener el perfil del talento autenticado
export async function getUserProfile() {
  const { data } = await api.get("/talents/talent/session");
  return data;
}
