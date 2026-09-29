import { createRouter, createWebHistory } from 'vue-router'
import { authService } from '../services/authService'
import HomeView from '../views/HomeView.vue'
import ProductsView from '../views/ProductsView.vue'
import ProductDetailsView from '../views/ProductDetailsView.vue'
import CartView from '../views/CartView.vue'
import LoginView from '../views/LoginView.vue'
import AccountView from '../views/AccountView.vue'
import NotFoundView from '../views/NotFoundView.vue'
const router = createRouter({
  history: createWebHistory(), scrollBehavior: (to) => to.hash ? { el: to.hash, behavior: 'smooth' } : { top: 0, behavior: 'smooth' },
  routes: [
    { path: '/', name: 'home', component: HomeView, meta: { title: 'Início' } },
    { path: '/produtos', name: 'products', component: ProductsView, meta: { title: 'Produtos' } },
    { path: '/produtos/:id', name: 'product', component: ProductDetailsView, meta: { title: 'Produto' } },
    { path: '/carrinho', name: 'cart', component: CartView, meta: { title: 'Sacola' } },
    { path: '/entrar', name: 'login', component: LoginView, meta: { title: 'Entrar' } },
    { path: '/conta', name: 'account', component: AccountView, meta: { title: 'Minha conta', requiresAuth: true } },
    { path: '/:pathMatch(.*)*', name: 'not-found', component: NotFoundView, meta: { title: 'Página não encontrada' } },
  ],
})
router.beforeEach((to) => {
  document.title = `${to.meta.title || 'Loja'} — DsCommerce`
  if (to.meta.requiresAuth && !authService.isAuthenticated) return { name: 'login', query: { redirect: to.fullPath } }
})
export default router
