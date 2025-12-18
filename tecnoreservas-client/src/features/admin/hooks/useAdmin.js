import { useState, useCallback } from "react";

export function useAdmin() {

  /*Estado del total de los usuarios */
  const [totalUsers, setTotalUsers] = useState(0);


  /*Estados de los talentos */
  // Estado inicial para la lista de talentos
  const [talents, setTalents] = useState([]);

  // Función para cargar talentos (simulación de una llamada a la API)
  const loadTalents = useCallback((newTalents) => {
    setTalents(newTalents);
  }, []);

  // Función para agregar un talento
  const addTalent = useCallback((talent) => {
    setTalents((prevTalents) => [...prevTalents, talent]);
  }, []);

  // Función para editar un talento
  const editTalent = useCallback((updatedTalent) => {
    setTalents((prevTalents) =>
      prevTalents.map((talent) => (talent.id === updatedTalent.id ? updatedTalent : talent))
    );
  }, []);

  // Función para eliminar un talento
  const deleteTalent = useCallback((talentId) => {
    setTalents((prevTalents) => prevTalents.filter((talent) => talent.id !== talentId));
  }, []);


  /*Estados de los Expertos */
  // Estado inicial para la lista de expertos
  const [experts, setExperts] = useState([]);

  // Función para cargar expertos (simulación de una llamada a la API)
  const loadExperts = useCallback((newExperts) => {
    setExperts(newExperts);
  }, []);

  // Función para agregar un experto
  const addExpert = useCallback((expert) => {
    setExperts((prevExperts) => [...prevExperts, expert]);
  }, []);

  // Función para editar un experto
  const editExpert = useCallback((updatedExpert) => {
    setExperts((prevExperts) =>
      prevExperts.map((expert) => (expert.id === updatedExpert.id ? updatedExpert : expert))
    );
  }, []);

  // Función para eliminar un experto
  const deleteExpert = useCallback((expertId) => {
    setExperts((prevExperts) => prevExperts.filter((expert) => expert.id !== expertId));
  }, []);

  /**Estados para las lineas de trabajo */
  const [lines, setLines] = useState([]);


  /*Estados para cargar las Reservas de citas */
  const [appointments, setAppointments] = useState([]);


  /*Estado para cargar componentes */
  const [isLoading, setIsLoading] = useState(false);

  // Retornar el estado y las funciones
  return {
    totalUsers,
    setTotalUsers,
    talents,
    loadTalents,
    addTalent,
    editTalent,
    deleteTalent,
    experts,
    loadExperts,
    addExpert,
    editExpert,
    deleteExpert,
    appointments,
    setAppointments,
    lines,
    setLines,
    isLoading,
    setIsLoading,
  };
}