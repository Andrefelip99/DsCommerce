<script setup>
import { ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { authService } from '../services/authService'
const route = useRoute()
const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')
async function submit() {
  error.value = ''; loading.value = true
  try {
    await authService.login(username.value.trim(), password.value)
    await authService.profile()
    const destination = typeof route.query.redirect === 'string' && route.query.redirect.startsWith('/') ? route.query.redirect : '/conta'
    router.push(destination)
  } catch (cause) { error.value = cause.status === 401 ? 'E-mail ou senha não conferem.' : cause.message || 'Não foi possível entrar. Tente novamente.' }
  finally { loading.value = false }
}
</script>
<template>
  <section class="shell auth-page"><div class="auth-aside"><span class="auth-mark" aria-hidden="true">d.</span><p class="eyebrow">DSCOMMERCE / SUA CONTA</p><h1>Uma experiência<br />feita para <em>você.</em></h1><p>Acesse sua conta para acompanhar seus pedidos e finalizar sua escolha.</p><RouterLink class="text-link" to="/produtos">Continuar explorando <span aria-hidden="true">↗</span></RouterLink></div><div class="auth-form-wrap"><p class="eyebrow">BEM-VINDO DE VOLTA</p><h2>Entrar</h2><p class="form-intro">Use os dados da sua conta para continuar.</p><form class="auth-form" @submit.prevent="submit"><label for="username">E-mail</label><input id="username" v-model="username" type="email" autocomplete="username" placeholder="voce@exemplo.com" required /><label for="password">Senha</label><input id="password" v-model="password" type="password" autocomplete="current-password" placeholder="Sua senha" required /><p v-if="error" class="form-error" role="alert">{{ error }}</p><button class="button button-dark auth-submit" type="submit" :disabled="loading">{{ loading ? 'Entrando…' : 'Entrar na conta' }} <span aria-hidden="true">↗</span></button></form><p class="auth-footnote">A autenticação é fornecida pela API DsCommerce.</p></div></section>
</template>
