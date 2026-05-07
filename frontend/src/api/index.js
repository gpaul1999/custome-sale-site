import axios from 'axios'

const BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const TENANT = import.meta.env.VITE_TENANT_ID || 'default'

const api = axios.create({ baseURL: BASE, withCredentials: true })

api.interceptors.request.use(config => {
  const adminToken = localStorage.getItem('adminToken')
  const userToken = localStorage.getItem('userToken')
  if (config.url?.startsWith('/api/admin')) {
    if (adminToken) config.headers.Authorization = `Bearer ${adminToken}`
  } else if (userToken) {
    config.headers.Authorization = `Bearer ${userToken}`
  }
  if (config.url?.startsWith('/auth')) {
    config.headers['X-Tenant-ID'] = TENANT
  }
  return config
})

export default api

export const formatPrice = (n) =>
  n == null ? '—' : n.toLocaleString('vi-VN') + ' đ'
