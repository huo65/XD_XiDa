<template>
  <van-pull-refresh
      v-model="refreshLoading"
      success-text="刷新成功"
      @refresh="likeReload">
    <!--    todo 用户搜索-->
    <van-list
        v-model:loading="listLoading"
        :finished="listFinished"
        offset="0"
        @load="likeLoad"
        style="margin: 15px;height: 100%"
    >
      <template #finished>
        <span v-if="likeList && likeList.length!==0">没有更多了</span>
      </template>
      <template #loading>
        <van-loading vertical>
          <template #icon>
            <van-icon name="star-o" size="30"/>
          </template>
          加载中...
        </van-loading>
      </template>
      <user-like-list :like-list="likeList"/>
    </van-list>
    <van-back-top right="20px" bottom="60px"/>
    <van-empty
        v-if="(!likeList || likeList.length===0) && listLoading===false"
        image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
        image-size="80"
        description="您还没有收到过赞"
    />
  </van-pull-refresh>
</template>

<script setup>
import {ref} from "vue";
import myAxios from "../../plugins/my-axios.js";

const loading = ref(true)
const likeList = ref([])

const refreshLoading = ref(false)
const listLoading = ref(false)
const listFinished = ref(false)
const likeCurrentPage = ref(0)

const getLikes = async (currentPage) => {
  let res = await myAxios.get("/message/like", {
    params: {
      currentPage: currentPage
    }
  });
  if (res?.data.code === 0) {
    if (res.data.data.records.length > 0) {
      res.data.data.records.forEach(item => likeList.value.push(item))
    } else {
      listFinished.value = true
    }
    listLoading.value = false
  }
}
const likeLoad = async () => {
  likeCurrentPage.value++
  await getLikes(likeCurrentPage.value)
}
const likeReload = async () => {
  likeCurrentPage.value = 1
  likeList.value = []
  listFinished.value = false
  await getLikes(likeCurrentPage.value)
  refreshLoading.value = false
  listLoading.value = false
}
</script>

<style scoped>

</style>
