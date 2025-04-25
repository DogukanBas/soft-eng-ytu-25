import Header from "../../shared/Header"
import Footer from "../../shared/Footer"
import "./AddDepartment.css"
import { Button, TextField } from "@mui/material"
import { Link } from "react-router"
import { addDepartment } from "../../services/adminService"
import { useState } from "react"
import Alert from '@mui/material/Alert';
import CheckIcon from '@mui/icons-material/Check';
import BusinessIcon from '@mui/icons-material/Business';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

function AddDepartment() {
    const [deptName, setDeptName] = useState("")
    const [success, setSuccess] = useState(false)
    const [error, setError] = useState("")

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSuccess(false);
        setError("");
        
        try {
            const response = await addDepartment(deptName)
            console.log(response);
            setSuccess(true);
            setDeptName("");
        } catch (error) {
            console.log(error);
            setError("Failed to add department. Please try again.");
        }
    }

    return (
        <>
            <Header />
            <div className="add-department-container">
                <div className="department-card">
                    <div className="form-header">
                        <div className="form-header-icon">
                            <BusinessIcon fontSize="large" />
                        </div>
                        <div>
                            <h2 className="form-title">Add New Department</h2>
                            <p className="form-subtitle">Create a new department in the system</p>
                        </div>
                    </div>

                    {success && (
                        <Alert 
                            icon={<CheckIcon fontSize="inherit" />} 
                            severity="success"
                            className="success-message"
                            sx={{ mb: 2 }}
                        >
                            Department successfully added!
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
                    
                    <form className="department-form" onSubmit={handleSubmit}>
                        <div className="form-row">
                            <TextField 
                                onChange={(e) => setDeptName(e.target.value)} 
                                id="dept-name" 
                                label="Department Name" 
                                variant="outlined" 
                                value={deptName} 
                                fullWidth
                                required
                                placeholder="Enter department name"
                            />
                        </div>
                        
                        <div className="form-actions">
                            <Link to="/admin">
                                <Button variant="outlined" startIcon={<ArrowBackIcon />}>
                                    Cancel
                                </Button>
                            </Link>
                            <Button 
                                type="submit" 
                                variant="contained" 
                                color="primary"
                                startIcon={<BusinessIcon />}
                            >
                                Add Department
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

export default AddDepartment