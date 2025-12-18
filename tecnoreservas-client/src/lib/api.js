// src/lib/api.js
import api, { getToken, setToken } from "../services/api.js";

/**
 * apiFetch: compat layer para mantener la firma antigua
 * path: string, { method, body, headers }
 */
export async function apiFetch(path, { method = "GET", body, headers } = {}) {
  try {
    const res = await api.request({
      url: path,
      method,
      data: body,
      headers,
    });
    return res.data;
  } catch (err) {
    const status = err?.response?.status;

    if (status === 401) {
      setToken(null);
      throw new Error("No autorizado (401)");
    }

    const msg =
      err?.response?.data?.message ||
      err?.response?.data ||
      err?.message ||
      `Error ${status || ""}`;
    throw new Error(typeof msg === "string" ? msg : JSON.stringify(msg));
  }
}

// Re-export para que los componentes antiguos sigan funcionando
export { getToken, setToken };
