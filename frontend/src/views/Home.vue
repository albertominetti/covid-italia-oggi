<template>
  <div class="home">
    <div>
      <h1 v-if="covidData" class="text-capitalize">
        {{ covidData.lastDate | moment }}
      </h1>
      <div v-else class="spinner-border" role="status">
        <span class="sr-only">{{ $t("loading") }}...</span>
      </div>
      <b-container v-if="covidData" fluid="fluid" id="dashboard">
        <b-row no-gutters>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('intensive_care')"
              color="#9a9419"
              :series="covidData.inIntensiveCareTs"
            />
          </b-col>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('deceased')"
              color="#7b1a9c"
              :series="covidData.newDeceasedTs"
            />
          </b-col>
        </b-row>
        <b-row no-gutters>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('new_postives')"
              color="#c26310"
              :series="covidData.newPositivesTs"
            />
          </b-col>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('new_tests')"
              color="#12a8d2"
              :series="covidData.newTestsTs"
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
import moment from "moment";
import GenericChart from "@/components/GenericChart.vue";
import CovidDataApi from "@/api/CovidDataApi";
import CovidData from "@/api/model/CovidData";

@Component({
  components: { GenericChart },
  filters: {
    moment(date: Date) {
      // TODO move to utils
      return moment(date).format("dddd DD MMMM");
    }
  }
})
export default class Home extends Vue {
  private covidData: CovidData | null = null;

  public mounted() {
    this.loadData();
  }

  async loadData() {
    await CovidDataApi.getNationalCovidData()
      .then(covidData => {
        this.covidData = covidData;
      })
      .catch(e => {
        console.log("Sorry...", e); // TODO better exception handling
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
