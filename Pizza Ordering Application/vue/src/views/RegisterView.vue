<template>
  <div id="register" class="text-center">
    <form v-on:submit.prevent="register">
      <h1>Create Account</h1>
      <div role="alert" v-if="registrationErrors">
        {{ registrationErrorMsg }}
      </div>
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <div class="form-input-group">
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" required />
      </div>
      <button type="submit">Create Account</button>
      <p><router-link v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
    </form>
  </div>
</template>

<script>
import authService from '../services/AuthService';

export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
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
  display: flex;
  justify-content: space-between;
  width: 80%;
}
label {
  margin-right: 0.5rem;
}

#register {
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
