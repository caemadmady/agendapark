// src/auth/auth.utils.js

// Normaliza el rol desde lo que devuelva el backend
export const mapRole = (raw) => {
  if (!raw) return null;
  const x = String(raw).toUpperCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
  if (["ADMIN", "SUPERADMIN"].includes(x)) return "ADMIN";
  if (["SECURITY"].includes(x)) return "RECEPCION";
  if (["EXPERTO", "EXPERT"].includes(x)) return "EXPERTO";
  if (["TALENTO", "TALENT"].includes(x)) return "TALENTO";
  return x;
};

// Normaliza el id de línea/serviceline
export const mapLine = (dto = {}) =>
  dto?.linea?.id ||
  dto?.line ||
  dto?.serviceLine ||
  dto?.serviceLineId ||
  null;