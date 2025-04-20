import './App.css'

import { Routes, Route } from 'react-router-dom'
import Layout from './components/Layout'
import Dashboard from './pages/Dashboard'
import ExpenseForm from './pages/ExpenseForm'
import ExpenseList from './pages/ExpenseList'
import ApprovalList from './pages/ApprovalList'
import Reports from './pages/Reports'
import Budgets from './pages/Budgets'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Dashboard />} />
        <Route path="expense-form" element={<ExpenseForm />} />
        <Route path="expense-list" element={<ExpenseList />} />
        <Route path="approval-list" element={<ApprovalList />} />
        <Route path="reports" element={<Reports />} />
        <Route path="budgets" element={<Budgets />} />
      </Route>
    </Routes>
  )
}

export default App
