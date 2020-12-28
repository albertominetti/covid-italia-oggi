<template>
  <b-nav-item-dropdown id="my-nav-dropdown" :text="value.toUpperCase()">
    <b-dropdown-item
      v-for="(locale, index) in locales"
      :key="index"
      @click="setLocale(locale.value)"
    >
      <lang-flag :iso="locale.value" :title="locale.text" :squared="false" />
      {{ locale.text }}
    </b-dropdown-item>
  </b-nav-item-dropdown>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import LangFlag from "vue-lang-code-flags";

    @Component({ components: { LangFlag } })
export default class LocaleSwitcher extends Vue {
  @Prop({ required: true }) locales!: { text: string; value: string }[];
  @Prop({ required: true }) value!: string;

  setLocale(locale: string): void {
    this.$emit("input", locale);
    this.$emit("change", locale);
  }
}
</script>

<style scoped></style>
