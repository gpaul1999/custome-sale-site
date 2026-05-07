import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function ProtectedRoute({ children, requireAdmin }) {
  const { user, adminUser } = useAuth()
  const navigate = useNavigate()
  if (requireAdmin && !adminUser) {
    navigate('/admin/login', { replace: true })
    return null
  }
  if (!requireAdmin && !user) {
    navigate('/login', { replace: true })
    return null
  }
  return children
}
