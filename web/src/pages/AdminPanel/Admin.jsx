import Header from "../../shared/Header"
import Footer from "../../shared/Footer"
import "./Admin.css"
import { Button } from "@mui/material"
import { Link } from "react-router"
import { useAuth } from "../../contexts/AuthContext"
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import BusinessIcon from '@mui/icons-material/Business';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import LogoutIcon from '@mui/icons-material/Logout';

function Admin() {
    const { logout } = useAuth()
    const handleLogout = () => {
        logout();
    };

    return (
        <>
            <Header />
            <div className="admin-container">
                <div className="admin-header">
                    <div className="admin-title">
                        <AdminPanelSettingsIcon fontSize="large" />
                        <h1>Admin Dashboard</h1>
                    </div>
                    <div className="admin-logout">
                        <Link to="/">
                            <Button 
                                variant="outlined" 
                                color="error" 
                                onClick={handleLogout}
                                startIcon={<LogoutIcon />}
                            >
                                Log Out
                            </Button>
                        </Link>
                    </div>
                </div>
                
                <div className="admin-content">
                    <div className="admin-card">
                        <div className="admin-card-header">
                            <div className="admin-card-icon">
                                <BusinessIcon />
                            </div>
                            <h3 className="admin-card-title">Department Management</h3>
                        </div>
                        <p className="admin-card-description">
                            Create new departments
                        </p>
                        <Link to="/admin/add-department">
                            <Button 
                                variant="contained" 
                                fullWidth
                                className="admin-button"
                                startIcon={<BusinessIcon />}
                            >
                                Add Department
                            </Button>
                        </Link>
                    </div>
                    
                    <div className="admin-card">
                        <div className="admin-card-header">
                            <div className="admin-card-icon">
                                <PersonAddIcon />
                            </div>
                            <h3 className="admin-card-title">User Management</h3>
                        </div>
                        <p className="admin-card-description">
                            Add new users
                        </p>
                        <Link to="/admin/add-user">
                            <Button 
                                variant="contained" 
                                fullWidth
                                className="admin-button"
                                startIcon={<PersonAddIcon />}
                            >
                                Add User
                            </Button>
                        </Link>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}

export default Admin