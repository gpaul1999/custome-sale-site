import { createContext, useContext, useState, useEffect } from 'react'

const CartContext = createContext(null)

export function CartProvider({ children }) {
  const [items, setItems] = useState(() => {
    try { return JSON.parse(localStorage.getItem('cart')) || [] } catch { return [] }
  })

  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(items))
  }, [items])

  const addToCart = (product, quantity = 1) => {
    setItems(prev => {
      const existing = prev.find(i => i.productId === product.id)
      if (existing) {
        return prev.map(i => i.productId === product.id ? { ...i, quantity: i.quantity + quantity } : i)
      }
      return [...prev, { productId: product.id, quantity, product }]
    })
  }

  const removeFromCart = (productId) => {
    setItems(prev => prev.filter(i => i.productId !== productId))
  }

  const updateQuantity = (productId, quantity) => {
    if (quantity <= 0) return removeFromCart(productId)
    setItems(prev => prev.map(i => i.productId === productId ? { ...i, quantity } : i))
  }

  const clearCart = () => setItems([])

  const cartCount = items.reduce((s, i) => s + i.quantity, 0)
  const cartTotal = items.reduce((s, i) => s + (i.product?.price || 0) * i.quantity, 0)

  return (
    <CartContext.Provider value={{ items, addToCart, removeFromCart, updateQuantity, clearCart, cartCount, cartTotal }}>
      {children}
    </CartContext.Provider>
  )
}

export const useCart = () => useContext(CartContext)
