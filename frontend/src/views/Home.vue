<template>
    <div class="home">
        <img alt="Vue logo" src="../assets/logo.png">
        <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>
        <button class=”Search__button” @click="callRestService()">CALL Spring Boot REST backend service</button>
        <h3>{{ response }}</h3>
    </div>
</template>

<script lang="ts">

    import {Component, Vue} from 'vue-property-decorator';
    import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
    import {AXIOS} from './../http-commons'


    @Component({
        components: {
            HelloWorld,
        },

    })
    export default class Home extends Vue {
        private response: string[] = [];
        private errors: string[] = [];

        public callRestService() {
            AXIOS.get(`api/graphs`)
                .then(response => {
                    // JSON responses are automatically parsed.
                    this.response = response.data
                })
                .catch(e => {
                    this.errors.push(e)
                })
        }
    }
</script>
