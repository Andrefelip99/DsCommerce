import { apiRequest } from './api'
export const productService = {
  list({ name = '', page = 0, size = 12 } = {}) {
    return apiRequest(`/products?${new URLSearchParams({ name, page: String(page), size: String(size) })}`)
  },
  get(id) { return apiRequest(`/products/${encodeURIComponent(id)}`) },
}
