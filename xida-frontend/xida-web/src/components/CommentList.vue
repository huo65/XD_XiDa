<template>
    <van-cell-group v-for="comment in props.commentList" :border="false" style="margin-left: 5px;margin-right: 5px">
        <van-cell center :border="false" :title="comment.commentUser.username" :label="comment.createTime">
            <template #icon>
                <van-image
                    round
                    width="40"
                    height="40"
                    style="margin-right: 10px"
                    :src="comment.commentUser.avatarUrl"
                />
            </template>
            <template #value>
                <van-icon v-if="!comment.isLiked" name="good-job-o" size="15" @click="likeComment(comment)">
                    {{ comment.likedNum }}
                </van-icon>
                <van-icon v-else name="good-job-o" color="red" size="15" @click="likeComment(comment)">
                    {{ comment.likedNum }}
                </van-icon>
              <van-icon v-if="String(currentUser.id)==comment.commentUser.id || currentUser.role===1" name="delete-o"
                        size="15" style="margin-left: 10px" @click="deleteComment(comment.id)"/>
            </template>
        </van-cell>
        <div style="display: block;width: 80%;word-wrap: break-word;margin-left: 55px">
            {{ comment.content }}
        </div>
    </van-cell-group>

</template>

<script setup lang="ts">
import {CommentType} from "../models/comment.d.ts";
import myAxios from "../plugins/my-axios.js";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../services/user";
import {showConfirmDialog, showFailToast} from "vant";

let emits = defineEmits(['refresh']);
interface BlogCommentsProps {
    commentList: CommentType[]
}
const currentUser=ref()
let props = defineProps<BlogCommentsProps>();
onMounted(async ()=>{
  currentUser.value = await getCurrentUser()
})
const likeComment = async (comment) => {
    let res = await myAxios.put("/comments/like/" + comment.id);
    if (res?.data.code === 0) {
        let res_ = await myAxios.get("/comments/" + comment.id);
        if (res_?.data.code === 0) {
            comment.likedNum = res_.data.data.likedNum
            comment.isLiked = res_.data.data.isLiked
        }
    }
}
const deleteComment=async (id)=>{
  showConfirmDialog({
    title: '确定要删除评论吗',
    message:
        '此操作无法撤回',
  })
      .then(async () => {
        let res = await myAxios.delete("/comments/"+id);
        if (res?.data.code===0){
          emits("refresh")
        }else {
          showFailToast("删除失败" + (res.data.description ? `,${res.data.description}` : ''))
        }
      })
      .catch(() => {
      });
}
</script>

<style scoped>

</style>
