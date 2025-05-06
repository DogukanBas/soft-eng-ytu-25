import React from 'react'
import "./Accountant.css"
import { useAuth } from '../../contexts/AuthContext'
import Header from '../../shared/Header'
import Footer from '../../shared/Footer'
import { Link } from 'react-router'
import { Button } from "@mui/material"

import AccountBoxIcon from '@mui/icons-material/AccountBox';
import LocalActivityIcon from '@mui/icons-material/LocalActivity';
import WalletIcon from '@mui/icons-material/Wallet';
import LogoutIcon from '@mui/icons-material/Logout';

function Accountant() {
    const { logout } = useAuth()
    const handleLogout = () => {
        logout();
    };

    return (
        <>
            <Header />
            <div className="accountant-container">
                <div className="accountant-header">
                    <div className="accountant-title">
                        <AccountBoxIcon fontSize="large" />
                        <h1>Accountant Dashboard</h1>
                    </div>
                    <div className="accountant-logout">
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
                
                <div className="accountant-content">
                    <div className="accountant-card">
                        <div className="accountant-card-header">
                            <div className="accountant-card-icon">
                                <LocalActivityIcon />
                            </div>
                            <h3 className="accountant-card-title">Ticket Management</h3>
                        </div>
                        <p className="accountant-card-description">
                            List your tickets
                        </p>
                        <Link to="/accountant/list-tickets">
                            <Button 
                                variant="contained" 
                                fullWidth
                                className="accountant-button"
                                startIcon={<LocalActivityIcon />}
                            >
                                List Tickets
                            </Button>
                        </Link>
                    </div>
                    
                    <div className="accountant-card">
                        <div className="accountant-card-header">
                            <div className="accountant-card-icon">
                                <WalletIcon />
                            </div>
                            <h3 className="accountant-card-title">Budget Management</h3>
                        </div>
                        <p className="accountant-card-description">
                            Set your budgets
                        </p>
                        <Link to="/accountant/set-budget">
                            <Button 
                                variant="contained" 
                                fullWidth
                                className="accountant-button"
                                startIcon={<WalletIcon />}
                            >
                                Set Budget
                            </Button>
                        </Link>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}

export default Accountant