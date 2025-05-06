import { createBrowserRouter, RouterProvider } from 'react-router';
import Login from './pages/LoginPanel/Login.jsx';
import Admin from "./pages/AdminPanel/Admin.jsx"
import AddDepartment from './pages/AdminPanel/AddDepartment.jsx';
import AddUser from './pages/AdminPanel/AddUser.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';
import Accountant from './pages/AccountantPanel/Accountant.jsx'
import ListTickets from './pages/AccountantPanel/ListTickets.jsx'
import SetBudget from './pages/AccountantPanel/SetBudget.jsx'



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
        path: "/accountant",
        element: <ProtectedRoute requiredRole="accountant"><Accountant /></ProtectedRoute>
      },
      {   
        path: "/accountant/list-tickets",
        element: <ProtectedRoute requiredRole="accountant"><ListTickets /></ProtectedRoute>
      },
      {   
        path: "/accountant/set-budget",
        element: <ProtectedRoute requiredRole="accountant"><SetBudget /></ProtectedRoute>
      },
      
    ]);

    return (
        <>
            <RouterProvider router={router} /> 
        </>
    )
  }

  export default App