<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import AppHeader from './components/AppHeader.vue'
import AppFooter from './components/AppFooter.vue'
import { authService } from './services/authService'
const router = useRouter()
const notice = ref('')
function sessionExpired() {
  authService.logout(); notice.value = 'Sua sessão terminou. Entre novamente para continuar.'
  if (router.currentRoute.value.meta.requiresAuth) router.push({ name: 'login' })
}
onMounted(() => window.addEventListener('dscommerce:session-expired', sessionExpired))
onUnmounted(() => window.removeEventListener('dscommerce:session-expired', sessionExpired))
</script>
<template>
  <AppHeader />
  <div v-if="notice" class="session-notice" role="status">{{ notice }} <button type="button" aria-label="Fechar aviso" @click="notice = ''">×</button></div>
  <main id="conteudo"><RouterView /></main>
  <AppFooter />
</template>
