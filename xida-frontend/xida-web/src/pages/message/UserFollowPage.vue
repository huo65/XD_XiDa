<template>
  <van-pull-refresh
      v-model="refreshLoading"
      success-text="刷新成功"
      @refresh="followReload">
    <!--    todo 用户搜索-->
    <van-list
        v-model:loading="listLoading"
        :finished="listFinished"
        offset="0"
        @load="followLoad"
        style="margin: 15px;height: 100%"
    >
      <template #finished>
        <span v-if="userList && userList.length!==0">没有更多了</span>
      </template>
      <template #loading>
        <van-loading vertical>
          <template #icon>
            <van-icon name="star-o" size="30"/>
          </template>
          加载中...
        </van-loading>
      </template>
      <user-card-list :user-list="userList"/>
    </van-list>
    <van-back-top right="20px" bottom="60px"/>
    <van-empty
        v-if="(!userList || userList.length===0) && listLoading===false"
        image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
        image-size="80"
        description="您暂无关注的用户"
    />
  </van-pull-refresh>
</template>

<script setup>
import UserCardList from "../../components/UserCardList.vue";
import {ref} from "vue";
import myAxios from "../../plugins/my-axios.js";

const loading = ref(true)
const userList = ref([])

const refreshLoading = ref(false)
const listLoading = ref(false)
const listFinished = ref(false)
const userFollowCurrentPage = ref(0)

const getUserFollow = async (currentPage) => {
  let res = await myAxios.get("/follow/my", {
    params: {
      currentPage: currentPage
    }
  });
  if (res?.data.code === 0) {
    if (res.data.data.records.length > 0) {
      res.data.data.records.forEach((user) => {
        if (user.tags) {
          user.tags = JSON.parse(user.tags)
        }
      })
      res.data.data.records.forEach(item => userList.value.push(item))
    } else {
      listFinished.value = true
    }
    listLoading.value = false
  }
}
const followLoad = async () => {
  userFollowCurrentPage.value++
  await getUserFollow(userFollowCurrentPage.value)
}
const followReload = async () => {
  userFollowCurrentPage.value = 1
  userList.value = []
  listFinished.value = false
  await getUserFollow(userFollowCurrentPage.value)
  refreshLoading.value = false
  listLoading.value = false
}
</script>

<style scoped>

</style>
