// src/features/talento/services/bookings.api.js
//import { apiFetch } from "../../../lib/api";
import api from "../../../services/api";

// Reutiliza el endpoint /reservations/user
export async function getMyUpcomingReservations() {
  const { data } = await api.get("/reservations/user");
  return data;
}

export async function createReservation(dto) {
  const {data} = await api.post("/reservations/create", dto);
  return data;
 /*  // dto: { dateTimeStart, endDateTime, expert, talent?, notes? }
  return apiFetch("/reservations/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto),
  }); */
}
