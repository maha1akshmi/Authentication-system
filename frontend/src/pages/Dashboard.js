import React from 'react';
import { useAuth } from '../context/AuthContext';
import './Dashboard.css';

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Dashboard</h1>
        <p>Welcome back, <strong>{user?.fullName}</strong>! 👋</p>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">📊</div>
          <div className="stat-value">0</div>
          <div className="stat-label">Active Projects</div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">✅</div>
          <div className="stat-value">0</div>
          <div className="stat-label">Tasks Completed</div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">⏰</div>
          <div className="stat-value">0</div>
          <div className="stat-label">Pending Tasks</div>
        </div>
      </div>

      <div className="dashboard-content">
        <div className="dashboard-card">
          <h3>Your Profile</h3>
          <div className="profile-info">
            <div className="profile-row">
              <span className="profile-label">Full Name</span>
              <span className="profile-value">{user?.fullName}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Email</span>
              <span className="profile-value">{user?.email}</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Role</span>
              <span className="profile-value profile-role">{user?.role}</span>
            </div>
          </div>
        </div>

        <div className="dashboard-card">
          <h3>Quick Actions</h3>
          <div className="profile-info">
            <div className="profile-row">
              <span className="profile-label">Account Status</span>
              <span className="profile-value" style={{ color: 'var(--success)' }}>Active</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Member Since</span>
              <span className="profile-value">2024</span>
            </div>
            <div className="profile-row">
              <span className="profile-label">Last Login</span>
              <span className="profile-value">Just now</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;