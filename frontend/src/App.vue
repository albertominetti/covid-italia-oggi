<template>
  <div id="app">
    <b-navbar id="navbar" type="light" variant="light" toggleable="lg">
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/"><b-icon-house /> Home </b-nav-item>
          <b-nav-item to="/region"> <b-icon-graph-up /> Regions</b-nav-item>
          <b-nav-item to="/images"> <b-icon-image /> Images</b-nav-item>
          <b-nav-item to="/about"> <b-icon-info-square /> About</b-nav-item>
        </b-navbar-nav>
        <b-navbar-nav class="ml-auto">
          <b-nav-item>
            <locale-switcher
              v-model="$i18n.locale"
              :locales="locales"
              @change="onLocaleChanged"
            ></locale-switcher>
          </b-nav-item>
          <b-nav-item>
            <img class="navbar-logo" src="./assets/covid-19.png" alt="virus" />
          </b-nav-item>
          <b-nav-item>
            <img class="navbar-logo" src="./assets/italy.png" alt="italy" />
          </b-nav-item>
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
    import {supportedLocales} from "@/i18n";
    import moment from "moment";

    @Component({
  components: { LocaleSwitcher }
})
export default class App extends Vue {
  onLocaleChanged(locale: string): void {
    moment.locale(locale);
  }

  get locales(): { text: string; value: string }[] {
    return Object.entries(supportedLocales).map(locale => ({
      value: locale[0],
      text: locale[1]
    }));
  }
}
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
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

.navbar-logo {
  max-width: 40px;
}
</style>
