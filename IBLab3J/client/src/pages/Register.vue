<script>
import axios from "axios";
import {ROOT_URL} from "@/properties.js";
import {decrypt, encrypt} from "@/services/encDec.js";

export default {
  inject: ["auth"],
  name: "Register",
  data() {
    return {
      username: null,
      password: null,
      error: null
    }
  },
  async mounted() {
    await this.auth.getSessionId();
    await this.auth.accessCheck(this.$route.path);
  },
  methods: {
    async submit_user() {
      if (!this.username || !this.username.trim() || !this.password || !this.password.trim()) {
        this.error = "Логин и пароль не могут быть пустыми";
        return;
      }

      if (this.username[0] === ' ' || this.password[0] === ' ') {
        this.error = "Логин и пароль не могут начинаться с пробела";
        return;
      }

      try {
        const response = await axios.post(`${ROOT_URL}/auth/register`, {
          username: encrypt(this.username, this.auth.K),
          password: encrypt(this.password, this.auth.K),
          sessionId: localStorage.getItem('sessionId')
        });
        console.log(decrypt(response.data.token, this.auth.K))
        localStorage.setItem('token', decrypt(response.data.token, this.auth.K));
        this.error = null;  // Сброс ошибки
        this.$router.push('/');
        await this.auth.accessCheck(this.$route.path);

      } catch (error) {
        if (error.response && error.response.status === 422) {
          // Если ошибка 409 (Conflict), показываем сообщение о конфликте
          this.error = "Такой логин уже существует";
        } else {
          // Если другая ошибка, выводим в консоль и обнуляем ошибку (если она уже была)
          console.error('Произошла ошибка:', error);
          this.error = null;
        }
      }
    }
  }
}
</script>

<template>
  <div class="container mt-5">
    <form>
      <div class="card blue-grey darken-4">
        <div class="card-content white-text">
          <span class="card-title">Registration</span>
          <div class="row">
            <div class="col s12">
              <div class="input-field">
                <label for="username" class="white-text">Username:</label>
                <input v-model="username" type="text" id="username"/>
              </div>

              <div class="input-field">
                <label for="password" class="white-text">Password:</label>
                <input v-model="password" type="password" id="password"/>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <div v-show="error" class="new badge red" role="alert">
                {{ error }}
              </div>
            </div>
          </div>

          <div class="row">
            <button
                class="col s12 m4 l4 btn waves-effect waves-light green"
                type="submit"
                @click="submit_user()"
                name="action">
              Registration
              <i class="material-icons right">check</i>
            </button>
            <div class="col s12 m4 l4"></div>
            <router-link class="col s12 m4 l4 btn waves-effect waves-light blue" to="/login">
              Login
              <i class="material-icons right">chevron_right</i>
            </router-link>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<style scoped>

</style>