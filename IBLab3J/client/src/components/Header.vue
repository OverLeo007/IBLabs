<script>
export default {
  name: "Header",
  methods: {
    logout() {
      localStorage.removeItem('token');

    }
  }
}
</script>

<template>
  <header class="navbar-fixed">
    <nav>
      <div class="nav-wrapper blue-grey darken-1">
        <a href="/instruments" class="brand-logo">В начало</a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
          <li><router-link @click="logout()" to="/login" class="nav-link">Выйти</router-link></li>
        </ul>
      </div>
    </nav>
  </header>
</template>

<style scoped>

</style>