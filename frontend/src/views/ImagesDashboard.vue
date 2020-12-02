<template>
    <div>
        <h1>
            <span v-if="day != null">{{day | moment}}</span>
            <span v-else>... loading ...</span>
        </h1>
        <graph-image v-for="u in urls" :key="u" :url="u"></graph-image>
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
                return moment(date).format('dddd DD MMMM yyyy');
            }
        }
    })
    export default class ImagesDashboard extends Vue {
        private errors: string[] = []; // TODO exception handling
        private day: string | null = null;
        private urls: string[] | null = null;

        public created() {
            this.loadImageUrls()
        }

        public loadImageUrls() {
            AXIOS.get(`api/graphs`)
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