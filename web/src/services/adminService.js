import axios from 'axios';

const API_BASE_URL = ''; // Backend adresi

// Token eklemek için interceptor
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const addEmployee = async (employeeData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/api/admin/add-employee`, employeeData);
    return response.data;
  } catch (error) {
    console.error('Add employee error:', error.response?.data || error.message);
    throw error;
  }
};

export const addDepartment = async (deptName) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/api/admin/add-department`, { deptName });
    return response.data;
  } catch (error) {
    console.error('Add department error:', error.response?.data || error.message);
    throw error;
  }
};

export const getDepartments = async () => {
  try {
    const response = await axios.get("/api/admin/departments");
    return response.data.departments;
    

  } catch (error) {
    console.error('Get departments error:', error.response?.data || error.message);
  }
}