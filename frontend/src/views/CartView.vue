<script setup>
import { computed, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import ProductImage from '../components/ProductImage.vue'
import FeedbackPanel from '../components/FeedbackPanel.vue'
import { useCart } from '../stores/cart'
import { authService } from '../services/authService'
import { orderService } from '../services/orderService'
import { formatCurrency } from '../services/formatters'
const router = useRouter()
const cart = useCart()
const submitting = ref(false)
const error = ref('')
const order = ref(null)
const needsLogin = computed(() => !authService.isAuthenticated)
function quantityChanged(item, event) { cart.setQuantity(item.productId, Number(event.target.value) || 1) }
async function checkout() {
  error.value = ''
  if (!cart.items.value.length) return
  if (!authService.isAuthenticated) { router.push({ name: 'login', query: { redirect: '/carrinho' } }); return }
  submitting.value = true
  try {
    order.value = await orderService.create(cart.items.value)
    cart.clear()
  } catch (cause) { error.value = cause.message }
  finally { submitting.value = false }
}
</script>
<template>
  <section class="shell cart-page">
    <div class="section-kicker"><span>DS / 04</span><span>SUA SELEÇÃO</span></div>
    <template v-if="order"><div class="order-success"><span class="success-mark">✓</span><p class="eyebrow">PEDIDO CONFIRMADO</p><h1>Obrigado pela<br /><em>sua escolha.</em></h1><p>Seu pedido <strong>#{{ order.id }}</strong> foi registrado. O total confirmado pela loja é <strong>{{ formatCurrency(order.total) }}</strong>.</p><p class="order-status">Status: {{ order.status }}</p><div class="cart-actions"><RouterLink class="button button-dark" to="/produtos">Continuar explorando</RouterLink><RouterLink class="text-link" :to="`/produtos`">Ver produtos <span aria-hidden="true">↗</span></RouterLink></div></div></template>
    <template v-else><div class="cart-heading"><div><p class="eyebrow">DSCOMMERCE / SACOLA</p><h1>Sua <em>seleção.</em></h1></div><span v-if="cart.count.value" class="cart-heading-count">{{ cart.count.value }} {{ cart.count.value === 1 ? 'item' : 'itens' }}</span></div>
      <div v-if="!cart.items.value.length" class="empty-cart"><span class="empty-cart-mark" aria-hidden="true">d.</span><h2>A sacola ainda está vazia.</h2><p>Explore os produtos da loja e encontre algo para levar com você.</p><RouterLink class="button button-dark" to="/produtos">Explorar produtos <span aria-hidden="true">↗</span></RouterLink></div>
      <div v-else class="cart-layout"><div class="cart-items"><article v-for="item in cart.items.value" :key="item.productId" class="cart-item"><RouterLink class="cart-item-image" :to="`/produtos/${item.productId}`"><ProductImage :src="item.imageUrl" :alt="item.name" /></RouterLink><div class="cart-item-info"><p class="eyebrow">DS / {{ String(item.productId).padStart(3, '0') }}</p><RouterLink class="cart-item-title" :to="`/produtos/${item.productId}`">{{ item.name }}</RouterLink><span class="cart-item-price">{{ formatCurrency(item.price) }} <small>cada</small></span><div class="cart-item-controls"><div class="quantity-control"><button type="button" :aria-label="`Diminuir quantidade de ${item.name}`" @click="cart.setQuantity(item.productId, item.quantity - 1)">−</button><input :value="item.quantity" :aria-label="`Quantidade de ${item.name}`" type="number" min="1" max="99" @change="quantityChanged(item, $event)" /><button type="button" :aria-label="`Aumentar quantidade de ${item.name}`" @click="cart.setQuantity(item.productId, item.quantity + 1)">+</button></div><button class="remove-button" type="button" @click="cart.remove(item.productId)">Remover</button></div></div><strong class="cart-item-subtotal">{{ formatCurrency(item.price * item.quantity) }}</strong></article></div>
        <aside class="order-summary"><p class="eyebrow">RESUMO DO PEDIDO</p><h2>Quase lá.</h2><div class="summary-row"><span>Itens ({{ cart.count.value }})</span><strong>{{ formatCurrency(cart.total.value) }}</strong></div><div class="summary-row"><span>Entrega</span><span class="muted">Calculada pela loja</span></div><div class="summary-total"><span>Total parcial</span><strong>{{ formatCurrency(cart.total.value) }}</strong></div><p class="summary-note">A API não informa frete ou processamento de pagamento. O pedido registra os itens e o total dos produtos.</p><FeedbackPanel v-if="error" title="Não foi possível registrar o pedido" :message="error" /><button class="button button-dark checkout-button" type="button" :disabled="submitting" @click="checkout">{{ submitting ? 'Enviando pedido…' : needsLogin ? 'Entrar para continuar' : 'Confirmar pedido' }} <span aria-hidden="true">↗</span></button><RouterLink class="continue-link" to="/produtos">← Continuar comprando</RouterLink></aside>
      </div>
    </template>
  </section>
</template>
