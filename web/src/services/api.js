import axios from 'axios'

const API_URL = 'http://localhost:8080/api' // SpringBoot backend URL'i

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Auth token'ını eklemek için interceptor
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default {
  // Kullanıcı işlemleri
  login(email, password) {
    return api.post('/auth/login', { email, password })
  },
  
  // Gider işlemleri
  addExpense(expense) {
    return api.post('/expenses', expense)
  },
  getExpenses() {
    return api.get('/expenses')
  },
  getExpenseById(id) {
    return api.get(`/expenses/${id}`)
  },
  
  // Onay işlemleri
  getPendingApprovals() {
    return api.get('/approvals/pending')
  },
  approveExpense(id, decision) {
    return api.put(`/approvals/${id}`, { approved: decision })
  },
  
  // Rapor işlemleri
  getExpenseReports(params) {
    return api.get('/reports/expenses', { params })
  },
  
  // Bütçe işlemleri
  getBudgets() {
    return api.get('/budgets')
  },
  getBudgetSuggestions() {
    return api.get('/budgets/suggestions')
  },

  //Dashboard
  getDashboardStats() {
    return api.get('/dashboard/stats')
  }
}