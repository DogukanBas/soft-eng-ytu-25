
import { Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext.jsx';

const ProtectedRoute = ({ children, requiredRole }) => {
  const { auth, isLoading } = useAuth();

  if (isLoading) {
    return <div>Yükleniyor...</div>; // ya da bir spinner
  }

  if (!auth.token) {
    console.log("Missing authentication token.");
    return <Navigate to="/" />;
  }

  if (requiredRole === "admin" && auth.userType !== "admin") {
    console.log("Access to the admin panel is denied.");
    return <Navigate to="/" />;
  } 

  if (requiredRole === "user" && auth.userType === "admin") {
    return <Navigate to="/" />;
  }

  return children;
};

export default ProtectedRoute;
