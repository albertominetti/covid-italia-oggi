<template>
  <div class="home">
    <div>
      <h1 v-if="covidData" class="text-capitalize">
        {{ covidData.lastDate | moment }}
      </h1>
      <h1 v-else>
        {{ $t("select_region") }}
      </h1>
      <div class="d-inline-flex p-2">
        <b-form-select
          v-model="regionCode"
          :options="options"
          @change="loadData()"
          class="text-large"
        />
      </div>
      <b-container v-if="covidData" fluid="fluid" id="dashboard">
        <b-row no-gutters>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('intensive_care')"
              :series="covidData.inIntensiveCareTs"
              group="region"
              color="#9a9419"
            />
          </b-col>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('deceased')"
              :series="covidData.newDeceasedTs"
              group="region"
              color="#7b1a9c"
            />
          </b-col>
        </b-row>
        <b-row no-gutters>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('new_postives')"
              :series="covidData.newPositivesTs"
              group="region"
              color="#c26310"
            />
          </b-col>
          <b-col lg="12" xl="6">
            <GenericChart
              :title="$t('new_tests')"
              :series="covidData.newTestsTs"
              group="region"
              color="#12a8d2"
            />
          </b-col>
        </b-row>
      </b-container>
      <p v-if="covidData">
        {{ $t("data_from") }} <em>{{ $t("data_source") }}</em>
      </p>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
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
export default class Region extends Vue {
  private regionCode: string | null = null;
  private covidData: CovidData | null = null;

  private options = [
    { value: "01", text: "Piemonte" },
    { value: "02", text: "Valle d'Aosta" },
    { value: "03", text: "Lombardia" },
    { value: "21", text: "P.A. Bolzano" },
    { value: "22", text: "P.A. Trento" },
    { value: "05", text: "Veneto" },
    { value: "06", text: "Friuli Venezia Giulia" },
    { value: "07", text: "Liguria" },
    { value: "08", text: "Emilia Romagna" },
    { value: "09", text: "Toscana" },
    { value: "10", text: "Umbria" },
    { value: "11", text: "Marche" },
    { value: "12", text: "Lazio" },
    { value: "13", text: "Abruzzo" },
    { value: "14", text: "Molise" },
    { value: "15", text: "Campania" },
    { value: "16", text: "Puglia" },
    { value: "17", text: "Basilicata" },
    { value: "18", text: "Calabria" },
    { value: "19", text: "Sicilia" },
    { value: "20", text: "Sardegna" }
  ];

  public async loadData() {
    if (!this.regionCode) {
      return;
    }
    await CovidDataApi.getRegionalCovidData(this.regionCode)
      .then(covidData => {
        this.covidData = covidData;
      })
      .catch(e => {
        console.log("Sorry...", e); // TODO better exception handling
      });
  }
}
</script>

<style scoped>
@media screen and (max-width: 40em) {
  #dashboard {
    padding: 0;
  }
}
.text-large {
  font-size: 30px;
}
</style>
