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
        element: <ProtectedRoute />,
        children: [
          {
            path: "/admin",
            element: <Admin />,
          },
          {
            path: "/admin/add-department",
            element: <AddDepartment />
          },
          {
            path: "/admin/add-user",
            element: <AddUser />
          },

          {
            path: "/user",
            element: <User />
          } 
        ]
      }
    ]);

    return (
        <>
            <RouterProvider router={router} /> 
        </>
    )
  }

  export default App