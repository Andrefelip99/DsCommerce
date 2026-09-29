import { computed, reactive } from 'vue'
const storageKey = 'dscommerce.cart'
function restore() {
  try { const parsed = JSON.parse(localStorage.getItem(storageKey) || '[]'); return Array.isArray(parsed) ? parsed.filter((item) => item && item.productId && item.quantity > 0) : [] }
  catch { return [] }
}
const state = reactive({ items: restore() })
function persist() { localStorage.setItem(storageKey, JSON.stringify(state.items)) }
export function useCart() {
  const count = computed(() => state.items.reduce((total, item) => total + item.quantity, 0))
  const total = computed(() => state.items.reduce((sum, item) => sum + item.price * item.quantity, 0))
  function add(product, quantity = 1) {
    const found = state.items.find((item) => item.productId === product.id)
    if (found) found.quantity += quantity
    else state.items.push({ productId: product.id, name: product.name, price: product.price, quantity, imageUrl: product.imgUrl })
    persist()
  }
  function setQuantity(productId, quantity) {
    const item = state.items.find((entry) => entry.productId === productId)
    if (!item) return
    if (quantity < 1) state.items = state.items.filter((entry) => entry.productId !== productId)
    else item.quantity = Math.min(99, quantity)
    persist()
  }
  function remove(productId) { state.items = state.items.filter((item) => item.productId !== productId); persist() }
  function clear() { state.items = []; persist() }
  return { items: computed(() => state.items), count, total, add, setQuantity, remove, clear }
}
