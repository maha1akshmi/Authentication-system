import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const PrivateRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  // Show loading UI while auth state is being checked
  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <div className="loading-text">Loading...</div>
      </div>
    );
  }

  // If user is authenticated, render children, otherwise redirect to login
  return isAuthenticated ? children : <Navigate to="/login" replace />;
};

export default PrivateRoute;
