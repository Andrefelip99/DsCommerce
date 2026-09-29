import { apiRequest } from './api'
const tokenKey = 'dscommerce.accessToken'
let currentUser = null
let profilePromise = null
export const authService = {
  get token() { return sessionStorage.getItem(tokenKey) },
  get isAuthenticated() { return Boolean(this.token) },
  async login(username, password) {
    const clientId = import.meta.env.VITE_OAUTH_CLIENT_ID || 'myclientid'
    const clientSecret = import.meta.env.VITE_OAUTH_CLIENT_SECRET || 'myclientsecret'
    const credentials = btoa(`${clientId}:${clientSecret}`)
    const body = new URLSearchParams({ grant_type: 'password', username, password })
    const response = await apiRequest('/oauth2/token', {
      method: 'POST', headers: { Authorization: `Basic ${credentials}`, 'Content-Type': 'application/x-www-form-urlencoded' }, body, auth: false,
    })
    if (!response.access_token) throw new Error('A API não retornou um token de acesso.')
    sessionStorage.setItem(tokenKey, response.access_token)
    currentUser = null; profilePromise = null
    window.dispatchEvent(new Event('dscommerce:auth-changed'))
    return response
  },
  async profile() {
    if (!this.isAuthenticated) return null
    if (currentUser) return currentUser
    if (!profilePromise) profilePromise = apiRequest('/users/me').then((user) => { currentUser = user; return user }).catch((error) => { profilePromise = null; throw error })
    return profilePromise
  },
  logout() {
    sessionStorage.removeItem(tokenKey); currentUser = null; profilePromise = null
    window.dispatchEvent(new Event('dscommerce:auth-changed'))
  },
}
