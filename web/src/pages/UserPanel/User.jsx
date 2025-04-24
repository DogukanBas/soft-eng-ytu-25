import Header from "../../shared/Header"
import Footer from "../../shared/Footer"
import "./User.css"
import { Button } from "@mui/material"
import { Link } from "react-router"
import { useAuth } from "../../contexts/AuthContext"


function User() {
    const { logout } = useAuth()
    const handleLogout = () => {
        logout();
    };

    return (
        <>
        <Header />
        <div className="user-container">
            <h1>User Panel</h1>
            <Link to="/">
                        <Button onClick={handleLogout} sx={{width: "100%"}}>Log Out</Button>
            </Link>
        </div>
        <Footer />
        </>
    )
}

export default User