import { Outlet, Link } from 'react-router-dom'
import { AppBar, Toolbar, Typography, Box, CssBaseline, Drawer, List, ListItem, ListItemIcon, ListItemText } from '@mui/material'
import { Home, Receipt, CheckCircle, Assessment, MonetizationOn } from '@mui/icons-material'

const drawerWidth = 240

export default function Layout() {
  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
        <Toolbar>
          <Typography variant="h6" noWrap component="div">
            Gider Yönetim Sistemi
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
        }}
      >
        <Toolbar />
        <Box sx={{ overflow: 'auto' }}>
          <List>
            <ListItem button component={Link} to="/">
              <ListItemIcon><Home /></ListItemIcon>
              <ListItemText primary="Dashboard" />
            </ListItem>
            <ListItem button component={Link} to="/expense-form">
              <ListItemIcon><Receipt /></ListItemIcon>
              <ListItemText primary="Gider Ekle" />
            </ListItem>
            <ListItem button component={Link} to="/expense-list">
              <ListItemIcon><Receipt /></ListItemIcon>
              <ListItemText primary="Giderlerim" />
            </ListItem>
            <ListItem button component={Link} to="/approval-list">
              <ListItemIcon><CheckCircle /></ListItemIcon>
              <ListItemText primary="Onay Bekleyenler" />
            </ListItem>
            <ListItem button component={Link} to="/reports">
              <ListItemIcon><Assessment /></ListItemIcon>
              <ListItemText primary="Raporlar" />
            </ListItem>
            <ListItem button component={Link} to="/budgets">
              <ListItemIcon><MonetizationOn /></ListItemIcon>
              <ListItemText primary="Bütçeler" />
            </ListItem>
          </List>
        </Box>
      </Drawer>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  )
}