<template>
    <van-cell-group v-for="comment in props.commentList" :border="false">
        <van-cell center :border="false" :title="comment.commentUser.username"
                  :label="`${comment.createTime} 评论帖子`">
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
                <van-icon v-if="String(currentUser.id)===comment.commentUser.id || currentUser.role===1" name="delete-o"
                          size="15" style="margin-left: 10px" @click="deleteComment(comment.id)"/>
            </template>
        </van-cell>
        <div style="display: block;width: 80%;word-wrap: break-word;margin-left: 10px">
            {{ comment.content }}
        </div>
        <div>
            <van-space style="background: #f3f2f5;width: 100%;margin: 0" align="center"
                       @click="toBlog(comment.blog.id)">
                <van-image v-if="comment.blog.coverImage" :src="comment.blog.coverImage" width="50" height="50"
                           style="margin-left: 10px"/>
                <van-cell :title="comment.blog.author.username" :label="comment.blog.title"
                          style="background: #f3f2f5;margin: 0">
                </van-cell>
            </van-space>
        </div>
    </van-cell-group>

</template>

<script setup lang="ts">
import {CommentType} from "../models/comment.d.ts";
import myAxios from "../plugins/my-axios.js";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../services/user";
import {showConfirmDialog, showFailToast} from "vant";
import {useRouter} from "vue-router";

let emits = defineEmits(['refresh']);

interface BlogCommentsProps {
    commentList: CommentType[]
}

const currentUser = ref()
let props = defineProps<BlogCommentsProps>();
onMounted(async () => {
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
const deleteComment = async (id) => {
    showConfirmDialog({
        title: '确定要删除评论吗',
        message:
            '此操作无法撤回',
    })
        .then(async () => {
            let res = await myAxios.delete("/comments/" + id);
            if (res?.data.code === 0) {
                emits("refresh")
            } else {
                showFailToast("删除失败" + (res.data.description ? `,${res.data.description}` : ''))
            }
        })
        .catch(() => {
        });
}
let router = useRouter();
const toBlog = (blogId) => {
    router.push({
        path: "/blog",
        query: {
            id: blogId
        }
    })
}
</script>

<style scoped>

</style>
