<template>
    <div class="home">
        <div>
            <h2 v-if="lastDate != null">{{lastDate | moment}}</h2>
            <div v-else class="spinner-border" role="status">
                <span class="sr-only">Loading...</span>
            </div>
            <b-container fluid="fluid">
                <b-row no-gutters>
                    <b-col lg="12" xl="6">
                        <GenericChart title="Terapia intensiva" color="#9a9419" :series="inIntensiveCare"/>
                    </b-col>
                    <b-col lg="12" xl="6">
                        <GenericChart title="Deceduti" color="#7b1a9c" :series="newDeceased"/>
                    </b-col>
                </b-row>
                <b-row no-gutters>
                    <b-col lg="12" xl="6">
                        <GenericChart title="Nuovi positivi" color="#c26310" :series="newPositives"/>
                    </b-col>
                    <b-col lg="12" xl="6">
                        <GenericChart title="Tamponi" color="#12a8d2" :series="newTests"/>
                    </b-col>
                </b-row>
            </b-container>
            <p>
                I dati provengono da <em>Presidenza del Consiglio dei Ministri - Dipartimento della Protezione
                Civile</em>
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
        private lastDate: Date | null = null;
        private static totalPopulation = 60244639; // TODO useless, please review

        private inIntensiveCare: Point[] = [];
        private newDeceased: Point[] = [];
        private newPositives: Point[] = [];
        private newTests: Point[] = [];

        public mounted() {
            this.loadData();
        }

        public loadData() {
            AXIOS.get("/api/data") // TODO move outside
                .then(response => {
                    this.lastDate = new Date(response.data.lastDate);
                    const startDate = new Date(response.data.startDate);
                    this.inIntensiveCare = Home.prepareTimeSeries(startDate, response.data.inIntensiveCare);
                    this.newDeceased = Home.prepareTimeSeries(startDate, response.data.newDeceased);
                    this.newPositives = Home.prepareTimeSeries(startDate, response.data.newPositives);
                    this.newTests = Home.prepareTimeSeries(startDate, response.data.newTests);
                })
                .catch(e => {
                    console.log("Sorry...", e)
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
