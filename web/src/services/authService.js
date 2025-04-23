import axios from 'axios';

const API_BASE_URL = ''; // Backend adresinizi buraya yazın

export const login = async (personalNoOrEmail, password) => {
  try {
    const reqBody = 
    {
        personalNoOrEmail: personalNoOrEmail,
        password: password,
    }
    const reqConfig = 
    {
      withCredentials: true, // Oturum bilgileri için gerekli
      headers: {
      'Content-Type': 'application/json'
      }
    }
    const response = await axios.post(`${API_BASE_URL}/api/auth/login`, reqBody, reqConfig);
    
    return response.data;
  } catch (error) {
    console.error('Login error:', error.response?.data || error.message);
    throw error;
  }
};

// Token'i localStorage'a kaydetmek için yardımcı fonksiyon
export const setAuthToken = (token) => {
  if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    localStorage.setItem('accessToken', token);
  } else {
    delete axios.defaults.headers.common['Authorization'];
    localStorage.removeItem('accessToken');
  }
};

// Token'i localStorage'dan al
export const getAuthToken = () => {
  return localStorage.getItem('accessToken');
};

