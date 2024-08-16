<template>
  <div id="login">
    <form v-on:submit.prevent="login">
      <h1 >Sign In to Super Pizza</h1>
      <div role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
      <div role="alert" v-if="this.$route.query.registration">
        Thank you for registering, please sign in.
      </div>
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <button type="submit">Sign in</button>
      <p>
      <router-link v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link></p>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false,
      showToast: false
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
            
            
          }
        })
        .catch(error => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 1px solid #d9d8d8;
  border-radius: 15px;
  padding: 20px 30px;
  width: 400px;
  margin: 30px 0px;
  max-height: 600px;
}

form button {
  appearance: none;
    background-color: #e61d25;
    color: #fff;
    text-transform: uppercase;
    border: none;
    padding: 0.5rem 1rem;
    font-size: 1em;
    letter-spacing: 0.15em;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
}

form button:hover {
  color: #fff;
  background-color: #000;
}
.form-input-group {
  margin-bottom: 1rem;
}
label {
  margin-right: 0.5rem;
}

#login {
  margin-top: 8em;
  min-height: 80vh;
  display: flex;
  justify-content: center;
}

.router-link-active, .router-link-active:visited, .router-link-active:active {
        text-decoration: none;
        color: black;
    }

    


</style>