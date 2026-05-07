import { Link } from 'react-router-dom'
import { formatPrice } from '../api'
import { useCart } from '../context/CartContext'

export default function ProductCard({ product }) {
  const { addToCart } = useCart()
  const img = product.images?.[0]
  const displayPrice = product.saleOff && product.salePrice ? product.salePrice : product.price

  return (
    <div className="card flex flex-col">
      <div className="relative">
        <img
          src={img || 'https://placehold.co/300x200?text=No+Image'}
          alt={product.syntax}
          className="w-full h-44 object-cover bg-gray-100"
          onError={e => { e.target.src = 'https://placehold.co/300x200?text=No+Image' }}
        />
        {product.saleOff && (
          <span className="absolute top-2 right-2 badge-sale">-{product.salePercent}%</span>
        )}
      </div>
      <div className="p-4 flex flex-col flex-1">
        <div className="text-xs text-[#0055aa] font-medium mb-1">{product.productTypeName} / {product.productCategoryName}</div>
        <h3 className="font-semibold text-gray-900 mb-1 line-clamp-2">{product.syntax}</h3>
        <p className="text-sm text-gray-500 mb-3 line-clamp-2 flex-1">{product.description}</p>
        <div className="mb-3">
          {product.saleOff && product.salePrice ? (
            <div>
              <span className="text-[#ff6600] font-bold text-lg">{formatPrice(product.salePrice)}</span>
              <span className="text-gray-400 text-sm line-through ml-2">{formatPrice(product.price)}</span>
            </div>
          ) : (
            <span className="text-[#0055aa] font-bold text-lg">{formatPrice(product.price)}</span>
          )}
        </div>
        <div className="flex gap-2">
          <Link to={`/product/${product.id}`} className="btn-outline text-sm flex-1 text-center">Xem chi tiết</Link>
          <button onClick={() => addToCart(product, 1)} className="btn-accent text-sm px-3">🛒</button>
        </div>
      </div>
    </div>
  )
}
