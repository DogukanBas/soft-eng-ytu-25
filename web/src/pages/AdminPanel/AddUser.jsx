// AddUser.jsx
import Header from "../../shared/Header"
import Footer from "../../shared/Footer"
import "./AddUser.css"
import { useEffect, useState } from "react"
import { TextField, FormControl, Button, Alert } from "@mui/material"
import { Select, InputLabel } from "@mui/material"
import { MenuItem } from "@mui/material"
import { Link } from "react-router"
import { addEmployee, getDepartments } from "../../services/adminService"
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import BadgeIcon from '@mui/icons-material/Badge';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import BusinessIcon from '@mui/icons-material/Business';
import CheckIcon from '@mui/icons-material/Check';

function AddUser() {  
    const [userData, setUserData] = useState({
        name: "",
        surname: "",
        personalNo: "",
        email: "",
        password: "",
        userType: "",
        deptName: ""
    });
    const [departments, setDepartments] = useState([]);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState("");
   
    const departmentsLoader = async () => {
        try {
            const data = await getDepartments()
            setDepartments(data);
        } catch (error) {
            console.error(error);
            setError("Failed to load departments. Please refresh the page.");
        }
    }

    useEffect(() => {
        departmentsLoader();
    }, [])
   
    const handleSubmit = async (e) => {
        e.preventDefault();
        setSuccess(false);
        setError("");

        try {
            const response = await addEmployee(userData)
            console.log(response);
            setSuccess(true);
            setUserData({
                name: "",
                surname: "",
                personalNo: "",
                email: "",
                password: "",
                userType: "",
                deptName: ""
            });
        } catch (error) {
            console.log(error);
            setError("Failed to add user. Please check the information and try again.");
        }
    }

    const handleChange = (event) => {
        const { name, value } = event.target;
        setUserData((prevUserData) => ({
            ...prevUserData,
            [name]: value
        }));
    };

    return (
        <>
            <Header />
            <div className="add-user-container">
                <div className="user-card">
                    <div className="form-header">
                        <div className="form-header-icon">
                            <PersonAddIcon fontSize="large" />
                        </div>
                        <div>
                            <h2 className="form-title">Add New User</h2>
                            <p className="form-subtitle">Create a new user account</p>
                        </div>
                    </div>

                    {success && (
                        <Alert 
                            icon={<CheckIcon fontSize="inherit" />}
                            severity="success"
                            className="success-message"
                            sx={{ mb: 2 }}
                        >
                            User successfully added!
                        </Alert>
                    )}

                    {error && (
                        <Alert 
                            severity="error"
                            sx={{ mb: 2 }}
                        >
                            {error}
                        </Alert>
                    )}
                    
                    <form className="user-form" onSubmit={handleSubmit}>
                        <div className="section-divider">
                            <BadgeIcon fontSize="small" sx={{ mr: 1 }} />
                            Personal Information
                        </div>

                        <div className="form-group">
                            <TextField
                                id="name"
                                name="name"
                                label="First Name"
                                variant="outlined"
                                type="text"
                                value={userData.name}
                                onChange={handleChange}
                                fullWidth
                                required
                            />
                            <TextField
                                id="surname"
                                name="surname"
                                label="Last Name"
                                variant="outlined"
                                type="text"
                                value={userData.surname}
                                onChange={handleChange}
                                fullWidth
                                required
                            />
                        </div>

                        <div className="form-group">
                            <TextField
                                id="personalNo"
                                name="personalNo"
                                label="Personal No"
                                variant="outlined"
                                type="number"
                                value={userData.personalNo}
                                onChange={handleChange}
                                fullWidth
                                required
                            />
                            <TextField
                                id="email"
                                name="email"
                                label="Email Address"
                                variant="outlined"
                                type="email"
                                value={userData.email}
                                onChange={handleChange}
                                fullWidth
                                required
                            />
                        </div>

                        <div className="section-divider">
                            <VpnKeyIcon fontSize="small" sx={{ mr: 1 }} />
                            Account Details
                        </div>

                        <div className="form-group">
                            <TextField
                                id="password"
                                name="password"
                                label="Password"
                                variant="outlined"
                                type="password"
                                value={userData.password}
                                onChange={handleChange}
                                fullWidth
                                required
                            />
                            <FormControl fullWidth required>
                                <InputLabel id="user-type-label">Account Type</InputLabel>
                                <Select
                                    labelId="user-type-label"
                                    id="user-type"
                                    value={userData.userType}
                                    label="Account Type"
                                    onChange={handleChange}
                                    name="userType"
                                >
                                    <MenuItem value={"team_member"}>Team Member</MenuItem>
                                    <MenuItem value={"manager"}>Manager</MenuItem>
                                    <MenuItem value={"accountant"}>Accountant</MenuItem>
                                </Select>
                            </FormControl>
                        </div>

                        <div className="section-divider">
                            <BusinessIcon fontSize="small" sx={{ mr: 1 }} />
                            Department Assignment
                        </div>

                        <FormControl fullWidth required>
                            <InputLabel id="dept-name-label">Department</InputLabel>
                            <Select
                                labelId="dept-name-label"
                                id="dept-name"
                                value={userData.deptName}
                                label="Department"
                                onChange={handleChange}
                                name="deptName"
                            >
                                {departments.map((el, index) => (
                                    <MenuItem key={index} value={el}>{el}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        
                        <div className="form-actions">
                            <Link to="/admin">
                                <Button variant="outlined" startIcon={<ArrowBackIcon />}>
                                    Cancel
                                </Button>
                            </Link>
                            <Button 
                                variant="contained" 
                                type="submit"
                                startIcon={<PersonAddIcon />}
                            >
                                Add User
                            </Button>
                        </div>
                    </form>
                </div>

                <div className="back-link">
                    <Link to="/admin">
                        <Button 
                            variant="text" 
                            startIcon={<ArrowBackIcon />}
                        >
                            Back to Admin Panel
                        </Button>
                    </Link>
                </div>
            </div>
            <Footer />
        </>
    )
}

export default AddUser