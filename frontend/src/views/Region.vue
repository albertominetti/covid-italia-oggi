<template>
    <div class="home">
        <div>
            <h1 v-if="lastDate != null" class="text-capitalize">{{lastDate | moment}}</h1>
            <h1 v-else>
                {{$t("select_region")}}
            </h1>
            <div class="d-inline-flex p-2">
                <b-form-select v-model="regionCode" :options="options" @change="loadData()" class="text-large"/>
            </div>
            <b-container v-show="lastDate" fluid="fluid" id="dashboard">
                <b-row no-gutters>
                    <b-col lg="12" xl="6">
                        <GenericChart :title='$t("intensive_care")' color="#9a9419" :series="inIntensiveCare"/>
                    </b-col>
                    <b-col lg="12" xl="6">
                        <GenericChart :title='$t("deceased")' color="#7b1a9c" :series="newDeceased"/>
                    </b-col>
                </b-row>
                <b-row no-gutters>
                    <b-col lg="12" xl="6">
                        <GenericChart :title='$t("new_postives")' color="#c26310" :series="newPositives"/>
                    </b-col>
                    <b-col lg="12" xl="6">
                        <GenericChart :title='$t("new_tests")' color="#12a8d2" :series="newTests"/>
                    </b-col>
                </b-row>
            </b-container>
            <p v-show="lastDate">
                {{$t("data_from")}} <em>{{$t("data_source")}}</em>
            </p>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {AXIOS} from "@/mixins/http-commons"; // TODO move in http-commons
    import moment from 'moment';
    import GenericChart from "@/components/GenericChart.vue";

    @Component({
        components: {GenericChart},
        filters: {
            moment(date: string) { // TODO move to utils
                return moment(date).format('dddd DD MMMM');
            }
        }
    })
    export default class Home extends Vue {
        private regionCode: string | null = null;
        private options = [
            {value: '01', text: 'Piemonte'},
            {value: '02', text: 'Valle d\'Aosta'},
            {value: '03', text: 'Lombardia'},
            {value: '21', text: 'P.A. Bolzano'},
            {value: '22', text: 'P.A. Trento'},
            {value: '05', text: 'Veneto'},
            {value: '06', text: 'Friuli Venezia Giulia'},
            {value: '07', text: 'Liguria'},
            {value: '08', text: 'Emilia Romagna'},
            {value: '09', text: 'Toscana'},
            {value: '10', text: 'Umbria'},
            {value: '11', text: 'Marche'},
            {value: '12', text: 'Lazio'},
            {value: '13', text: 'Abruzzo'},
            {value: '14', text: 'Molise'},
            {value: '15', text: 'Campania'},
            {value: '16', text: 'Puglia'},
            {value: '17', text: 'Basilicata'},
            {value: '18', text: 'Calabria'},
            {value: '19', text: 'Sicilia'},
            {value: '20', text: 'Sardegna'},
        ];
        private lastDate: Date | null = null;

        private inIntensiveCare: Point[] = [];
        private newDeceased: Point[] = [];
        private newPositives: Point[] = [];
        private newTests: Point[] = [];

        public loadData() {
            if (!this.regionCode) {
                return;
            }
            AXIOS.get("/api/data/regions/" + this.regionCode) // TODO move outside
                .then(response => {
                    this.lastDate = new Date(response.data.lastDate);
                    const startDate = new Date(response.data.startDate);
                    this.inIntensiveCare = Home.prepareTimeSeries(startDate, response.data.inIntensiveCare);
                    this.newDeceased = Home.prepareTimeSeries(startDate, response.data.newDeceased);
                    this.newPositives = Home.prepareTimeSeries(startDate, response.data.newPositives);
                    this.newTests = Home.prepareTimeSeries(startDate, response.data.newTests);
                })
                .catch(e => {
                    console.log("Sorry...", e) // TODO better exception handling
                })
        }

        private static prepareTimeSeries(firstDate: Date, rawSeries: number []): Point[] {
            const series: Point[] = [];
            const currDate = new Date(firstDate);
            for (const e of rawSeries) {
                series.push({
                    x: currDate.getTime(), y: e
                });
                currDate.setDate(currDate.getDate() + 1);
            }
            return series;
        }
    }

    interface Point { // TODO import the interface that belong to GenericChart.vue
        x: number;
        y: number;
    }

</script>

<style>
    @media screen and (max-width: 40em) {
        #dashboard {
            padding: 0;
        }
    }
    .text-large {
        font-size: 30px;
    }
</style>