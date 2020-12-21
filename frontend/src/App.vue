<template>
  <div id="app">
    <Push :closeOnNavigation="true" noOverlay>
      <router-link to="/">
        <b-icon-graph-up />
        <span>Home</span></router-link
      >
      <router-link to="/region">
        <b-icon-graph-up />
        <span>Regions</span></router-link
      >
      <router-link to="/images">
        <b-icon-image />
        <span>Images</span></router-link
      >
      <router-link to="/about">
        <b-icon-info-square />
        <span>About</span></router-link
      >
    </Push>
    <main id="page-wrap">
      <span style="font-size: 3em;" class="text-center">
        <img src="./assets/covid-19.png" alt="virus" style="height: 1em;" />
        &nbsp;
        <img src="./assets/italy.png" alt="flag" style="height: 1em;" />
      </span>
      <router-view class="mt-2" />
    </main>
  </div>
</template>

<script lang="ts">
import { Push } from "vue-burger-menu";
import { Component, Vue } from "vue-property-decorator";

@Component({
  components: { Push }
})
export default class Home extends Vue {}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

.bm-menu a {
  color: #b8b7ad;
}

.bm-menu a:focus,
.bm-menu a:hover {
  color: #c94e50;
  text-decoration: none;
}

.bm-burger-button > .bm-burger-bars {
  background-color: #777;
}

@media screen and (max-width: 40em) {
  main h1 {
    font-size: 2em;
  }

  main h2 {
    font-size: 1.8em;
  }

  main h3 {
    font-size: 1.5em;
  }
}

main {
  padding-top: 1pt;
}
</style>
