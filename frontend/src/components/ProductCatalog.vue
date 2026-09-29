<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import FeedbackPanel from './FeedbackPanel.vue'
import ProductCard from './ProductCard.vue'
import { productService } from '../services/productService'
import { categoryService } from '../services/categoryService'
import { useCart } from '../stores/cart'

const route = useRoute()
const router = useRouter()
const cart = useCart()
const term = ref('')
const pageData = ref({ content: [], number: 0, size: 12, totalPages: 0, totalElements: 0 })
const categories = ref([])
const loading = ref(true)
const error = ref('')
const added = ref(false)
const currentPage = computed(() => Number(pageData.value.number || 0))
const pageNumbers = computed(() => {
  const total = Number(pageData.value.totalPages || 0)
  const start = Math.max(0, Math.min(currentPage.value - 2, total - 5))
  return Array.from({ length: Math.min(total, 5) }, (_, index) => start + index)
})

async function loadProducts(page = currentPage.value) {
  loading.value = true
  error.value = ''
  try {
    pageData.value = await productService.list({ name: term.value, page, size: 12 })
  } catch (cause) {
    error.value = cause.message || 'Não foi possível carregar os produtos.'
  } finally {
    loading.value = false
  }
}
function goToPage(page) {
  if (page < 0 || page >= pageData.value.totalPages || page === currentPage.value) return
  loadProducts(page)
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
function addToCart(product) {
  cart.add(product)
  added.value = true
  window.clearTimeout(addToCart.timeout)
  addToCart.timeout = window.setTimeout(() => { added.value = false }, 2600)
}
watch(() => route.query.name, (value) => {
  term.value = typeof value === 'string' ? value : ''
  loadProducts(0)
})
onMounted(() => {
  term.value = typeof route.query.name === 'string' ? route.query.name : ''
  loadProducts(0)
  categoryService.list().then((items) => { categories.value = items || [] }).catch(() => { categories.value = [] })
})
</script>

<template>
  <div class="catalog-page">
    <div v-if="added" class="cart-toast" role="status">
      <span class="toast-check" aria-hidden="true">✓</span>
      <span>Produto adicionado à sacola</span>
      <RouterLink to="/carrinho">Ver sacola</RouterLink>
      <button type="button" aria-label="Fechar aviso" @click="added = false">×</button>
    </div>

    <section id="categorias" class="category-strip" aria-label="Categorias disponíveis">
      <div class="content-shell category-strip-inner">
        <strong class="category-label">Categorias</strong>
        <div v-if="categories.length" class="category-chips">
          <span v-for="category in categories" :key="category.id" class="category-pill">{{ category.name }}</span>
        </div>
        <span v-else class="category-caption">Explore os produtos disponíveis na loja</span>
        <span class="category-caption category-caption-right">{{ pageData.totalElements || 0 }} produtos no catálogo</span>
      </div>
    </section>

    <section class="content-shell catalog-content">
      <div class="catalog-heading-row">
        <div>
          <p class="catalog-breadcrumb"><RouterLink to="/">Início</RouterLink><span>/</span><span>Produtos</span></p>
          <h1>{{ term ? `Resultados para “${term}”` : 'Produtos' }}</h1>
        </div>
        <p class="catalog-summary">{{ pageData.totalElements || 0 }} {{ pageData.totalElements === 1 ? 'produto disponível' : 'produtos disponíveis' }}</p>
      </div>

      <div class="results-toolbar">
        <p><strong>{{ pageData.totalElements || 0 }}</strong> {{ pageData.totalElements === 1 ? 'resultado' : 'resultados' }}<template v-if="term"> para <strong>“{{ term }}”</strong></template></p>
        <span v-if="pageData.totalPages > 1">Página {{ currentPage + 1 }} de {{ pageData.totalPages }}</span>
      </div>

      <div v-if="loading" class="product-grid" aria-label="Carregando produtos" aria-busy="true">
        <div v-for="n in 8" :key="n" class="product-skeleton"><div class="skeleton-image"></div><div class="skeleton-line skeleton-line-long"></div><div class="skeleton-line skeleton-line-short"></div><div class="skeleton-line skeleton-line-price"></div></div>
      </div>
      <FeedbackPanel v-else-if="error" title="Não conseguimos carregar os produtos" :message="error" action-label="Tentar novamente" @action="loadProducts(0)" />
      <FeedbackPanel v-else-if="!pageData.content?.length" :title="term ? 'Não encontramos esse produto' : 'Nenhum produto disponível agora'" :message="term ? 'Tente outro nome ou limpe a busca para ver o catálogo completo.' : 'Tente novamente em instantes.'" :action-label="term ? 'Limpar busca' : 'Atualizar'" @action="term ? router.push({ name: route.name, query: {} }) : loadProducts(0)" />
      <div v-else class="product-grid" aria-live="polite">
        <ProductCard v-for="product in pageData.content" :key="product.id" :product="product" @add="addToCart" />
      </div>

      <nav v-if="!loading && !error && pageData.totalPages > 1" class="pagination" aria-label="Paginação dos produtos">
        <button class="pagination-arrow" type="button" :disabled="currentPage === 0" aria-label="Página anterior" @click="goToPage(currentPage - 1)">‹</button>
        <button v-for="page in pageNumbers" :key="page" class="pagination-page" :class="{ active: page === currentPage }" type="button" :aria-current="page === currentPage ? 'page' : undefined" @click="goToPage(page)">{{ page + 1 }}</button>
        <button class="pagination-arrow" type="button" :disabled="currentPage + 1 >= pageData.totalPages" aria-label="Próxima página" @click="goToPage(currentPage + 1)">›</button>
      </nav>
    </section>
  </div>
</template>
