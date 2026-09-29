<script setup>
defineProps({ title: { type: String, default: 'Algo não saiu como esperado' }, message: { type: String, default: '' }, actionLabel: { type: String, default: '' } })
const emit = defineEmits(['action'])
</script>
<template>
  <section class="feedback-panel" role="status">
    <span class="feedback-mark" aria-hidden="true">—</span><h2>{{ title }}</h2><p>{{ message }}</p>
    <button v-if="actionLabel" class="button button-outline" type="button" @click="emit('action')">{{ actionLabel }}</button>
  </section>
</template>
