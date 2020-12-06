<template>
    <div class="home">
        <div v-if="lastDate">
            <h2>{{lastDate | moment}}</h2>
            <b-container fluid="fluid" class="p-1">
                <b-row no-gutters>
                    <b-col lg="12" xl="6">
                        <apexchart :options="getChartOptions('Terapia intensiva', '#9a9419')"
                                   :series="inIntensiveCare" class="chart"/>
                    </b-col>
                    <b-col lg="12" xl="6">
                        <apexchart :options="getChartOptions('Deceduti', '#7b1a9c')"
                                   :series="newDeceased" class="chart"/>
                    </b-col>
                </b-row>
                <b-row no-gutters>
                    <b-col lg="12" xl="6">
                        <apexchart :options="getChartOptions('Nuovi positivi', '#c26310')"
                                   :series="newPositives" class="chart"/>
                    </b-col>
                    <b-col lg="12" xl="6">
                        <apexchart :options="getChartOptions('Tamponi', '#12a8d2')"
                                   :series="newTests" class="chart"/>
                    </b-col>
                </b-row>
            </b-container>
            <p>
                I dati provengono da <em>Presidenza del Consiglio dei Ministri - Dipartimento della Protezione Civile</em>
            </p>
        </div>
        <div v-else class="spinner-border" role="status">
            <span class="sr-only">Loading...</span>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import apexchart from 'vue-apexcharts/dist/vue-apexcharts';
    import {AXIOS} from "@/mixins/http-commons";
    import moment from 'moment';

    @Component({
        components: {apexchart},

        filters: {
            moment(date: string) {
                return moment(date).format('dddd DD MMMM');
            }
        }
    })
    export default class Home extends Vue {
        private height = 400; // TODO useless, please review
        private lastDate: Date | null = null;
        private checked = false; // TODO useless, please review
        private static totalPopulation = 60244639; // TODO useless, please review

        private inIntensiveCare: ApexAxisChartSeries = [];
        private newDeceased: ApexAxisChartSeries = [];
        private newPositives: ApexAxisChartSeries = [];
        private newTests: ApexAxisChartSeries = [];

        public mounted() {
            this.loadData();
        }

        public getChartOptions(title: string, color: string): ApexCharts.ApexOptions {
            return {
                chart: {
                    id: title,
                    group: 'social',
                    type: 'area',
                    animations: {
                        enabled: true,
                    },
                    toolbar: {
                        tools: {
                            download: true,
                        }
                    },
                    zoom: {
                        enabled: false,
                        autoScaleYaxis: false
                    },
                    dropShadow: {
                        enabled: false,
                        color: '#000',
                        top: 18,
                        left: 7,
                        blur: 10,
                        opacity: 0.2
                    },
                },
                stroke: {
                    width: 2,
                    curve: "straight"
                },
                grid: {
                    row: {
                        colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                        opacity: 0.5
                    }
                },
                title: {
                    text: title,
                    align: 'center'
                },
                dataLabels: {
                    enabled: false
                },
                colors: [color],
                yaxis: {
                    labels: {
                        minWidth: 0,
                        align: 'right',
                        show: true,
                        formatter(val: number, opts?: any): string {
                            return val.toLocaleString(undefined, {useGrouping: true});
                        }
                    },
                    min: 0,
                    forceNiceScale: true
                },
                tooltip: {
                    x: {
                        show: true,
                        formatter(val: number, opts?: any): string {
                            return moment(val).format("ddd DD MMMM");
                        }
                    },
                },
                markers: {
                    size: 0
                },
                xaxis: {
                    type: 'datetime',
                    labels: {
                        datetimeFormatter: {
                            year: 'yyyy',
                            month: 'MMM \'yy',
                            day: 'dd MMM',
                            hour: 'HH:mm'
                        }
                    },
                    tooltip: {
                        enabled: false
                    }
                },
                fill: {
                    type: "gradient",
                    colors: [color, "transparent"],
                    gradient: {
                        shadeIntensity: 1,
                        opacityFrom: 0.7,
                        opacityTo: 0.9,
                        stops: [0, 100]
                    }
                }
            };
        }

        public loadData() {
            AXIOS.get("/api/data") // TODO move outside
                .then(response => {
                    this.lastDate = new Date(response.data.lastDate);
                    const startDate = new Date(response.data.startDate);
                    this.inIntensiveCare = this.prepareTimeSeries("Terapia intensiva", startDate, response.data.inIntensiveCare);
                    this.newDeceased = this.prepareTimeSeries("Deceduti", startDate, response.data.newDeceased);
                    this.newPositives = this.prepareTimeSeries("Nuovi Positivi", startDate, response.data.newPositives);
                    this.newTests = this.prepareTimeSeries("Tamponi", startDate, response.data.newTests);
                })
                .catch(e => {
                    console.log("Sorry...", e)
                })
        }

        private prepareTimeSeries(title: string, firstDate: Date, rawSeries: number []): ApexAxisChartSeries {
            const series: Point[] = [];
            const currDate = new Date(firstDate);
            for (const e of rawSeries) {
                series.push({
                    x: currDate.getTime(),
                    y: e
                });
                currDate.setDate(currDate.getDate() + 1);
            }
            return [{
                name: title,
                data: series
            }]
        }
    }

    interface Point {
        x: number;
        y: number;
    }

</script>
