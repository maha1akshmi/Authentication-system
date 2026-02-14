import axiosInstance from './axiosConfig';
import { tokenUtils } from '../utils/tokenUtils';

export const authService = {
  login: async (data) => {
    const response = await axiosInstance.post('/auth/login', data);
    const { token, ...user } = response.data;
    tokenUtils.setToken(token);
    tokenUtils.setUser(user);
    return user;
  },

  signup: async (data) => {
    const response = await axiosInstance.post('/auth/signup', data);
    const { token, ...user } = response.data;
    tokenUtils.setToken(token);
    tokenUtils.setUser(user);
    return user;
  },

  logout: () => {
    tokenUtils.clearAll();
  },

  getCurrentUser: () => {
    const token = tokenUtils.getToken();
    if (!token || tokenUtils.isTokenExpired(token)) {
      tokenUtils.clearAll();
      return null;
    }
    return tokenUtils.getUser();
  },

  forgotPassword: async (email) => {
    const response = await axiosInstance.post('/auth/forgot-password', { email });
    return response.data;
  },

  resetPassword: async (token, newPassword) => {
    const response = await axiosInstance.post('/auth/reset-password', { token, newPassword });
    return response.data;
  },
};