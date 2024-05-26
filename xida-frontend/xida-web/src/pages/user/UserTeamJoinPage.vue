<template>
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch"/>
    <van-pull-refresh
        v-model="refreshLoading"
        success-text="刷新成功"
        @refresh="onRefresh"
    >
        <van-list
            v-model:loading="listLoading"
            :finished="listFinished"
            offset="0"
            finished-text="没有更多了"
            @load="onLoad"
        >
            <template #loading>
                <van-skeleton>
                    <template #template>
                        <div :style="{ display: 'flex', width: '100%' }">
                            <van-skeleton-image/>
                            <div :style="{ flex: 1, marginLeft: '16px' }">
                                <van-skeleton-paragraph row-width="60%"/>
                                <van-skeleton-paragraph/>
                                <van-skeleton-paragraph/>
                                <van-skeleton-paragraph/>
                            </div>
                        </div>
                    </template>
                </van-skeleton>
            </template>
            <TeamCardList :team-list="teamList" @refresh="onRefresh"/>
        </van-list>
    </van-pull-refresh>
    <van-empty v-if="teamList?.length<1 && !listLoading" description="数据为空"/>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import TeamCardList from "../../components/TeamCardList.vue";
import {ref} from "vue";
import myAxios from "../../plugins/my-axios.js";
import {showFailToast} from "vant";

let router = useRouter();
const searchText = ref("")

const refreshLoading = ref(false)
const listLoading = ref(false)
const listFinished = ref(false)
const currentPage = ref(0)
const doAddTeam = () => {
    router.push("/team/add")
}
const teamList = ref([])

/**
 * 搜索队伍
 * @param val
 * @returns {Promise<void>}
 */
const onSearch = async (val) => {
    teamList.value = []
    currentPage.value = 1
    await listTeams(currentPage.value, val)
}


async function listTeams(currentPage, val = '') {
    listLoading.value = true
    const res = await myAxios.get("/team/list/my/join?currentPage=" + currentPage + "&searchText=" + val)
    if (res?.data.code === 0) {
        if (res.data.data.records.length === 0) {
            listFinished.value = true
            return
        } else {
            res.data.data.records.forEach(team => teamList.value.push(team))
        }
    } else {
        showFailToast("队伍加载失败，请稍后重试")
    }
    listLoading.value = false
}

const onLoad = async () => {
    currentPage.value++
    await listTeams(currentPage.value)
    // onLoading.value=false
}

const onRefresh = async () => {
    teamList.value = []
    listFinished.value = false
    currentPage.value = 1
    await listTeams(currentPage.value)
    refreshLoading.value = false
}
</script>

<style scoped>

</style>
