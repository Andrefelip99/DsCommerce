import { apiRequest } from './api'
export const categoryService = { list: () => apiRequest('/categories') }
