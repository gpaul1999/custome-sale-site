import { useState, useRef, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useCart } from '../context/CartContext'
import api from '../api'

export default function Header() {
  const { user, logout } = useAuth()
  const { cartCount } = useCart()
  const [searchQ, setSearchQ] = useState('')
  const [results, setResults] = useState([])
  const [menuOpen, setMenuOpen] = useState(false)
  const [menuData, setMenuData] = useState([])
  const [megaOpen, setMegaOpen] = useState(false)
  const timer = useRef(null)
  const navigate = useNavigate()

  useEffect(() => {
    api.get('/api/data/menu/product-types').then(r => setMenuData(r.data)).catch(() => {})
  }, [])

  const onSearch = (val) => {
    setSearchQ(val)
    clearTimeout(timer.current)
    if (!val.trim()) { setResults([]); return }
    timer.current = setTimeout(async () => {
      try {
        const { data } = await api.get(`/api/data/products/search/dropdown?q=${encodeURIComponent(val)}`)
        setResults(data.slice(0, 8))
      } catch { setResults([]) }
    }, 300)
  }

  const handleSearchSubmit = (e) => {
    e.preventDefault()
    if (searchQ.trim()) { navigate(`/services?q=${encodeURIComponent(searchQ)}`); setResults([]) }
  }

  return (
    <header className="sticky top-0 z-50 bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex items-center h-16 gap-4">
          {/* Logo */}
          <Link to="/" className="flex-shrink-0">
            <span className="text-2xl font-bold text-[#0055aa]">H2</span>
            <span className="text-2xl font-bold text-[#ff6600]"> Solution</span>
          </Link>

          {/* Search */}
          <form onSubmit={handleSearchSubmit} className="flex-1 relative hidden md:block max-w-lg">
            <input
              type="text"
              value={searchQ}
              onChange={e => onSearch(e.target.value)}
              onBlur={() => setTimeout(() => setResults([]), 200)}
              placeholder="Tìm kiếm dịch vụ..."
              className="w-full border border-gray-200 rounded-full px-4 py-2 pl-10 focus:outline-none focus:border-[#0055aa] text-sm"
            />
            <span className="absolute left-3 top-2.5 text-gray-400">🔍</span>
            {results.length > 0 && (
              <div className="absolute top-full left-0 right-0 bg-white border rounded-xl shadow-lg mt-1 z-50">
                {results.map(p => (
                  <Link key={p.id} to={`/product/${p.id}`} className="flex items-center gap-3 px-4 py-2 hover:bg-gray-50 text-sm">
                    <img src={p.images?.[0] || ''} className="w-8 h-8 object-cover rounded" onError={e => e.target.style.display='none'} alt="" />
                    <span className="font-medium">{p.syntax}</span>
                    <span className="ml-auto text-[#0055aa] font-semibold">{p.price?.toLocaleString('vi-VN')}đ</span>
                  </Link>
                ))}
              </div>
            )}
          </form>

          {/* Nav */}
          <nav className="hidden md:flex items-center gap-1 text-sm font-medium">
            <Link to="/" className="px-3 py-2 hover:text-[#0055aa] rounded">Trang chủ</Link>
            <div className="relative" onMouseEnter={() => setMegaOpen(true)} onMouseLeave={() => setMegaOpen(false)}>
              <Link to="/services" className="px-3 py-2 hover:text-[#0055aa] rounded flex items-center gap-1">
                Dịch vụ <span className="text-xs">▾</span>
              </Link>
              {megaOpen && menuData.length > 0 && (
                <div className="absolute left-0 top-full bg-white shadow-xl border rounded-xl p-4 w-64 z-50">
                  {menuData.map(type => (
                    <div key={type.id} className="mb-3">
                      <Link to={`/services?typeId=${type.id}`} className="font-semibold text-[#0055aa] block mb-1 hover:underline">{type.syntax}</Link>
                      {type.categories?.map(cat => (
                        <Link key={cat.id} to={`/services?typeId=${type.id}`} className="block text-gray-600 hover:text-[#0055aa] text-xs py-0.5 pl-2">• {cat.syntax}</Link>
                      ))}
                    </div>
                  ))}
                </div>
              )}
            </div>
            <Link to="/about" className="px-3 py-2 hover:text-[#0055aa] rounded">Giới thiệu</Link>
          </nav>

          <div className="ml-auto flex items-center gap-3">
            <Link to="/cart" className="relative p-2 hover:text-[#0055aa]">
              🛒
              {cartCount > 0 && (
                <span className="absolute -top-1 -right-1 bg-[#ff6600] text-white text-xs w-5 h-5 rounded-full flex items-center justify-center">{cartCount}</span>
              )}
            </Link>
            {user ? (
              <div className="flex items-center gap-2 text-sm">
                <span className="hidden md:block text-gray-700">Xin chào, {user.firstName || user.email}</span>
                <button onClick={logout} className="text-gray-500 hover:text-red-500 text-sm">Đăng xuất</button>
              </div>
            ) : (
              <div className="flex items-center gap-2">
                <Link to="/login" className="btn-outline text-sm hidden md:block">Đăng nhập</Link>
                <Link to="/register" className="btn-primary text-sm hidden md:block">Đăng ký</Link>
              </div>
            )}
            <button className="md:hidden p-2" onClick={() => setMenuOpen(!menuOpen)}>☰</button>
          </div>
        </div>
      </div>

      {/* Mobile menu */}
      {menuOpen && (
        <div className="md:hidden border-t bg-white px-4 py-3 space-y-2 text-sm">
          <Link to="/" className="block py-2 font-medium" onClick={() => setMenuOpen(false)}>Trang chủ</Link>
          <Link to="/services" className="block py-2 font-medium" onClick={() => setMenuOpen(false)}>Dịch vụ</Link>
          <Link to="/about" className="block py-2 font-medium" onClick={() => setMenuOpen(false)}>Giới thiệu</Link>
          {user ? (
            <button onClick={() => { logout(); setMenuOpen(false) }} className="block py-2 text-red-500">Đăng xuất</button>
          ) : (
            <>
              <Link to="/login" className="block py-2" onClick={() => setMenuOpen(false)}>Đăng nhập</Link>
              <Link to="/register" className="block py-2" onClick={() => setMenuOpen(false)}>Đăng ký</Link>
            </>
          )}
        </div>
      )}
    </header>
  )
}
