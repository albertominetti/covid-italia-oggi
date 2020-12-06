<template>
    <div id="app">
        <Push :closeOnNavigation="true" noOverlay>
            <router-link to="/"><span>Home</span></router-link>
            <router-link to="/images"><span>Images</span></router-link>
            <router-link to="/about"><span>About</span></router-link>
        </Push>
        <main id="page-wrap">
            <router-view class="mt-4"/>
        </main>
    </div>
</template>

<script lang="ts">
    import {Push} from 'vue-burger-menu'
    import {Component, Vue} from "vue-property-decorator";

    @Component({
        components: {Push}
    })
    export default class Home extends Vue {

    }
</script>

<style>
    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
    }

    #nav {
        padding: 30px;
    }

    #nav a {
        font-weight: bold;
        color: #2c3e50;
    }

    #nav a.router-link-exact-active {
        color: #42b983;
    }
</style>
