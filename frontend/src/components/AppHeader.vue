<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { useCart } from '../stores/cart'
import { authService } from '../services/authService'
const cart = useCart()
const route = useRoute()
const router = useRouter()
const query = ref('')
const signedIn = ref(authService.isAuthenticated)
const menuOpen = ref(false)
watch(() => route.query.name, (value) => { query.value = typeof value === 'string' ? value : '' })
function syncAuth() { signedIn.value = authService.isAuthenticated }
function submitSearch() {
  const name = query.value.trim()
  router.push({ name: route.name === 'products' ? 'products' : 'home', query: name ? { name } : {} })
  menuOpen.value = false
}
function syncQuery(value) { query.value = typeof value === 'string' ? value : '' }
function syncRoute() { syncQuery(route.query.name) }
onMounted(() => {
  syncRoute()
  window.addEventListener('dscommerce:auth-changed', syncAuth)
})
onUnmounted(() => window.removeEventListener('dscommerce:auth-changed', syncAuth))
</script>
<template>
  <a class="skip-link" href="#conteudo">Pular para o conteúdo</a>
  <header class="market-header">
    <div class="header-main content-shell">
      <RouterLink class="store-brand" to="/" aria-label="DsCommerce, início">
        <span class="store-brand-mark" aria-hidden="true">DS</span>
        <span class="store-brand-name">dscommerce</span>
      </RouterLink>
      <form class="header-search" role="search" @submit.prevent="submitSearch">
        <label class="sr-only" for="header-search-input">Buscar produtos</label>
        <input id="header-search-input" v-model="query" type="search" placeholder="Buscar produtos" />
        <button type="submit" aria-label="Buscar">
          <svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="10.8" cy="10.8" r="6.5"/><path d="m16 16 4.2 4.2"/></svg>
        </button>
      </form>
      <nav class="header-actions" aria-label="Acesso rápido">
        <RouterLink :to="signedIn ? '/conta' : '/entrar'" class="header-action">
          <svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="8" r="3.5"/><path d="M5 20c.5-3.4 3.2-5.5 7-5.5s6.5 2.1 7 5.5"/></svg>
          <span>{{ signedIn ? 'Minha conta' : 'Entrar' }}</span>
        </RouterLink>
        <RouterLink to="/carrinho" class="header-action cart-action" :aria-label="`Sacola, ${cart.count.value} itens`">
          <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M3.5 4h2l2.2 11.1h10.8L21 7H6.2"/><circle cx="9.5" cy="19" r="1.3"/><circle cx="17" cy="19" r="1.3"/></svg>
          <span>Sacola</span><span v-if="cart.count.value" class="cart-count">{{ cart.count.value }}</span>
        </RouterLink>
      </nav>
      <button class="mobile-menu-button" type="button" :aria-expanded="menuOpen" aria-label="Abrir navegação" @click="menuOpen = !menuOpen"><span></span><span></span><span></span></button>
    </div>
    <div class="header-lower content-shell" :class="{ 'menu-open': menuOpen }">
      <nav class="header-nav" aria-label="Navegação principal">
        <RouterLink to="/#categorias" @click="menuOpen = false">Categorias</RouterLink>
        <RouterLink to="/produtos" @click="menuOpen = false">Produtos</RouterLink>
        <RouterLink v-if="signedIn" to="/conta" @click="menuOpen = false">Minha conta</RouterLink>
        <RouterLink v-else to="/entrar" @click="menuOpen = false">Entrar</RouterLink>
        <RouterLink to="/carrinho" @click="menuOpen = false">Sacola <span v-if="cart.count.value">({{ cart.count.value }})</span></RouterLink>
      </nav>
      <p class="delivery-note"><svg viewBox="0 0 24 24" aria-hidden="true"><path d="M3 6h11v11H3zM14 10h4l3 3v4h-7z"/><circle cx="7.5" cy="19" r="1.5"/><circle cx="17.5" cy="19" r="1.5"/></svg> Escolhas para o seu dia a dia</p>
    </div>
  </header>
</template>
