import React from 'react';
import { FaPen, FaTrash } from 'react-icons/fa';
import '../styles/UserCard.css';

const UserCard = ({ user, onEdit, onDelete }) => {
  return (
    <div className="user-card">
      <div className="user-details">
        <h5 className="user-name">{user.name}</h5>
        <p className="user-contact">{user.email}</p>
        <p className="user-contact">Usuario: {user.username}</p>
        <div className="user-badges">
          <span className="badge role-badge">{user.role}</span>
          {user.lineProjectId ? 
          (<span className="badge line-badge">{user.lineProjectId}</span>) : 
          user.lineId ? 
          (<span className="badge line-badge">{user.lineId}</span>) : null}
        </div>
      </div>
      <div className="user-actions">
        <button className="action-btn btn-edit" onClick={onEdit}><FaPen /></button>
        <button className="action-btn btn-delete" onClick={onDelete}><FaTrash /></button>
      </div>
    </div>
  );
};

export default UserCard;