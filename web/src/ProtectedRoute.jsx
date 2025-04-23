import React from 'react'
import { Navigate, Outlet } from 'react-router-dom'
import { getAuthToken } from './services/authService.js'

const ProtectedRoute = () => {
    const token = getAuthToken(); // localStorage'dan token al
    return token ? <Outlet /> : <Navigate to="/" />
}

export default ProtectedRoute;
