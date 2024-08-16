<template>
  <div class="home">
    <HeroComponent></HeroComponent>
    <FeaturedComponent></FeaturedComponent>
  </div>
</template>

<script>
import HeroComponent from "../components/HeroComponent.vue";
import FeaturedComponent from "../components/FeaturedComponent.vue";

export default {
  components: {
    HeroComponent,
    FeaturedComponent,
  },
};
</script>
<style scoped>
.home {
  width: 100vw;
}
@media screen and (max-width: 480px) {
  .home {
    margin-bottom: 1400px;
  }
}
</style>
