<template>
    <div class="home">
        <canvas id="line" v-show="chartData != null"></canvas>
        <button @click="createChart()">Randomize</button>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {Line, mixins} from 'vue-chartjs'
    import {Chart} from 'chart.js'

    @Component({
        components: {LineChat: Line},
    })
    export default class Home extends Vue {
        private chartData: Chart.ChartData = {};

        public mounted() {
            this.createChart();
        }

        public createChart() {
            this.fillData()
            const canvas = document.getElementById('line') as HTMLCanvasElement
            const options = {
                type: 'line',
                data: this.chartData
            }

            new Chart(canvas, options);
        }

        public fillData() {
            this.chartData = Object.assign({}, this.chartData, {
                labels: [this.getRandomInt(), this.getRandomInt()],
                datasets: [
                    {
                        label: 'Data One',
                        backgroundColor: '#f87979',
                        data: [this.getRandomInt(), this.getRandomInt()]
                    }, {
                        label: 'Data One',
                        backgroundColor: '#f87979',
                        data: [this.getRandomInt(), this.getRandomInt()]
                    }
                ]
            })
        }

        public getRandomInt() {
            return Math.floor(Math.random() * (50 - 5 + 1)) + 5
        }
    }
</script>
