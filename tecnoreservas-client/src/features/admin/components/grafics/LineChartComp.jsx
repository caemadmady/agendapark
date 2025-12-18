import { useEffect, useMemo } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, Cell } from 'recharts';

// Colores para cada estado
const statusColors = {
  SOLICITADA: '#f59e0b',    // Naranja - solicitud pendiente
  CONFIRMADA: '#10b981',    // Verde - confirmada
  CUMPLIDA: '#3b82f6',      // Azul - completada
  INCUMPLIDA: '#ef4444'     // Rojo - no cumplida
};

export default function LineChartComp({ data }) {

  // Calcula las métricas de reservas por estado
  const appointmentMetrics = useMemo(() => {
    if (!Array.isArray(data) || data.length === 0) {
      return [
        { status: 'SOLICITADA', count: 0, fill: statusColors.SOLICITADA },
        { status: 'CONFIRMADA', count: 0, fill: statusColors.CONFIRMADA },
        { status: 'CUMPLIDA', count: 0, fill: statusColors.CUMPLIDA },
        { status: 'INCUMPLIDA', count: 0, fill: statusColors.INCUMPLIDA }
      ];
    }

    const states = {
      SOLICITADA: 0,
      CONFIRMADA: 0,
      CUMPLIDA: 0,
      INCUMPLIDA: 0
    };

    data.forEach(appointment => {
      const status = appointment.status?.toUpperCase();
      if (status in states) {
        states[status]++;
      }
    });

    return [
      { status: 'SOLICITADA', count: states.SOLICITADA, fill: statusColors.SOLICITADA },
      { status: 'CONFIRMADA', count: states.CONFIRMADA, fill: statusColors.CONFIRMADA },
      { status: 'CUMPLIDA', count: states.CUMPLIDA, fill: statusColors.CUMPLIDA },
      { status: 'INCUMPLIDA', count: states.INCUMPLIDA, fill: statusColors.INCUMPLIDA }
    ];
  }, [data]);

  return (
    <ResponsiveContainer width="100%" height={400}>
      <BarChart data={appointmentMetrics} margin={{ top: 5, right: 30, left: 0, bottom: 5 }}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="status" />
        <YAxis allowDecimals={false} domain={[0, 'dataMax + 1']} />
        <Tooltip />
        <Legend />
        <Bar dataKey="count" name="Cantidad de Reservas" radius={[8, 8, 0, 0]}>
          {appointmentMetrics.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={entry.fill} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  );
}