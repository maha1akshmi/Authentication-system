import React, { createContext, useContext, useEffect, useState } from "react";
import { authService } from "../services/authService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    try {
      const currentUser = authService?.getCurrentUser();
      if (currentUser) setUser(currentUser);
    } catch (err) {
      console.error("Auth load error:", err);
    } finally {
      setLoading(false); // always set loading to false
    }
  }, []);

  const login = async (data) => {
    const res = await authService.login(data);
    setUser(res);
    return res;
  };

  const signup = async (data) => {
    const res = await authService.signup(data);
    setUser(res);
    return res;
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const isAdmin = () => user?.role === "ADMIN";

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        signup,
        logout,
        isAdmin,
        loading,
        isAuthenticated: !!user
      }}
    >
      {children} {/* Render children always */}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
