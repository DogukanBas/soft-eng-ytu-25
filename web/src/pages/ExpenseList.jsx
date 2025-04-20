import { useState, useEffect } from 'react'
import { 
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, Typography, Box, Chip, CircularProgress 
} from '@mui/material'
import api from '../services/api'

const statusColors = {
  PENDING: 'warning',
  APPROVED: 'success',
  REJECTED: 'error',
  PAID: 'info'
}

export default function ExpenseList() {
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchExpenses = async () => {
      try {
        const { data } = await api.getExpenses()
        setExpenses(data)
      } catch (err) {
        setError(err.response?.data?.message || 'Giderler yüklenirken bir hata oluştu')
      } finally {
        setLoading(false)
      }
    }
    
    fetchExpenses()
  }, [])

  if (loading) return <CircularProgress />
  if (error) return <Typography color="error">{error}</Typography>

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Giderlerim</Typography>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Tarih</TableCell>
              <TableCell>Tür</TableCell>
              <TableCell>Tutar</TableCell>
              <TableCell>Açıklama</TableCell>
              <TableCell>Durum</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {expenses.map((expense) => (
              <TableRow key={expense.id}>
                <TableCell>{new Date(expense.date).toLocaleDateString()}</TableCell>
                <TableCell>{expenseTypes.find(t => t.value === expense.type)?.label || expense.type}</TableCell>
                <TableCell>{expense.amount} TL</TableCell>
                <TableCell>{expense.description}</TableCell>
                <TableCell>
                  <Chip 
                    label={expense.status} 
                    color={statusColors[expense.status] || 'default'} 
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  )
}