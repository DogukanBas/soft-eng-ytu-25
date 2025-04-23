import Header from "../../shared/Header"
import Footer from "../../shared/Footer"
import "./Admin.css"
import { Button } from "@mui/material"
import { Link } from "react-router"
import { setAuthToken } from "../../services/authService"


function Admin() {
    return (
        <>
            <Header />
            <div className="admin-container">
                <div className="buttons-container">
                    <h1>Admin Panel</h1>
                    <Link to="/admin/add-department">
                        <Button variant="contained" sx={{width: "100%"}}>Add Department</Button>
                    </Link>
                    <Link to="/admin/add-user">
                        <Button variant="contained" sx={{width: "100%"}}>Add User</Button>
                    </Link>
                </div>
                <Link to="/">
                        <Button onClick={() => setAuthToken(null)} sx={{width: "100%"}}>Log Out</Button>
                </Link>
            </div>
                
            <Footer />
        </>
    )
}

export default Admin