<template>
    <apexchart :options="options" :series="apexSeries" type="area"/>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import moment from 'moment';
    import apexchart, {apexDefaultLocale, apexLocales} from "@/mixins/apex-charts";

    @Component({
        components: {apexchart},
    })
    export default class GenericChart extends Vue {
        @Prop({required: true}) readonly title!: string;
        @Prop({required: true}) readonly color!: string;
        @Prop({required: true}) readonly series!: Point[];

        private height = 400; // TODO useless, please review

        private get options(): ApexCharts.ApexOptions {
            return this.getChartOptions(this.title, this.color);
        }

        private get apexSeries(): ApexAxisChartSeries {
            return [{name: this.title, data: this.series}];
        }

        private getChartOptions(title: string, color: string): ApexCharts.ApexOptions {
            return {
                chart: {
                    locales: apexLocales,
                    defaultLocale: apexDefaultLocale,
                    id: title,
                    group: 'dashboard-group',
                    type: 'area',
                    animations: {
                        enabled: true,
                    },
                    toolbar: {
                        show: true,
                        autoSelected: "zoom",
                        tools: {
                            download: true, zoom: true, reset: true, selection: true,
                            zoomin: false, zoomout: false, pan: false
                        }
                    },
                    zoom: {
                        enabled: true,
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
                noData: {
                    text: 'Loading...'
                },
                stroke: {
                    width: 2,
                    curve: "straight"
                },
                grid: {
                    row: {
                        colors: ['#f3f3f3', 'transparent'],
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
                        minWidth: 1,
                        align: 'right',
                        show: true,
                        formatter: (val: number, opts?: any): string => {
                            return this.nFormatter(val, 1);
                        }
                    },
                    min: 0,
                    forceNiceScale: true
                },
                tooltip: {
                    x: {
                        show: true, formatter(val: number, opts?: any): string {
                            return moment(val).format("ddd DD MMMM");
                        }
                    },
                    y: {
                        formatter: (val: number, opts?: any): string => {
                            return this.groupingFormatter(val);
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
                            year: 'yyyy', month: 'MMM \'yy', day: 'dd MMM', hour: 'HH:mm'
                        }
                    },
                    tooltip: {
                        enabled: false
                    }
                },
                fill: {
                    type: "gradient", colors: [color, "transparent"], gradient: {
                        shadeIntensity: 1, opacityFrom: 0.7, opacityTo: 0.9, stops: [0, 100]
                    }
                }
            };
        }

        private nFormatter(num: number, digits: number): string {
            const si = [
                {value: 1, symbol: ""},
                {value: 1E3, symbol: "k"},
                {value: 1E6, symbol: "M"},
                {value: 1E9, symbol: "G"},
                {value: 1E12, symbol: "T"},
                {value: 1E15, symbol: "P"},
                {value: 1E18, symbol: "E"}
            ];
            const rx = /\.0+$|(\.[0-9]*[1-9])0+$/;
            let i;
            for (i = si.length - 1; i > 0; i--) {
                if (num >= si[i].value) {
                    break;
                }
            }
            return (num / si[i].value).toFixed(digits).replace(rx, "$1") + si[i].symbol;
        }

        private groupingFormatter(val: number): string {
            return val.toLocaleString(undefined, {useGrouping: true});
        }

    }

    interface Point {
        x: number;
        y: number;
    }

</script>
