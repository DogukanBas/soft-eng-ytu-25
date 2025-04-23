import Header from "../../shared/Header"
import Footer from "../../shared/Footer"
import "./AddUser.css"
import { use, useEffect, useState } from "react"
import { InputLabel, TextField, FormControl, Button, FormLabel } from "@mui/material"
import { Select } from "@mui/material"
import { MenuItem } from "@mui/material"
import { Link } from "react-router"
import { addEmployee, getDepartments } from "../../services/adminService"
import axios from "axios"

function AddUser() {  
    const [userData, setUserData] = useState({name:"", surname:"", personalNo:"", email:"", password:"", userType:"", deptName:""});
    // const getDepartments = {departments: ["IT", "Finance", "Tech", "HR"]}
    const [departments, setDepartments] = useState([]);

    const departmentsLoader = async () => {
        try {
            const data = await getDepartments()
            setDepartments(data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        departmentsLoader();
    }, [])

    
   
    const handleSubmit = async (e) => {
            e.preventDefault();
            try {
                const response = await addEmployee(userData)
                console.log(response);
                alert("User added successfully")
                setUserData({name:"", surname:"", personalNo:"", email:"", password:"", userType:"", deptName:""})
            } catch (error) {
                console.log(error);
                alert("User could not be added")
                
            }
        }
        

    const handleChange = (event) => {
      
        const { name, value } = event.target;
        setUserData((prevUserData) => (
            {
            ...prevUserData,
            [name]: value
            }
        ));

    };
    
    useEffect(() => {

    }, [])

    return (
        <>
            <Header />
            <div className="add-user-container">
                <form onSubmit={handleSubmit}>
                        <TextField id="name" name="name" label="Name" variant="outlined" type="text" value={userData.name} onChange={handleChange} required/>
                        <TextField id="surname" name="surname" label="Surname" variant="outlined" type="text" value={userData.surname} onChange={handleChange} required/>
                        <TextField id="personalNo" name="personalNo" label="Personal No" variant="outlined" type="number" value={userData.personalNo} onChange={handleChange} required/>
                        <TextField id="email" name="email" label="Email" variant="outlined" type="email" value={userData.email} onChange={handleChange} required/>
                        <TextField id="password" name="password" label="Password" variant="outlined" type="password" value={userData.password} onChange={handleChange} required/>
                        <FormControl>
                            <InputLabel id="user-type-label">Account Type</InputLabel>
                            <Select
                                labelId="user-type-label"
                                id="user-type"
                                value={userData.userType}
                                label="User Type"
                                onChange={handleChange}
                                name="userType"
                                required
                            >
                                <MenuItem value={"team_member"}>Team Member</MenuItem>
                                <MenuItem value={"manager"}>Manager</MenuItem>
                                <MenuItem value={"accountant"}>Accountant</MenuItem>
                                <MenuItem value={"admin"}>Admin</MenuItem>
                            </Select>
                        </FormControl>
                        <FormControl>
                            <InputLabel id="dept-name-label">Department Name</InputLabel>
                            <Select
                                labelId="dept-name-label"
                                id="dept-name"
                                value={userData.deptName}
                                label="Department Name"
                                onChange={handleChange}
                                name="deptName"
                                required
                            >
                                {/* <MenuItem value={"IT"}>IT</MenuItem> */}
                                {departments.map((el, index) => <MenuItem key={index} value={el}>{el}</MenuItem>)}

                            </Select>
                        </FormControl>
                        <Button variant="outlined" type="submit">Add User</Button>
                                          
                </form>
                <Link to="/admin"><Button sx={{width: "100%"}}>Back to Admin Panel</Button></Link>
            </div>
            <Footer />
        </>
    )
}

export default AddUser