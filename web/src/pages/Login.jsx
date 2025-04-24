import "./Login.css"
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import LoginIcon from '@mui/icons-material/Login';
import Header from "../shared/Header";
import Footer from "../shared/Footer";
import { useNavigate } from "react-router";
import { useState } from "react";
import { login } from '../services/authService';
import { useAuth } from "../contexts/AuthContext";


function Login(props) {
    const [personalNoOrEmail, setPersonalNoOrEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState('')
    const navigate = useNavigate()
    const { setAuth } = useAuth()

    

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        
        try {
          const response = await login(personalNoOrEmail, password);
          console.log(response);
          
          // Giriş başarılı ise token'i kaydet ve yönlendir
          setAuth({ token: response.accessToken, userType: response.userType });
          
          
          // Kullanıcı tipine göre yönlendirme yapabilirsiniz
          navigate(response.userType === 'admin' ? '/admin' : '/user');
          
        } catch (err) {
          setError('Giriş başarısız. Bilgilerinizi kontrol edin.');
          console.error(err);
        }
      };
    
    return (
        <>
            <Header />
            <div className="login-container">
                
                <form onSubmit={handleSubmit}>
                    <h1>CostMS Login</h1>
                    <TextField
                        id="personalNoOrEmail"
                        label="Personal No or Email"
                        type="text"
                        value={personalNoOrEmail}
                        autoComplete="current-email"
                        onChange={(e) => setPersonalNoOrEmail(e.target.value)}
                        required
                    />
                    <TextField
                        id="password"
                        label="Password"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <Button size="large" type="submit" variant="contained" endIcon={<LoginIcon />}>Log In</Button>
                </form>
            </div>
            <Footer />
        </>
    )
}

export default Login