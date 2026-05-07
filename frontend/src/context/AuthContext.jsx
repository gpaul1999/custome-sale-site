import { createContext, useContext, useState, useEffect } from 'react'
import api from '../api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem('user')) } catch { return null }
  })
  const [adminUser, setAdminUser] = useState(() => localStorage.getItem('adminUsername'))

  const login = async (email, password) => {
    const { data } = await api.post('/auth/login', { email, password })
    localStorage.setItem('userToken', data.token)
    localStorage.setItem('user', JSON.stringify(data))
    setUser(data)
    return data
  }

  const register = async (email, password, firstName, lastName) => {
    const { data } = await api.post('/auth/register', { email, password, firstName, lastName })
    localStorage.setItem('userToken', data.token)
    localStorage.setItem('user', JSON.stringify(data))
    setUser(data)
    return data
  }

  const logout = () => {
    localStorage.removeItem('userToken')
    localStorage.removeItem('user')
    setUser(null)
  }

  const adminLogin = async (username, password) => {
    const { data } = await api.post('/api/admin/auth/login', { username, password })
    localStorage.setItem('adminToken', data.token)
    localStorage.setItem('adminUsername', data.username)
    setAdminUser(data.username)
    return data
  }

  const adminLogout = () => {
    localStorage.removeItem('adminToken')
    localStorage.removeItem('adminUsername')
    setAdminUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, adminUser, login, register, logout, adminLogin, adminLogout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
