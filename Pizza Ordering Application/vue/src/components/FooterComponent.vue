<template>
  <div class="footer">
    <footer>
      <div class="footer-logo">
        <img
          src="../../public/Super_Pizza_Mascot_Logo-removebg-preview.png"
          alt="Pizza"
          style="width: 55%"
        />
      </div>
      <div class="footer-menu">
        <h3>Pages</h3>
        <ul class="footer-nav-links" id="footer-nav-links">
          <router-link v-bind:to="{ name: 'home' }">
            <li>CUSTOM PIZZA</li>
          </router-link>
          <router-link v-bind:to="{ name: 'home' }">
            <li>SPECIALTY PIZZA</li>
          </router-link>
          <router-link v-bind:to="{ name: 'home' }">
            <li>DRINKS</li>
          </router-link>
        </ul>
      </div>
      <div class="socials">
        <h3>Follow Us</h3>
        <img src="../../public/facebook.png" alt="Facebook" />
        <img src="../../public/instagram.png" alt="Instagram" />
        <img src="../../public/twitter.png" alt="Twitter" />
        <img src="../../public/tiktok.png" alt="Youtube" />

      </div>
      <div class="newsletter">
        <h3>Subscribe</h3>
        <input type="email" placeholder="Enter your email" />
        <button class="subscribe-btn">Subscribe</button>
        </div>
    </footer>
  </div>
</template>

<script></script>

<style scoped>
    footer {
        display: grid;
        position: relative;
        bottom: 0px;
        height: 100%;
        z-index: 99;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        border-bottom: #e0e0e0 solid 1px;
        background-color: #fff;
        box-shadow: 0 -4px 2px 0px rgba(0, 0, 0, 0.1);
        margin-top: 20px;
    }



    footer ul {
        display: flex;
        flex-direction: column;
        align-items: start;
        list-style: none;
        gap: 10px;
        max-width: 100%;
        padding: 0px;
    }

    .footer-nav-links .router-link-active:hover {
        color: #e61d25;
    }

    footer .socials img {
        width: 30px;
        margin-right: 10px;
        cursor: pointer;
    }



    footer .subscribe-btn {
        background-color: #e61d25;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    footer .subscribe-btn:hover {
        background-color: #e61d25;
        color: white;
    }

    footer input {
        padding: 10px;
        border: 1px solid #e0e0e0;
        border-radius: 5px;
        width: 50%;
        margin-bottom: 10px;
    }

    .router-link-active, .router-link-active:visited, .router-link-active:active {
        text-decoration: none;
        color: black;
    }

    @media screen and (max-width: 480px) {
      footer {
        grid-template-columns: 1fr;
        justify-items: center;
      }

      .footer-logo {
        text-align: center;
      }
    }
</style>
