<script setup>
import { RouterLink } from 'vue-router'
import ProductImage from './ProductImage.vue'
import { formatCurrency } from '../services/formatters'
defineProps({ product: { type: Object, required: true } })
const emit = defineEmits(['add'])
</script>
<template>
  <article class="product-card">
    <RouterLink class="product-card-image" :to="`/produtos/${product.id}`" :aria-label="`Ver detalhes de ${product.name}`">
      <ProductImage :src="product.imgUrl" :alt="product.name" />
    </RouterLink>
    <div class="product-card-body">
      <RouterLink class="product-card-name" :to="`/produtos/${product.id}`">{{ product.name }}</RouterLink>
      <p class="product-card-price">{{ formatCurrency(product.price) }}</p>
      <p class="product-card-caption">Preço do catálogo</p>
      <div class="product-card-actions">
        <RouterLink class="product-detail-link" :to="`/produtos/${product.id}`">Ver detalhes</RouterLink>
        <button class="quick-add" type="button" :aria-label="`Adicionar ${product.name} à sacola`" @click="emit('add', product)"><span aria-hidden="true">+</span></button>
      </div>
    </div>
  </article>
</template>
