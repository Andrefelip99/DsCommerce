<script setup>
import { ref, watch } from 'vue'
const props = defineProps({ src: { type: String, default: '' }, alt: { type: String, default: '' }, className: { type: String, default: '' } })
const failed = ref(!props.src)
watch(() => props.src, () => { failed.value = !props.src })
function onError() { failed.value = true }
</script>
<template>
  <div class="product-image-frame" :class="className">
    <img v-if="!failed" :src="src" :alt="alt" loading="lazy" @error="onError" />
    <div v-else class="image-unavailable" role="img" :aria-label="`Imagem indisponível: ${alt || 'produto'}`"><svg viewBox="0 0 42 42" aria-hidden="true"><rect x="5" y="8" width="32" height="26" rx="2"/><circle cx="15" cy="17" r="2.2"/><path d="m8 30 9-8 5 4 5-6 7 10"/></svg><small>Imagem indisponível</small></div>
  </div>
</template>
