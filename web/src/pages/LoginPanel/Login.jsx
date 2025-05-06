import "./Login.css";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import LoginIcon from '@mui/icons-material/Login';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import Header from "../../shared/Header";
import Footer from "../../shared/Footer";
import { useNavigate } from "react-router";
import { useState } from "react";
import { login } from '../../services/authService';
import { useAuth } from "../../contexts/AuthContext";

function Login(props) {
    const [personalNoOrEmail, setPersonalNoOrEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { setAuth } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const response = await login(personalNoOrEmail, password);
            console.log(response);

            setAuth({ token: response.accessToken, userType: response.userType });
            if (response.userType === 'admin') {
                navigate("/admin")
            } else if (response.userType === 'accountant') {
                navigate("/accountant")
            }
            
        } catch (err) {
            setError('Login failed. Please check your credentials.');
            console.error(err);
        }
    };

    return (
        <>
            <Header />
            <main className="login-container">
                <div className="login-card">
                    <div className="login-header">
                        <AccountBalanceWalletIcon sx={{ fontSize: 40, mb: 1 }} />
                        <h1>Welcome to CostMS</h1>
                        <p>Sign in to access your account</p>
                    </div>
                    
                    <form className="login-form" onSubmit={handleSubmit}>
                        {error && (
                            <div className="error-message">
                                <ErrorOutlineIcon fontSize="small" />
                                {error}
                            </div>
                        )}
                        
                        <div className="form-group">
                            <TextField
                                id="personalNoOrEmail"
                                label="Personal No or Email"
                                type="text"
                                value={personalNoOrEmail}
                                autoComplete="current-email"
                                onChange={(e) => setPersonalNoOrEmail(e.target.value)}
                                required
                                fullWidth
                                variant="outlined"
                                
                            />
                        </div>
                        
                        <div className="form-group">
                            <TextField
                                id="password"
                                label="Password"
                                type="password"
                                autoComplete="current-password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                fullWidth
                                variant="outlined"
                            />
                        </div>
                        
                        <Button 
                            className="login-button"
                            size="large" 
                            type="submit" 
                            variant="contained" 
                            endIcon={<LoginIcon />}
                            fullWidth
                        >
                            Log In
                        </Button>
                    </form>
                    
                    <div className="login-footer">
                        <p>© 2025 CostMS</p>
                    </div>
                </div>
            </main>
            <Footer />
        </>
    );
}

export default Login;