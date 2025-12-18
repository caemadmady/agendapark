import React from 'react';
import { Routes, Route } from 'react-router-dom';
import DashboardAdmin from '../features/admin/pages/DashboardAdmin';
import UserManagement from '../features/admin/components/UserManagement';
import AppointmentManagement from '../features/admin/components/AppointmentManagement'; 
import {AdminStatistics} from '../features/admin/components/AdminStatistics';


const AdminRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<DashboardAdmin />}>
        <Route index element={<UserManagement />} />
        <Route path="users" element={<UserManagement />} />
        {/* 2. Reemplaza el placeholder con el componente real */}
        <Route path="appointments" element={<AppointmentManagement />} />
        <Route path="statistics" element={<AdminStatistics />} />
      </Route>
    </Routes>
  );
};

export default AdminRoutes;