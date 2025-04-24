import { createBrowserRouter, RouterProvider } from 'react-router';
import Login from './pages/Login.jsx';
import Admin from "./pages/AdminPanel/Admin.jsx"
import AddDepartment from './pages/AdminPanel/AddDepartment.jsx';
import AddUser from './pages/AdminPanel/AddUser.jsx';
import User from './pages/UserPanel/User.jsx';
import { useEffect, useState } from 'react';
import ProtectedRoute from './ProtectedRoute.jsx';



  function App() {
   
    const router = createBrowserRouter([
      { 
        path: "/", 
        element: <Login /> 
      },

      {
        path: "/admin",
        element: <ProtectedRoute requiredRole="admin"><Admin /></ProtectedRoute>
      
      },

      {
        path: "/admin/add-department",
        element: <ProtectedRoute requiredRole="admin"><AddDepartment /></ProtectedRoute>
      },

      {
        path: "/admin/add-user",
        element: <ProtectedRoute requiredRole="admin"><AddUser /></ProtectedRoute>
      },
        
      {   
        path: "/user",
        element: <ProtectedRoute requiredRole="user"><User /></ProtectedRoute>
      },
      
    ]);

    return (
        <>
            <RouterProvider router={router} /> 
        </>
    )
  }

  export default App