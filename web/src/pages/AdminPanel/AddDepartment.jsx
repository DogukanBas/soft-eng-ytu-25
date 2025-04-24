import Header from "../../shared/Header"
import Footer from "../../shared/Footer"
import "./AddDepartment.css"
import { Button, TextField } from "@mui/material"
import { Link } from "react-router"
import { addDepartment } from "../../services/adminService"
import { useState } from "react"
import Alert from '@mui/material/Alert';
import CheckIcon from '@mui/icons-material/Check';

function AddDepartment() {
    const [deptName, setDeptName] = useState("")

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await addDepartment(deptName)
            console.log(response);
            setDeptName("");
            

            
            
            
        } catch (error) {
            console.log(error);
            
            
        }
    }
    return (
        <>
            <Header />
            <div className="add-department-container">
                <form onSubmit={handleSubmit}>
                    <TextField onChange={(e) => setDeptName(e.target.value)} id="dept-name" label="Department Name" variant="outlined" value={deptName} required/>
                    <Button type="submit" variant="outlined">Add Department</Button> 
                </form>
                <Link to="/admin"><Button sx={{width: "100%"}}>Back to Admin Panel</Button></Link> 
                
            </div>
            <Footer />
        </>
    )
}

export default AddDepartment