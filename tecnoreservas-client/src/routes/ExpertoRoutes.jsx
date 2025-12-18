import { Route, Routes } from "react-router-dom";
import MainLayout from "../components/layout/MainLayout.jsx";
import DashboardExperto from "../features/experto/pages/DashboardExperto.jsx";

export default function ExpertoRoutes() {
  return (
    <Routes>
      {/* El MainLayout envuelve TODAS las vistas del experto */}
      <Route element={<MainLayout />}>
        <Route path="/" element={<DashboardExperto />} />
      </Route>
    </Routes>
  );
}
