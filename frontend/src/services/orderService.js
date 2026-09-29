import { apiRequest } from './api'
export const orderService = {
  create(items) {
    return apiRequest('/orders', { method: 'POST', body: JSON.stringify({ items: items.map(({ productId, name, price, quantity, imageUrl }) => ({ productId, name, price, quantity, imageUrl })) }) })
  },
  get(id) { return apiRequest(`/orders/${encodeURIComponent(id)}`) },
}
