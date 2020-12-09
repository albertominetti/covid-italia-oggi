<template>
    <div class="about">
        <h3>{{$t("my_name_is")}} <a id="me">Alberto</a>.</h3>

        <div v-show="selectedPlace === 'lausanne'">
            <figure class="figure">
                <img src="../assets/december-in-lausanne.jpg" class="figure-img img-fluid rounded" width="502px">
                <figcaption class="figure-caption text-right">December in Lausanne.</figcaption>
            </figure>
        </div>
        <div v-show="selectedPlace === 'zurich'">
            <figure class="figure">
                <img src="../assets/november-in-zurich.jpg" class="figure-img img-fluid rounded" width="502px">
                <figcaption class="figure-caption text-right">November in Zurich.</figcaption>
            </figure>
        </div>

        <span>
            View the whole project on <a href="https://github.com/albertominetti/covid-italia-oggi">github</a>.
        </span>
    </div>
</template>

<script lang="ts">
    import {Vue} from 'vue-property-decorator';

    export default class Images extends Vue {
        private static places = ["zurich", "lausanne"];
        private selectedPlace: string = this.randomPlace();

        private randomPlace(): string {
            return Images.places[Math.floor(Math.random() * Images.places.length)]
        }
    }
</script>