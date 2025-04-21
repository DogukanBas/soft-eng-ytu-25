import { useState, useEffect } from 'react'
import { 
  Box, Typography, Paper, Table, TableBody, TableCell, 
  TableContainer, TableHead, TableRow, CircularProgress 
} from '@mui/material'
import api from '../services/api'

export default function Budgets() {
  const [budgets, setBudgets] = useState([])
  const [suggestions, setSuggestions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [budgetsRes, suggestionsRes] = await Promise.all([
          api.getBudgets(),
          api.getBudgetSuggestions()
        ])
        setBudgets(budgetsRes.data)
        setSuggestions(suggestionsRes.data)
      } catch (err) {
        setError(err.response?.data?.message || 'Bütçe bilgileri yüklenirken bir hata oluştu')
      } finally {
        setLoading(false)
      }
    }
    
    fetchData()
  }, [])

  if (loading) return <CircularProgress />
  if (error) return <Typography color="error">{error}</Typography>

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Bütçe Yönetimi</Typography>
      
      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" gutterBottom>Mevcut Bütçeler</Typography>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Departman</TableCell>
                <TableCell>Gider Türü</TableCell>
                <TableCell>Yıllık Bütçe</TableCell>
                <TableCell>Kalan Bütçe</TableCell>
                <TableCell>Limit Aşım Eşiği</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {budgets.map((budget) => (
                <TableRow key={`${budget.department}-${budget.type}`}>
                  <TableCell>{budget.department}</TableCell>
                  <TableCell>{expenseTypes.find(t => t.value === budget.type)?.label || budget.type}</TableCell>
                  <TableCell>{budget.amount} TL</TableCell>
                  <TableCell>{budget.remaining} TL</TableCell>
                  <TableCell>{budget.threshold} TL</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
      
      <Paper elevation={3} sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom>Gelecek Yıl İçin Bütçe Önerileri</Typography>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Departman</TableCell>
                <TableCell>Gider Türü</TableCell>
                <TableCell>Önerilen Bütçe</TableCell>
                <TableCell>Önceki Yıl Harcaması</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {suggestions.map((suggestion) => (
                <TableRow key={`${suggestion.department}-${suggestion.type}`}>
                  <TableCell>{suggestion.department}</TableCell>
                  <TableCell>{expenseTypes.find(t => t.value === suggestion.type)?.label || suggestion.type}</TableCell>
                  <TableCell>{suggestion.suggestedAmount} TL</TableCell>
                  <TableCell>{suggestion.previousYearSpent} TL</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Box>
  )
}