import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const AdminRoute = ({ children }) => {
  const { isAuthenticated, isAdmin, loading } = useAuth();

  // Show loading UI while auth state is being checked
  if (loading) return <div>Loading...</div>;

  // Redirect to login if not authenticated
  if (!isAuthenticated) return <Navigate to="/login" replace />;

  // Redirect to dashboard if authenticated but not admin
  if (!isAdmin()) return <Navigate to="/dashboard" replace />;

  // Render admin content
  return children;
};

export default AdminRoute;

