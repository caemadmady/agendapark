// Simula notificaciones SSE
export const listenNotifications = (expertId, onMessage) => {
  console.log("🔥 Simulando SSE para experto:", expertId);

  const interval = setInterval(() => {
    onMessage({
      id: Date.now(),
      mensaje: "Nueva cita registrada para hoy.",
      fecha: new Date().toISOString(),
      _new: true
    });
  }, 8000);

  return {
    close() {
      clearInterval(interval);
    }
  };
};
