import { useState, useEffect } from 'react'
import { 
  Box, Typography, Paper, TextField, MenuItem, Select, 
  FormControl, InputLabel, Grid, CircularProgress 
} from '@mui/material'
import { BarChart, PieChart } from '../components/Charts'
import api from '../services/api'

export default function Reports() {
  const [reportData, setReportData] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [filters, setFilters] = useState({
    year: new Date().getFullYear(),
    type: 'ALL',
    groupBy: 'MONTH'
  })

  useEffect(() => {
    const fetchReportData = async () => {
      setLoading(true)
      try {
        const { data } = await api.getExpenseReports(filters)
        setReportData(data)
      } catch (err) {
        setError(err.response?.data?.message || 'Rapor yüklenirken bir hata oluştu')
      } finally {
        setLoading(false)
      }
    }
    
    fetchReportData()
  }, [filters])

  const handleFilterChange = (e) => {
    const { name, value } = e.target
    setFilters(prev => ({ ...prev, [name]: value }))
  }

  if (loading) return <CircularProgress />
  if (error) return <Typography color="error">{error}</Typography>

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Gider Raporları</Typography>
      
      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Grid container spacing={2}>
          <Grid item xs={12} md={4}>
            <TextField
              label="Yıl"
              name="year"
              type="number"
              value={filters.year}
              onChange={handleFilterChange}
              fullWidth
            />
          </Grid>
          <Grid item xs={12} md={4}>
            <FormControl fullWidth>
              <InputLabel>Gider Türü</InputLabel>
              <Select
                name="type"
                value={filters.type}
                onChange={handleFilterChange}
                label="Gider Türü"
              >
                <MenuItem value="ALL">Tümü</MenuItem>
                {expenseTypes.map((type) => (
                  <MenuItem key={type.value} value={type.value}>
                    {type.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={4}>
            <FormControl fullWidth>
              <InputLabel>Gruplama</InputLabel>
              <Select
                name="groupBy"
                value={filters.groupBy}
                onChange={handleFilterChange}
                label="Gruplama"
              >
                <MenuItem value="MONTH">Aylık</MenuItem>
                <MenuItem value="TYPE">Gider Türüne Göre</MenuItem>
                <MenuItem value="DEPARTMENT">Departmana Göre</MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>
      </Paper>
      
      {reportData && (
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Paper elevation={3} sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>Aylık Giderler</Typography>
              <BarChart data={reportData.monthly} />
            </Paper>
          </Grid>
          <Grid item xs={12} md={6}>
            <Paper elevation={3} sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>Gider Türleri Dağılımı</Typography>
              <PieChart data={reportData.byType} />
            </Paper>
          </Grid>
        </Grid>
      )}
    </Box>
  )
}