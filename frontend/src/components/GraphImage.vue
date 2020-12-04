<template>
    <div>
        <img v-if="url != null" :src="url" width="98%" alt=""/>
        <div v-else class="spinner-border" role="status">
            <span class="sr-only">Loading...</span>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';


    @Component
    export default class GraphImage extends Vue {
        @Prop() private url!: string;
    }
</script>

<style scoped>

</style>