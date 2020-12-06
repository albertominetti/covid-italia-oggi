<template>
    <div>
        <div v-if="day != null">
            <h2>{{day | moment}}</h2>
            <graph-image v-for="u in urls" :key="u" :url="u"></graph-image>
            <p>
                I dati provengono da <em>Presidenza del Consiglio dei Ministri - Dipartimento della Protezione Civile</em>
            </p>
            <p>
                Le immagini sono generate giornalmente con <em>R scripting version 3.6.2 (2019-12-12)</em>
            </p>
        </div>
        <div v-else class="spinner-border" role="status">
            <span class="sr-only">Loading...</span>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import GraphImage from '@/components/GraphImage.vue';
    import {AXIOS} from "@/mixins/http-commons";
    import moment from 'moment';

    @Component({
        components: {
            GraphImage
        },
        filters: {
            moment(date: string) {
                return moment(date).format('dddd DD MMMM');
            }
        }
    })
    export default class Images extends Vue {
        private errors: string[] = []; // TODO exception handling
        private day: string | null = null;
        private urls: string[] | null = null;

        public created() {
            this.loadImageUrls()
        }

        public loadImageUrls() {
            AXIOS.get(`api/images`)
                .then(response => {
                    this.day = response.data.day
                    this.urls = response.data.urls
                })
                .catch(e => {
                    this.errors.push(e)
                })
        }

    }
</script>

<style scoped>

</style>