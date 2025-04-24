import { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuthState] = useState({ token: null, userType: null });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const storedAuth = JSON.parse(localStorage.getItem('auth'));
    if (storedAuth?.token) {
      setAuthState(storedAuth);
    }
    setIsLoading(false); // Her durumda yükleme tamamlandı
  }, []);

  const setAuth = (data) => {
    localStorage.setItem('auth', JSON.stringify(data));
    setAuthState(data);
  };

  const logout = () => {
    localStorage.removeItem('auth');
    setAuthState({ token: null, userType: null });
  };

  return (
    <AuthContext.Provider value={{ auth, setAuth, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
