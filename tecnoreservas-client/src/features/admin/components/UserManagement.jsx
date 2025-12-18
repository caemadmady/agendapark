import React, { useState, useMemo, useEffect } from "react";
import { useOutletContext } from 'react-router-dom';
import { FaSearch, FaPlus, FaUsers } from "react-icons/fa";
import UserCard from "./UserCard";
import CreateUserModal from "./CreateUserModal";
import EditUserModal from "./EditUserModal";
import ConfirmModal from "../../../components/ui/ConfirmModal";
import "../styles/UserManagement.css";

// 🔗 usa tu capa de API
import { deleteUser } from "../services/admin.api.jsx"; 
import { Spinner } from "./Spinner";

const UserManagement = () => {
  const { talents, experts, isLoading, lines } = useOutletContext();
  const [users, setUsers] = useState([]);            // ⬅️ sin mockUsers
  const [searchTerm, setSearchTerm] = useState("");
  

  // Estados para modales (crear / editar)
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [currentUserToEdit, setCurrentUserToEdit] = useState(null);

  // Estado para confirmación de eliminación
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);

  useEffect(() => {
    const combinedUsers = [...talents, ...experts];
    setUsers(combinedUsers);
  }, [talents, experts]);

  // 🔎 Filtro memoizado
  const filteredUsers = useMemo(() => {
    const q = searchTerm.trim().toLowerCase();
    if (!q) return users;
    return users.filter((user) => {
      const name = (user.name || "").toLowerCase();
      const email = (user.email || "").toLowerCase();
      const username = (user.username || "").toLowerCase();
      const role = (user.role || "").toString().toLowerCase();
      return (
        name.includes(q) ||
        email.includes(q) ||
        username.includes(q) ||
        role.includes(q)
      );
    });
  }, [users, searchTerm]);

  // ✏️ Abrir modal de edición
  const handleOpenEditModal = (user) => {
    setCurrentUserToEdit(user);
    setIsEditModalOpen(true);
  };

  // ➕ Al crear usuario (respuesta real del backend)
  const handleUserCreated = (created) => {
    if (!created) return;
    setUsers((prev) => [created, ...prev]);
  };

  // ♻️ Al actualizar usuario
  const handleUserUpdated = (userId, updated) => {
    setUsers((prev) =>
      prev.map((u) =>
        (u.id ?? u._id) === (userId ?? updated?.id ?? updated?._id)
          ? { ...u, ...updated }
          : u
      )
    );
  };

  // 🗑️ Abrir modal de confirmar eliminación
  const handleOpenDeleteModal = (user) => {
    setUserToDelete(user);
    setIsConfirmModalOpen(true);
  };

  // 🗑️ Eliminar usuario
  const handleDeleteUser = async () => {
    if (!userToDelete) return;
    setIsDeleting(true);
    try {
      await deleteUser(userToDelete.id ?? userToDelete._id);
      setUsers((prev) =>
        prev.filter(
          (u) => (u.id ?? u._id) !== (userToDelete.id ?? userToDelete._id)
        )
      );
      setIsConfirmModalOpen(false);
      setUserToDelete(null);
    } catch (error) {
      console.error("Error al eliminar usuario:", error);
      setIsConfirmModalOpen(false);
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <>
      <div className="user-management-container card-style">
        <header className="management-header">
          <h4>
            <FaUsers /> Gestión de Usuarios
          </h4>
          <button
            className="btn-primary"
            onClick={() => setIsCreateModalOpen(true)}
          >
            <FaPlus /> Nuevo Usuario
          </button>
        </header>

        <div className="search-bar">
          <FaSearch className="search-icon" />
          <input
            type="text"
            placeholder="Buscar por nombre, email, usuario o rol..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div className="user-list">
          {filteredUsers.length && !isLoading ? (
            filteredUsers.map((user, index) => (
              <UserCard
                key={index}
                user={user}
                onEdit={() => handleOpenEditModal(user)}
                onDelete={() => handleOpenDeleteModal(user)}
              />
            ))
          ) : (
            <div className="no-users-message">
              <Spinner />
              <p>Por favor espera...</p>
            </div>
          )}
        </div>
      </div>

      {/* Modales */}
      <CreateUserModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        onUserCreated={handleUserCreated}
        lines= {lines}
      />

      <EditUserModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        userToEdit={currentUserToEdit}
        onUserUpdated={handleUserUpdated}
      />

      <ConfirmModal
        isOpen={isConfirmModalOpen}
        onClose={() => setIsConfirmModalOpen(false)}
        onConfirm={handleDeleteUser}
        title="Confirmar Eliminación"
        message={`¿Estás seguro de que deseas eliminar a ${userToDelete?.name || "este usuario"}?`}
        loading={isDeleting}
      />
    </>
  );
};

export default UserManagement;