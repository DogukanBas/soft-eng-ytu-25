import { useState, useEffect } from 'react'
import { 
  Box, Typography, Grid, Paper, CircularProgress, 
  Card, CardContent, Stack, Divider 
} from '@mui/material'
import { 
  Receipt as ReceiptIcon, 
  CheckCircle as CheckCircleIcon,
  MonetizationOn as BudgetIcon,
  Warning as WarningIcon
} from '@mui/icons-material'
import api from '../services/api'

export default function Dashboard() {
  const [stats, setStats] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [recentExpenses, setRecentExpenses] = useState([])

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [statsRes, expensesRes] = await Promise.all([
          api.get('/dashboard/stats'),
          api.get('/expenses?limit=5')
        ])
        setStats(statsRes.data)
        setRecentExpenses(expensesRes.data)
      } catch (err) {
        setError(err.response?.data?.message || 'Dashboard verileri yüklenirken bir hata oluştu')
      } finally {
        setLoading(false)
      }
    }
    
    fetchDashboardData()
  }, [])

  if (loading) return <CircularProgress />
  if (error) return <Typography color="error">{error}</Typography>

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Dashboard</Typography>
      
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <ReceiptIcon color="primary" fontSize="large" />
                <Box>
                  <Typography variant="h6">Toplam Gider</Typography>
                  <Typography variant="h4">{stats.totalExpenses} TL</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <CheckCircleIcon color="success" fontSize="large" />
                <Box>
                  <Typography variant="h6">Onaylananlar</Typography>
                  <Typography variant="h4">{stats.approvedExpenses}</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <WarningIcon color="warning" fontSize="large" />
                <Box>
                  <Typography variant="h6">Bekleyenler</Typography>
                  <Typography variant="h4">{stats.pendingExpenses}</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <BudgetIcon color="info" fontSize="large" />
                <Box>
                  <Typography variant="h6">Kalan Bütçe</Typography>
                  <Typography variant="h4">{stats.remainingBudget} TL</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
      
      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>Son Giderler</Typography>
            <Divider sx={{ my: 2 }} />
            
            {recentExpenses.length > 0 ? (
              <Stack spacing={2}>
                {recentExpenses.map((expense) => (
                  <Box key={expense.id} sx={{ p: 1, border: '1px solid #eee', borderRadius: 1 }}>
                    <Typography fontWeight="bold">
                      {new Date(expense.date).toLocaleDateString()} - {expense.amount} TL
                    </Typography>
                    <Typography variant="body2">{expense.description}</Typography>
                    <Typography variant="caption" color="text.secondary">
                      {expense.type} • {expense.status}
                    </Typography>
                  </Box>
                ))}
              </Stack>
            ) : (
              <Typography>Son gider bulunamadı</Typography>
            )}
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>Bütçe Durumu</Typography>
            <Divider sx={{ my: 2 }} />
            
            <Stack spacing={2}>
              {stats.budgetStatus.map((budget) => (
                <Box key={budget.department + budget.type}>
                  <Typography fontWeight="bold">
                    {budget.department} - {budget.type}
                  </Typography>
                  <Typography variant="body2">
                    Harcanan: {budget.spent} TL / Bütçe: {budget.total} TL
                  </Typography>
                  <Box sx={{ 
                    width: '100%', 
                    height: 10, 
                    bgcolor: '#eee', 
                    borderRadius: 5,
                    overflow: 'hidden'
                  }}>
                    <Box sx={{ 
                      width: `${(budget.spent / budget.total) * 100}%`, 
                      height: '100%',
                      bgcolor: budget.spent > budget.total ? 'error.main' : 'success.main'
                    }} />
                  </Box>
                </Box>
              ))}
            </Stack>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  )
}