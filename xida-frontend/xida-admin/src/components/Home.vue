<script setup lang="ts">
import TopBar from "./TopBar.vue";
import SideBar from "./SideBar.vue";

</script>

<template>
  <el-container class="home-container">
    <el-header>
      <TopBar/>
    </el-header>
    <el-container>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.el-main{
  background-color: #eaedf1;
}
.home-container{
  height: 100%;
}
</style>
