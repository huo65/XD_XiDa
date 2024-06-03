<template>
  <el-menu
      :default-active="activeP"
      style="height: 100%"
      router
  >
    <el-menu-item index="dashboard" @click="saveNavState('dashboard')">
      <el-icon>
        <location/>
      </el-icon>
      <span>工作台</span>
    </el-menu-item>
    <el-menu-item index="user" @click="saveNavState('user')">
      <el-icon>
        <User/>
      </el-icon>
      <span>用户管理</span>
    </el-menu-item>
  </el-menu>
</template>

<script lang="ts" setup>
import {Location, User} from "@element-plus/icons-vue";
import {onMounted, ref} from "vue";

const activeP = ref("")

onMounted(() => {
  activeP.value = String(sessionStorage.getItem('activePath'))
})
const saveNavState = (activePath: string) => {
  window.sessionStorage.setItem('activePath', activePath)
  activeP.value = activePath
}
</script>

<style scoped>
</style>
