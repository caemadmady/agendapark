import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function ProtectedRoute({ children, roles }) {
  const { me, loadingMe } = useAuth();
  const location = useLocation();

  if (loadingMe) {
    return (
      <div style={{display:"flex",justifyContent:"center",alignItems:"center",height:"100vh",fontSize:"2rem"}}>
        Verificando sesión...
      </div>
    );
  }
  if (!me) return <Navigate to="/login" state={{ from: location }} replace />;

  if (roles?.length && !roles.includes(me.role)) {
    const redirectByRole = { ADMIN:"/admin", RECEPCION:"/recepcion", EXPERTO:"/experto", TALENTO:"/talento" };
    return <Navigate to={redirectByRole[me.role] || "/login"} replace />;
  }
  return children;
}