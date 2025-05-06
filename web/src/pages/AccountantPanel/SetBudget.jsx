import React from 'react'
import Header from '../../shared/Header'
import Footer from '../../shared/Footer'
import "./SetBudget.css"
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import { getAllCostTypes, getDepartments, setDepartmentInitialBudget, setDepartmentRemainingBudget, setInitialBudgetByTypeName, setRemainingBudgetByTypeName, setMaxCostByTypeName, resetBudgetByTypeName, addCostType } from '../../services/accountantService';
import { useEffect } from 'react';
import { useState } from 'react';
import { Button, TextField } from '@mui/material';
import { useNavigate } from "react-router";

import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Alert from '@mui/material/Alert';

function SetBudget() {
    const navigate = useNavigate();
    const [alert, setAlert] = useState(null);

    const [departments, setDepartments] = useState([]);
    const [selectedDept, setSelectedDept] = useState({
        name: "",
        initialBudget: 0,
        remainingBudget: 0,
        maxCost: null,
    });

    const [costTypes, setCostTypes] = useState([]);
    const [selectedCost, setSelectedCost] = useState({
        name: "",
        initialBudget: 0,
        remainingBudget: 0,
        maxCost: 0,
    });

    const [open, setOpen] = useState(false);
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

    const costTypeLoader = async () => {
        try {
            const data = await getAllCostTypes()
            setCostTypes(data);
        } catch (error) {
            console.error(error);
            setError("Failed to load departments. Please refresh the page.");
        }
    }

    

    useEffect(() => {
        departmentsLoader();
    }, [selectedDept])

    useEffect(() => {
        costTypeLoader();
    }, [selectedCost])

    
    const handleClickOpen = () => {
        setOpen(true);
    };
    
    const handleClose = () => {
        setOpen(false);
    };


    return (
    <>
        <Header />
        <div className='setbudget-container'>
            <div style={{display: "flex", justifyContent: "space-around", alignItems: "center", height: "100%", width: "100%"}}>
                <div className='deptb-container'>
                    <div style={{padding: "1rem", display: "flex", flexDirection: "column", gap: "1rem"}}>
                        <h1>Set Department Budget</h1>
                        <FormControl fullWidth>
                            <InputLabel id="dept-name-label">Department</InputLabel>
                            <Select
                                labelId="dept-name-label"
                                id="dept-name"
                                value={selectedDept.name}
                                label="Department"
                                onChange={(event) => {
                                    const deptObj = departments.find(dept => dept.name === event.target.value);
                                    setSelectedDept(deptObj);
                                    console.log(selectedDept);
                                }}
                                name="deptName"
                            >
                                {departments?.map((el, index) => (
                                    <MenuItem key={index} value={el.name}>
                                        {el.name}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <div style={{display: "flex"}}>
                            <TextField
                                required
                                id="outlined-required"
                                label="Initial Budget"
                                value={selectedDept.initialBudget}
                                type='number'
                                onChange={(e) => setSelectedDept(prev => ({
                                    ...prev,
                                    initialBudget: e.target.value
                                }))}
                            />
                            <Button onClick={async () => {
                                const data = new URLSearchParams();
                                data.append('deptName', selectedDept.name);
                                data.append('initialBudget', selectedDept.initialBudget);
                                const response = await setDepartmentInitialBudget(data);
                            }}>
                                Set
                            </Button>
                        </div>
                        <div style={{display: "flex"}}>
                            <TextField
                                required
                                id="outlined-required"
                                label="Remaining Budget"
                                value={selectedDept.remainingBudget}
                                type='number'
                                onChange={(e) => setSelectedDept(prev => ({
                                    ...prev,
                                    remainingBudget: e.target.value
                                }))}
                            />
                            <Button onClick={() => {
                                const data = new URLSearchParams();
                                data.append('deptName', selectedDept.name);
                                data.append('remainingBudget', selectedDept.remainingBudget);
                                const response = setDepartmentRemainingBudget(data);
                                return (response.status === 200) ? <Alert severity="success">This is a success Alert.</Alert> : <Alert severity="warning">This is a warning Alert.</Alert>
                            }}>
                                Set
                            </Button>
                        </div>
                    </div>
                </div>
                <div className='costtypeb-container'>
                    <div style={{padding: "1rem", display: "flex", flexDirection: "column", gap: "1rem"}}>
                        <h1>Set Cost Type Budget</h1>
                        <div style={{display: "flex", alignItems: "center", gap: "1rem"}}>
                        <FormControl sx={{width: "80%"}}>
                        <InputLabel id="cost-type-label">Cost Type</InputLabel>
                            <Select
                                labelId="cost-type-label"
                                id="cost-type"
                                value={selectedCost.name}
                                label="Cost Type"
                                onChange={(event) => {
                                    const costObj = costTypes.find(costType => costType.name === event.target.value);
                                    setSelectedCost(costObj);
                                    console.log(selectedCost);
                                }}
                            >
                                {costTypes?.map((el, index) => (
                                    <MenuItem key={index} value={el.name}>
                                        {el.name}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Fab size="small" color="primary" aria-label="add" onClick={handleClickOpen}>
                            <AddIcon />
                        </Fab>
                        <Dialog
                            open={open}
                            onClose={handleClose}
                            slotProps={{
                            paper: {
                                component: 'form',
                                onSubmit: async (event) => {
                                event.preventDefault();
                                const formData = new FormData(event.currentTarget);
                                const formJson = Object.fromEntries(formData.entries());
                                const data = new URLSearchParams();
                                data.append('costTypeName', formJson.costTypeName);
                                data.append('initialBudget', formJson.initialBudget);
                                data.append('maxCost', formJson.maxCost);
                                const response = await addCostType(data);
                                costTypeLoader();
                                handleClose();
                                },
                            },
                            }}
                        >
                            <DialogTitle>Add new Cost Type</DialogTitle>
                            <DialogContent>
                                <TextField
                                    autoFocus
                                    required
                                    margin="dense"
                                    id="cost-type-name"
                                    name="costTypeName"
                                    label="Name of the new Cost Type"
                                    type="text"
                                    fullWidth
                                />
                                <TextField
                                    autoFocus
                                    required
                                    margin="dense"
                                    id="intial-budget"
                                    name="initialBudget"
                                    label="Initial Budget"
                                    type="number"
                                    sx={{width: "10rem"}}
                                    
                                />
                                <TextField
                                    autoFocus
                                    required
                                    margin="dense"
                                    id="max-cost"
                                    name="maxCost"
                                    label="Max Cost"
                                    type="number"
                                    sx={{width: "10rem", display: "block"}}
                                    
                                />
                            </DialogContent>
                            <DialogActions>
                            <Button onClick={handleClose}>Cancel</Button>
                            <Button type="submit">Add</Button>
                            </DialogActions>
                        </Dialog>
                        </div>
                        <div style={{display: "flex"}}>
                            <TextField
                                required
                                id="outlined-required"
                                label="Initial Budget"
                                value={selectedCost.initialBudget}
                                type='number'
                                onChange={(e) => setSelectedCost(prev => ({
                                    ...prev,
                                    initialBudget: e.target.value
                                }))}
                            />
                            <Button onClick={() => {
                                const data = new URLSearchParams();
                                data.append('typeName', selectedCost.name);
                                data.append('initialBudget', selectedCost.initialBudget);
                                const response = setInitialBudgetByTypeName(data);
                            }}>
                                Set
                            </Button>
                        </div>
                        <div style={{display: "flex"}}>
                            <TextField
                                required
                                id="outlined-required"
                                label="Remaining Budget"
                                value={selectedCost.remainingBudget}
                                type='number'
                                onChange={(e) => setSelectedCost(prev => ({
                                    ...prev,
                                    remainingBudget: e.target.value
                                }))}
                            />
                            <Button onClick={() => {
                                const data = new URLSearchParams();
                                data.append('typeName', selectedCost.name);
                                data.append('remainingBudget', selectedCost.remainingBudget);
                                const response = setRemainingBudgetByTypeName(data);
                            }}>
                                Set
                            </Button>
                        </div>
                        <div style={{display: "flex"}}>
                            <TextField
                                required
                                id="outlined-required"
                                label="Max Budget"
                                value={selectedCost.maxCost}
                                type='number'
                                onChange={(e) => setSelectedCost(prev => ({
                                    ...prev,
                                    maxCost: e.target.value
                                }))}
                            />
                            <Button onClick={() => {
                                const data = new URLSearchParams();
                                data.append('typeName', selectedCost.name);
                                data.append('maxCost', selectedCost.maxCost);
                                const response = setMaxCostByTypeName(data);
                            }}>
                                Set
                            </Button>
                        </div>
                        <Button variant='contained' onClick={() => {
                            const data = new URLSearchParams();
                            data.append('typeName', selectedCost.name);
                            const response = resetBudgetByTypeName(data);
                            setSelectedCost((prevData) => ({
                                ...prevData,
                                remainingBudget: selectedCost.initialBudget
                            }));
                        }}>
                            Reset Remaining to Initial    
                        </Button>        

                    </div>                
                </div>
                
            </div>
            <Button variant='text' onClick={() => navigate("/accountant")}>
                Back to Main Page    
            </Button>
            
        </div>
        <Footer />
    </>
  )
}

export default SetBudget