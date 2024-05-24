<template>
  <div style="position: relative;height: 100%;width: 100%">
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
        <UserCardList :user-list="userList"/>
      </van-list>
    </van-pull-refresh>
    <van-back-top right="20px" bottom="60px"/>
    <van-empty v-if="(!userList ||　userList.length===0) && listLoading===false" image="search"
               description="暂无符合要求的用户"/>
  </div>
</template>

<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/my-axios.js";
import {showFailToast, showSuccessToast, showToast} from "vant";
import qs from 'qs';
import UserCardList from "../components/UserCardList.vue";

let route = useRoute();
const {tags} = route.query
const userList = ref([])
const currentPage = ref(0)
const refreshLoading = ref(false)
const listLoading = ref(false)
const listFinished = ref(false)

async function getSearchResult(currentPage) {
  const res = await myAxios.get("/user/search/tags?currentPage=" + currentPage, {
    params: {
      tagNameList: tags
    },
    paramsSerializer: params => {
      return qs.stringify(params, {indices: false})
    }
  })
  if (res.data.code === 0) {
    if (res.data.data.records.length !== 0) {
      res.data.data.records.forEach(user => {
        if (user.tags) {
          user.tags = JSON.parse(user.tags)
        }
      })
      res.data.data.records.forEach((item) => userList.value.push(item))
    } else {
      listFinished.value = true
    }
  } else {
    showFailToast("搜索失败" + (res.data.description ? `,${res.data.description}` : ''))
    listFinished.value = true
  }
}

const onLoad = async () => {
  currentPage.value++
  await getSearchResult(currentPage.value)
  listLoading.value = false;
}

const onRefresh = async () => {
  currentPage.value = 1
  userList.value = []
  await getSearchResult(currentPage.value)
  refreshLoading.value = false
  listLoading.value = false;
}
</script>

<style scoped>

</style>
