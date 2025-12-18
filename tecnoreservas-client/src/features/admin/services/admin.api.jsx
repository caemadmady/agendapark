import api from "../../../services/api.js";

// Axios directo
/*Llamadas a la api para el CRUD de los Talentos*/
export async function getTalent() {
  const { data } = await api.get(`/talents/all`);
  return data;
}


/*Llamadas a la api para el CRUD de los Expertos*/
export async function getExpert() {
  const { data } = await api.get(`/experts/all`);
  return data;
}

/*Llamadas a la api para el CRUD de las Reservas de citas*/
export async function getAdminAppointments() {
  const { data } = await api.get(`/reservations/all`);
  return data;
}

/** Llamadas a la api para el CRUD de las lineas de trabajo */
// Obtener todas las lineas de trabajo
export async function getAllLines() {
  const { data } = await api.get(`/service/lines/all`);
  return data;
}

/**Llamadas a la API para hacer el CRUD de los Usuarios */
// Crear un nuevo Usuario
export async function createUser(userData) {
  const { data } = await api.post(`/register`, userData);
  return data;
}



export async function deleteUser(id) {
  const { data } = await api.delete(`/users/${id}`);
  return data;
}




export async function updateUser(id, userData) {
  return apiFetch(`/users/${id}`, {
    method: "PUT",
    body: userData,
  });
}


export async function updateAppointmentStatus(appointmentId, status) {
  return apiFetch(`/appointments/${appointmentId}/status`, {
    method: "PUT",
    body: { status },
  });
}