<template>
  <div class="about">
    <h3>{{ $t("my_name_is") }} <a id="me">Alberto</a>.</h3>

    <div v-show="selectedPlace === 'lausanne'">
      <figure class="figure">
        <img
          src="../assets/december-in-lausanne.jpg"
          class="figure-img img-fluid rounded"
          width="502px"
          alt="Me in Lausanne"
        />
        <figcaption class="figure-caption text-right">
          {{ $t("december_in_lausanne") }}
        </figcaption>
      </figure>
    </div>
    <div v-show="selectedPlace === 'zurich'">
      <figure class="figure">
        <img
          src="../assets/november-in-zurich.jpg"
          class="figure-img img-fluid rounded"
          width="502px"
          alt="Me in Zurich"
        />
        <figcaption class="figure-caption text-right">
          {{ $t("november_in_zurich") }}
        </figcaption>
      </figure>
    </div>

    <span>
      {{ $t("view_on_github") }}
      <a href="https://github.com/albertominetti/covid-italia-oggi">github</a>.
    </span>
  </div>
</template>

<script lang="ts">
import { Vue } from "vue-property-decorator";

export default class Images extends Vue {
  private static places = ["zurich", "lausanne"];
  private selectedPlace: string = Images.randomPlace();

  private static randomPlace(): string {
    return Images.places[Math.floor(Math.random() * Images.places.length)];
  }
}
</script>
