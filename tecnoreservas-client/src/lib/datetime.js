export function combineDateTime(dateStr, timeStr) {
  const [y, m, d] = dateStr.split('-').map(Number);
  const [H, M] = timeStr.split(':').map(Number);
  return new Date(y, m - 1, d, H, M, 0, 0);
}

export function addMinutes(date, mins) {
  const d = new Date(date);
  d.setMinutes(d.getMinutes() + Number(mins || 0));
  return d;
}

export function toIsoLocal(dt) {
  const p = (n) => String(n).padStart(2, '0');
  return `${dt.getFullYear()}-${p(dt.getMonth() + 1)}-${p(dt.getDate())}T${p(dt.getHours())}:${p(dt.getMinutes())}:00`;
}



/** YYYY-MM-DD (zona local) */
export function getTodayDate() {
  const now = new Date();
  // Ajuste local para evitar desfases por timezone
  const tzOffset = now.getTimezoneOffset() * 60000;
  return new Date(now - tzOffset).toISOString().slice(0, 10);
}

export function formatDateISO(date) {
  const d = new Date(date);
  const tzOffset = d.getTimezoneOffset() * 60000;
  return new Date(d - tzOffset).toISOString().slice(0, 10); // YYYY-MM-DD
}

export function startOfDay(date = new Date()) {
  const d = new Date(date);
  d.setHours(0, 0, 0, 0);
  return d;
}

export function endOfDay(date = new Date()) {
  const d = new Date(date);
  d.setHours(23, 59, 59, 999);
  return d;
}

export function addDays(date, days) {
  const d = new Date(date);
  d.setDate(d.getDate() + days);
  return d;
}

export function isSameDay(a, b) {
  const da = new Date(a); const db = new Date(b);
  return (
    da.getFullYear() === db.getFullYear() &&
    da.getMonth() === db.getMonth() &&
    da.getDate() === db.getDate()
  );
}
//Funcion para formatear la hora en formato 12 horas con AM/PM
export function formatTime12H(date) {
  const d = new Date(date);
  let hours = d.getHours();
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const period = hours >= 12 ? 'PM' : 'AM';
  hours = hours % 12 || 12; // Convierte 0 a 12 para medianoche
  return `${hours}:${minutes} ${period}`;
}

//Función para calcular la duración entre dos fechas en horas y minutos
export function getAppointmentDuration(startDate, endDate) {
  // Validar que los parámetros existan
  if (!startDate || !endDate) {
    return "Las fechas de inicio y fin son requeridas";
  }
  
  // Limpiar formato: remover decimales inválidos después de los segundos
  const cleanDate = (dateStr) => {
    const match = String(dateStr).match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}/);
    return match ? match[0] : dateStr;
  };
  
  const start = new Date(cleanDate(startDate));
  const end = new Date(cleanDate(endDate));
  
  if (isNaN(start.getTime()) || isNaN(end.getTime())) {
    return "Formato de fecha inválido";
  }
  
  if (end < start) {
    return "La hora de fin no puede ser anterior a la hora de inicio";
  }
  
  const diffMs = end - start;
  const diffMinutes = Math.floor(diffMs / 60000);
  const hours = Math.floor(diffMinutes / 60);
  const minutes = diffMinutes % 60;
  
  if (hours === 0) {
    return `${minutes} minuto${minutes !== 1 ? 's' : ''}`;
  } else if (minutes === 0) {
    return `${hours} hora${hours !== 1 ? 's' : ''}`;
  } else {
    return `${hours} hora${hours !== 1 ? 's' : ''} ${minutes} minuto${minutes !== 1 ? 's' : ''}`;
  }
}