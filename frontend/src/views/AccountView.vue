<script setup>
import { onMounted, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { authService } from '../services/authService'
import FeedbackPanel from '../components/FeedbackPanel.vue'
const router = useRouter()
const user = ref(null)
const loading = ref(true)
const error = ref('')
async function load() {
  loading.value = true; error.value = ''
  try { user.value = await authService.profile() }
  catch (cause) { error.value = cause.message }
  finally { loading.value = false }
}
function logout() { authService.logout(); router.push('/') }
onMounted(load)
</script>
<template>
  <section class="shell account-page"><div class="section-kicker"><span>DS / CONTA</span><span>ÁREA DO CLIENTE</span></div><div v-if="loading" class="loading-state"><span class="loading-spinner"></span><p>Carregando seus dados…</p></div><FeedbackPanel v-else-if="error" title="Não conseguimos acessar sua conta" :message="error" action-label="Entrar novamente" @action="router.push('/entrar')" /><div v-else-if="user" class="account-card"><p class="eyebrow">SEUS DADOS</p><h1>Olá, <em>{{ user.name }}.</em></h1><div class="account-details"><div><span class="eyebrow">E-MAIL</span><strong>{{ user.email }}</strong></div><div v-if="user.phone"><span class="eyebrow">TELEFONE</span><strong>{{ user.phone }}</strong></div><div><span class="eyebrow">PERFIL</span><strong>{{ user.roles?.map((role) => role.replace('ROLE_', '')).join(', ') }}</strong></div></div><p class="account-note">Você pode consultar os pedidos quando tiver o identificador do pedido. A API atual não disponibiliza uma lista de pedidos da conta.</p><div class="cart-actions"><RouterLink class="button button-dark" to="/produtos">Explorar produtos <span aria-hidden="true">↗</span></RouterLink><button class="button button-outline" type="button" @click="logout">Sair da conta</button></div></div></section>
</template>
