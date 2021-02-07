<template>
  <div class="home">
    <div>
      <h1 v-if="covidData" class="text-capitalize">
        {{ covidData.lastDate | displayDate }}
      </h1>
      <div v-else class="spinner-border" role="status">
        <span class="sr-only">{{ $t("loading") }}...</span>
      </div>
      <b-container v-if="covidData" fluid="fluid" id="dashboard">
        <b-row no-gutters>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('intensive_care')"
              :series="covidData.inIntensiveCareTs"
              color="#9a9419"
            />
          </b-col>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('deceased')"
              :series="covidData.newDeceasedTs"
              color="#7b1a9c"
            />
          </b-col>
        </b-row>
        <b-row no-gutters>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('new_postives')"
              :series="covidData.newPositivesTs"
              color="#c26310"
            />
          </b-col>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('new_tests')"
              :series="covidData.newTestsTs"
              color="#12a8d2"
            />
          </b-col>
        </b-row>
      </b-container>
      <p>
        {{ $t("data_from") }} <em>{{ $t("data_source") }}</em>
      </p>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import GenericChart from "@/components/GenericChart.vue";
import CovidDataApi from "@/api/CovidDataApi";
import CovidData from "@/api/model/CovidData";
import { displayDate } from "@/mixins/utils";

@Component({
  components: { GenericChart },
  filters: {
    displayDate
  }
})
export default class Home extends Vue {
  private covidData: CovidData | null = null;

  public mounted() {
    this.loadData();
  }

  async loadData() {
    await CovidDataApi.getNationalCovidData().then(covidData => {
      this.covidData = covidData;
    });
  }
}
</script>

<style>
@media screen and (max-width: 40em) {
  #dashboard {
    padding: 0;
  }
}
</style>
