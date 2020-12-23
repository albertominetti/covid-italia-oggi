<template>
  <b-form-select
    v-model="$i18n.locale"
    :options="supportedLocales"
    class="text-large"
    label-field="Lang"
  />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { supportedLocales } from "@/i18n";

@Component
export default class LocaleSwitcher extends Vue {
  get supportedLocales(): { text: string; value: string }[] {
    return Object.entries(supportedLocales).map(locale => ({
      value: locale[0],
      text: locale[1]
    }));
  }
}
</script>

<style scoped></style>
