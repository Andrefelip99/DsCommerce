<template>
  <footer class="site-footer">
    <div class="shell footer-main">
      <RouterLink class="store-brand footer-brand" to="/" aria-label="DsCommerce, página inicial">
        <span class="store-brand-mark" aria-hidden="true">DS</span><span class="store-brand-name">dscommerce</span>
      </RouterLink>
      <p>Uma seleção para acompanhar seus dias.<br /><span>Compra simples. Escolhas duradouras.</span></p>
      <nav aria-label="Links do rodapé"><RouterLink to="/produtos">Produtos</RouterLink><RouterLink to="/carrinho">Sacola</RouterLink><RouterLink to="/entrar">Acessar conta</RouterLink></nav>
    </div>
    <div class="shell footer-bottom"><span>DsCommerce</span><span>Produtos e informações fornecidos pela loja.</span></div>
  </footer>
</template>
