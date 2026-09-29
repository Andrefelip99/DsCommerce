<script setup>
import { onMounted, ref, watch } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import ProductImage from '../components/ProductImage.vue'
import FeedbackPanel from '../components/FeedbackPanel.vue'
import { productService } from '../services/productService'
import { formatCurrency } from '../services/formatters'
import { useCart } from '../stores/cart'
const route = useRoute()
const cart = useCart()
const product = ref(null)
const loading = ref(true)
const error = ref('')
const quantity = ref(1)
const added = ref(false)
async function load() {
  loading.value = true; error.value = ''; product.value = null
  try { product.value = await productService.get(route.params.id) }
  catch (cause) { error.value = cause.message }
  finally { loading.value = false }
}
function add() { if (!product.value) return; cart.add(product.value, quantity.value); added.value = true; window.setTimeout(() => { added.value = false }, 2500) }
watch(() => route.params.id, load)
onMounted(load)
</script>
<template>
  <section v-if="loading" class="loading-state detail-loading"><span class="loading-spinner"></span><p>Carregando produto…</p></section>
  <section v-else-if="error" class="shell detail-feedback"><FeedbackPanel title="Não encontramos esse produto" :message="error" action-label="Voltar ao catálogo" @action="$router.push('/produtos')" /></section>
  <section v-else-if="product" class="shell product-detail">
    <nav class="breadcrumbs" aria-label="Você está em"><RouterLink to="/">Início</RouterLink><span>/</span><RouterLink to="/produtos">Produtos</RouterLink><span>/</span><span>{{ product.name }}</span></nav>
    <div class="detail-layout"><div class="detail-visual"><ProductImage class-name="detail-image" :src="product.imgUrl" :alt="product.name" /><span class="detail-image-index">DS / {{ String(product.id).padStart(3, '0') }}</span></div>
      <div class="detail-copy"><p class="eyebrow">DETALHE DO PRODUTO <span class="detail-divider">/</span> DS {{ String(product.id).padStart(3, '0') }}</p><h1>{{ product.name }}</h1><p class="detail-price">{{ formatCurrency(product.price) }}</p><div class="detail-line"></div><h2>Sobre este produto</h2><p class="detail-description">{{ product.description }}</p>
        <div v-if="product.categories?.length" class="detail-categories"><span class="eyebrow">CATEGORIAS</span><span v-for="category in product.categories" :key="category.id" class="category-chip">{{ category.name }}</span></div>
        <div class="purchase-row"><label for="quantity">Quantidade</label><div class="quantity-control"><button type="button" aria-label="Diminuir quantidade" :disabled="quantity <= 1" @click="quantity--">−</button><input id="quantity" v-model.number="quantity" type="number" min="1" max="99" /><button type="button" aria-label="Aumentar quantidade" :disabled="quantity >= 99" @click="quantity++">+</button></div></div>
        <button class="button button-dark detail-add" type="button" @click="add">Adicionar à sacola <span aria-hidden="true">↗</span></button><p v-if="added" class="inline-notice" role="status">Adicionado à sacola. <RouterLink to="/carrinho">Ir para a sacola →</RouterLink></p><p class="detail-footnote">O preço e a disponibilidade são confirmados pela loja ao finalizar o pedido.</p>
      </div>
    </div>
  </section>
</template>
