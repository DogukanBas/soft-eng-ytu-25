import axios from 'axios';

// Token eklemek için interceptor
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getDepartments = async () => {
  try {
    const response = await axios.get("/api/accountant/departments");
    console.log(response);
    return response.data.departments;
  } catch (error) {
    console.error('Get departments error:', error.response?.data || error.message);
  }
}

export const setDepartmentInitialBudget = async (data) => {
  try {
    const response = await axios.post("/api/accountant/departments/set-initial-budget", data);
    console.log(response);
    alert("Initial Budget Setted Successfully")
    return response;
  } catch (error) {
    console.error('Set department inital budget error:', error.response?.data || error.message);
    alert("An Error Occured While Setting Initial Budget")
  }
}

export const setDepartmentRemainingBudget = async (data) => {
  try {
    const response = await axios.post("/api/accountant/departments/set-remaining-budget", data);
    console.log(response);
    alert("Remaining Budget Setted Successfully")
    return response;
  } catch (error) {
    console.error('Set department remaining budget error:', error.response?.data || error.message);
    alert("An Error Occured While Setting Remaining Budget")
  }
}

export const getAllCostTypes = async () => {
  try {
    const response = await axios.get("/api/accountant/cost-types");
    console.log(response);
    return response.data.costTypes;
  } catch (error) {
    console.error('Get cost type error:', error.response?.data || error.message);
  }
}

export const setInitialBudgetByTypeName = async (data) => {
  try {
    const response = await axios.post("/api/accountant/cost-types/set-initial-budget", data);
    console.log(response);
    alert("Initial Budget Setted Successfully")
    return response;
  } catch (error) {
    console.error('Set inital budget by type name error:', error.response?.data || error.message);
    alert("An Error Occured While Setting Initial Budget")
  }
}

export const setRemainingBudgetByTypeName = async (data) => {
  try {
    const response = await axios.post("/api/accountant/cost-types/set-remaining-budget", data);
    console.log(response);
    alert("Remaining Budget Setted Successfully")
    return response;
  } catch (error) {
    console.error('Set remaining budget by type name error:', error.response?.data || error.message);
    alert("An Error Occured While Setting Remaining Budget")
  }
}


export const setMaxCostByTypeName = async (data) => {
  try {
    const response = await axios.post("/api/accountant/cost-types/set-max-cost", data);
    console.log(response);
    alert("Max Cost Setted Successfully")
    return response;
  } catch (error) {
    console.error('Set max cost by type name error:', error.response?.data || error.message);
    alert("An Error Occured While Setting Max Cost")
  }
}

export const resetBudgetByTypeName = async (data) => {
  try {
    const response = await axios.post("/api/accountant/cost-types/reset-budget", data);
    console.log(response);
    alert("Remaining Budget Resetted Successfully ")
    return response;
  } catch (error) {
    console.error('Reset budget by type name error:', error.response?.data || error.message);
    alert("An Error Occured While Resetting Remaining Budget")
  }
}

export const addCostType = async (data) => {
  try {
    const response = await axios.post("/api/accountant/cost-types/add", data);
    console.log(response);
    alert("New Cost Type Added Successfully")
    return response;
  } catch (error) {
    console.error('Reset budget by type name error:', error.response?.data || error.message);
    alert("An Error Occured While Adding New Cost Type")
  }
}



