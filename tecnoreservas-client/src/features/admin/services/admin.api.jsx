import api from "../../../services/api.js";

// Axios directo
/*Llamadas a la api para el CRUD de los Talentos*/
export async function getTalent() {
  const { data } = await api.get(`/talents/all`);
  return data;
}

//Crear talento
export async function createTalent(talentData) {
  const { data } = await api.post(`/talents/create`, talentData);
  return data;
}


/*Llamadas a la api para el CRUD de los Expertos*/
//Traer los datos de un experto por id
export async function getExpert() {
  const { data } = await api.get(`/experts/all`);
  return data;
}

//Actualizar el email de un experto 
export async function updateExpertEmail(id, email) {
  const { data } = await api.patch(`/experts/update/email/${id}`, email);
  return data;
}

//Actualizar la contraseña de un experto
export async function updateExpertPassword(id, password) {
  const { data } = await api.patch(`/experts/update/email/${id}`, password);
  return data;
}

export async function createExpert(expertData) {
  const { data } = await api.post(`/experts/create-expert`, expertData);
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

//traer todos los usuarios
export async function getAllUsers() {
  const { data } = await api.get(`/users/all`);
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