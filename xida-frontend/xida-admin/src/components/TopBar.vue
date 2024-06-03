<script setup lang="ts">
import { onMounted, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import myAxios from "../plugins/my-axios.ts";

const activeP = ref("");
onMounted(async () => {
    activeP.value = String(sessionStorage.getItem("activePath"));
    const res = await myAxios.get("/user/admin/current");
    if (res.data.code !== 0) {
        ElMessage.error(res.data.description);
        sessionStorage.clear();
    }
    avatarUrl.value = res.data.data.avatarUrl;

});
const saveNavState = (activePath: string) => {
    window.sessionStorage.setItem("activePath", activePath);
    activeP.value = activePath;
};
const avatarUrl = ref("");
let router = useRouter();
const logout = () => {
    ElMessageBox.confirm(
        "确认退出",
        {
            confirmButtonText: "OK",
            cancelButtonText: "取消",
            type: "warning",
        },
    )
        .then(() => {
            sessionStorage.clear();
            router.push("/login");
            ElMessage({
                type: "success",
                message: "退出成功",
            });
        });
};
</script>

<template>
    <el-menu
        :default-active="activeP"
        class="el-menu-demo"
        mode="horizontal"
        :ellipsis="false"
        router
    >
        <el-menu-item index="0">
            <img
                style="width: 40px"
                src="/LOGO.png"
                alt="Element logo"
            />
            <h3>
                嘻搭管理系统
            </h3>
        </el-menu-item>
        <div class="flex-grow" />
        <el-menu-item index="dashboard" @click="saveNavState('dashboard')">首页</el-menu-item>
        <el-menu-item index="config" @click="saveNavState('config')">配置</el-menu-item>
        <el-menu-item index="user" @click="saveNavState('user')">用户管理</el-menu-item>
        <el-sub-menu>
            <template #title>
                <el-avatar :src="avatarUrl"></el-avatar>
            </template>
            <el-menu-item @click="logout">退出系统</el-menu-item>
        </el-sub-menu>
    </el-menu>
</template>

<style scoped>
.flex-grow {
    flex-grow: 1;
}
</style>
