<template>
    <div>
        <div v-if="covidImages">
            <h1 class="text-capitalize">{{covidImages.day | moment}}</h1>
            <graph-image v-for="u in covidImages.urls" :key="u" :url="u"></graph-image>
            <p>
                {{$t("data_from")}} <em>{{$t("data_source")}}</em>
            </p>
            <p>
                {{$t("images_from")}} <em>R scripting version 3.6.2 (2019-12-12)</em>
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
    import moment from 'moment';
    import CovidImagesApi from "@/api/CovidImagesApi";
    import {CovidImagesModel} from "@/api/model/CovidImages";

    @Component({
        components: {GraphImage},
        filters: {
            moment(date: Date) {
                return moment(date).format('dddd DD MMMM');
            }
        }
    })
    export default class Images extends Vue {
        private covidImages: CovidImagesModel | null = null;

        public created() {
            this.loadImageUrls()
        }

        public async loadImageUrls() {
            await CovidImagesApi.getImages()
                .then(covidImages => {
                    this.covidImages = covidImages;
                })
                .catch(e => {
                    console.log("Sorry...", e)
                })
        }

    }
</script>

<style scoped>

</style>