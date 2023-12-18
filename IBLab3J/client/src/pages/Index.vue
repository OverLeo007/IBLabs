

<script>
import Header from "@/components/Header.vue";

export default {
  name: "Index",
  inject: ['auth'],
  components: {Header},
  async mounted() {
    await this.auth.getSessionId();
    await this.auth.accessCheck(this.$route.path);
  }
}
</script>

<template>
  <Header></Header>


</template>

<style scoped>

</style>