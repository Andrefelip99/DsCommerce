const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'https://dscommerce-fyii.onrender.com').replace(/\/$/, '')

export class ApiError extends Error {
  constructor(status, message) { super(message); this.name = 'ApiError'; this.status = status }
}

export async function apiRequest(path, options = {}) {
  const headers = new Headers(options.headers || {})
  if (options.body && !(options.body instanceof URLSearchParams)) headers.set('Content-Type', 'application/json')
  const token = sessionStorage.getItem('dscommerce.accessToken')
  if (token && options.auth !== false && !headers.has('Authorization')) headers.set('Authorization', `Bearer ${token}`)
  let response
  try { response = await fetch(`${baseUrl}${path}`, { ...options, headers }) }
  catch { throw new ApiError(0, 'Não foi possível conectar à loja. Confira se a API está disponível.') }
  if (response.status === 401) {
    sessionStorage.removeItem('dscommerce.accessToken')
    window.dispatchEvent(new Event('dscommerce:session-expired'))
  }
  if (!response.ok) {
    const message = response.status === 404 ? 'Não encontramos este conteúdo.'
      : response.status === 403 ? 'Sua conta não tem permissão para esta ação.'
      : response.status >= 500 ? 'A loja está temporariamente indisponível. Tente novamente em instantes.'
      : 'Não foi possível concluir a solicitação. Confira os dados e tente novamente.'
    throw new ApiError(response.status, message)
  }
  if (response.status === 204) return null
  return response.json()
}

export { baseUrl }
