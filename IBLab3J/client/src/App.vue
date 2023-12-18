<script>
import Auth from "@/services/auth.js";

export default {
  data() {
    return {
      auth: new Auth()
    }
  },
  provide() {
    return {
      auth: this.auth
    }
  }
}


</script>


<template>
  <router-view></router-view>
</template>

<style scoped>

</style>
