import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, TextField, Button, Typography, MenuItem, Paper } from '@mui/material'
import api from '../services/api'

const expenseTypes = [
  { value: 'TAXI', label: 'Taksi Ücreti' },
  { value: 'PARKING', label: 'Otopark Bedeli' },
  { value: 'FUEL', label: 'Yakıt Gideri' },
  { value: 'MEAL', label: 'Yemek Gideri' },
  { value: 'OTHER', label: 'Diğer' }
]

export default function ExpenseForm() {
  const [formData, setFormData] = useState({
    type: '',
    amount: '',
    date: new Date().toISOString().split('T')[0],
    description: '',
    receiptNo: ''
  })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const navigate = useNavigate()

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    
    try {
      await api.addExpense(formData)
      setSuccess('Gider başarıyla eklendi ve onay için yöneticinize gönderildi!')
      setTimeout(() => navigate('/expense-list'), 2000)
    } catch (err) {
      setError(err.response?.data?.message || 'Gider eklenirken bir hata oluştu')
    }
  }

  return (
    <Paper elevation={3} sx={{ p: 3, maxWidth: 600, mx: 'auto' }}>
      <Typography variant="h5" gutterBottom>Yeni Gider Ekle</Typography>
      
      {error && <Typography color="error" gutterBottom>{error}</Typography>}
      {success && <Typography color="success.main" gutterBottom>{success}</Typography>}
      
      <Box component="form" onSubmit={handleSubmit}>
        <TextField
          select
          label="Gider Türü"
          name="type"
          value={formData.type}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        >
          {expenseTypes.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>
        
        <TextField
          label="Tutar"
          name="amount"
          type="number"
          value={formData.amount}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        
        <TextField
          label="Tarih"
          name="date"
          type="date"
          value={formData.date}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
          InputLabelProps={{ shrink: true }}
        />
        
        <TextField
          label="Açıklama"
          name="description"
          value={formData.description}
          onChange={handleChange}
          fullWidth
          margin="normal"
          multiline
          rows={4}
          required
        />
        
        <TextField
          label="Fiş No"
          name="receiptNo"
          value={formData.receiptNo}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />
        
        <Box sx={{ mt: 2, display: 'flex', justifyContent: 'flex-end' }}>
          <Button type="submit" variant="contained" color="primary">
            Gönder
          </Button>
        </Box>
      </Box>
    </Paper>
  )
}