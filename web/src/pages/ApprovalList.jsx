import { useState, useEffect } from 'react'
import { 
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, Typography, Box, Button, ButtonGroup, CircularProgress 
} from '@mui/material'
import api from '../services/api'

export default function ApprovalList() {
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchPendingApprovals = async () => {
      try {
        const { data } = await api.getPendingApprovals()
        setExpenses(data)
      } catch (err) {
        setError(err.response?.data?.message || 'Onay bekleyen giderler yüklenirken bir hata oluştu')
      } finally {
        setLoading(false)
      }
    }
    
    fetchPendingApprovals()
  }, [])

  const handleApproval = async (id, decision) => {
    try {
      await api.approveExpense(id, decision)
      setExpenses(prev => prev.filter(exp => exp.id !== id))
    } catch (err) {
      setError(err.response?.data?.message || 'Onay işlemi sırasında bir hata oluştu')
    }
  }

  if (loading) return <CircularProgress />
  if (error) return <Typography color="error">{error}</Typography>

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Onay Bekleyen Giderler</Typography>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Çalışan</TableCell>
              <TableCell>Tarih</TableCell>
              <TableCell>Tür</TableCell>
              <TableCell>Tutar</TableCell>
              <TableCell>Açıklama</TableCell>
              <TableCell>İşlemler</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {expenses.map((expense) => (
              <TableRow key={expense.id}>
                <TableCell>{expense.employeeName}</TableCell>
                <TableCell>{new Date(expense.date).toLocaleDateString()}</TableCell>
                <TableCell>{expenseTypes.find(t => t.value === expense.type)?.label || expense.type}</TableCell>
                <TableCell>{expense.amount} TL</TableCell>
                <TableCell>{expense.description}</TableCell>
                <TableCell>
                  <ButtonGroup>
                    <Button 
                      color="success" 
                      onClick={() => handleApproval(expense.id, true)}
                    >
                      Onayla
                    </Button>
                    <Button 
                      color="error" 
                      onClick={() => handleApproval(expense.id, false)}
                    >
                      Reddet
                    </Button>
                  </ButtonGroup>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  )
}