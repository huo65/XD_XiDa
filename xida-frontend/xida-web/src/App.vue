<script setup>
import {computed} from "vue";
import {useRoute} from "vue-router";

let route = useRoute();
const default_layout = "default"
const isRouterAlive = true
const layout = computed(() => {
    return (route.meta.layout || default_layout) + '-layout'
})
</script>
<template>
    <div id="app">
        <component :is="layout">
            <router-view v-if="isRouterAlive"/>
        </component>
    </div>
</template>

<style>
/*@font-face {*/
/*    font-family: 'pingfang';*/
/*    src: url('/public/font/PingFang.woff2') format('woff2');*/
/*}*/

#app {
    font-family: "PingFang SC", sans-serif;
}
</style>
