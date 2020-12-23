<template>
  <div id="app">
    <b-navbar id="navbar" type="light" variant="light">
      <b-collapse id="collapse-area" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/"><b-icon-house /> Home </b-nav-item>
          <b-nav-item to="/region"> <b-icon-graph-up /> Regions</b-nav-item>
          <b-nav-item to="/images"> <b-icon-image /> Images</b-nav-item>
          <b-nav-item to="/about"> <b-icon-info-square /> About</b-nav-item>
        </b-navbar-nav>
        <b-navbar-nav class="ml-auto">
          <b-nav-item>
            <locale-switcher></locale-switcher>
          </b-nav-item>
          <b-nav-brand>
            <img class="navbar-logo" src="./assets/covid-19.png" alt="virus" />
          </b-nav-brand>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
    <main id="page-wrap">
      <router-view class="mt-2" />
    </main>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import LocaleSwitcher from "@/components/LocaleSwitcher.vue";

    @Component({
  components: { LocaleSwitcher }
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
.navbar-logo {
  width: 50px;
}
</style>
