<template>
  <van-pull-refresh
      v-model="refreshLoading"
      success-text="刷新成功"
      @refresh="commentReload">
    <!--    todo 用户搜索-->
    <van-list
        v-model:loading="listLoading"
        :finished="listFinished"
        offset="0"
        @load="commentLoad"
        style="margin: 15px;height: 100%"
    >
      <template #finished>
        <span v-if="commentList && commentList.length!==0">没有更多了</span>
      </template>
      <template #loading>
        <van-loading vertical>
          <template #icon>
            <van-icon name="star-o" size="30"/>
          </template>
          加载中...
        </van-loading>
      </template>
      <user-comment-list :comment-list="commentList"/>
    </van-list>
    <van-back-top right="20px" bottom="60px"/>
    <van-empty
        v-if="(!commentList || commentList.length===0) && listLoading===false"
        image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
        image-size="80"
        description="您还没有写过评论"
    />
  </van-pull-refresh>
</template>

<script setup>
import {ref} from "vue";
import myAxios from "../../plugins/my-axios.js";

const loading = ref(true)
const commentList = ref([])

const refreshLoading = ref(false)
const listLoading = ref(false)
const listFinished = ref(false)
const commentCurrentPage = ref(0)

const getComments = async (currentPage) => {
  let res = await myAxios.get("/comments/list/my", {
    params: {
      currentPage: currentPage
    }
  });
  if (res?.data.code === 0) {
    if (res.data.data.records.length > 0) {
      res.data.data.records.forEach(item => commentList.value.push(item))
    } else {
      listFinished.value = true
    }
    listLoading.value = false
  }
}
const commentLoad = async () => {
  commentCurrentPage.value++
  await getComments(commentCurrentPage.value)
}
const commentReload = async () => {
  commentCurrentPage.value = 1
  commentList.value = []
  listFinished.value = false
  await getComments(commentCurrentPage.value)
  refreshLoading.value = false
  listLoading.value = false
}
</script>

<style scoped>

</style>
