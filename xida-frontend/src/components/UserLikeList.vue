<template>
    <van-cell-group v-for="like in props.likeList" :border="false">
        <div v-if="like.type==0">
            <van-cell center :border="false" :title="like.fromUser.username"
                      :label="`${like.createTime} 赞了我的博文`">
                <template #icon>
                    <van-image
                        round
                        width="40"
                        height="40"
                        style="margin-right: 10px"
                        :src="like.fromUser.avatarUrl"
                    />
                </template>
            </van-cell>
            <div>
                <van-space style="background: #f3f2f5;width: 100%;margin: 0" align="center"
                           @click="toBlog(like.blog.id)">
                    <van-image v-if="like.blog.coverImage" :src="like.blog.coverImage" width="50" height="50"
                               style="margin-left: 10px"/>
                    <van-cell :title="like.blog.author.username" :label="like.blog.title"
                              style="background: #f3f2f5;margin: 0">
                    </van-cell>
                </van-space>
            </div>
        </div>

        <div v-if="like.type==1">
            <van-cell center :border="false" :title="like.fromUser.username"
                      :label="`${like.createTime} 赞了我的评论`">
                <template #icon>
                    <van-image
                        round
                        width="40"
                        height="40"
                        style="margin-right: 10px"
                        :src="like.fromUser.avatarUrl"
                    />
                </template>
            </van-cell>
            <div>
                <van-space style="background: #f3f2f5;width: 100%" @click="toBlog(like.comment?.blogId)">
                    <span style="padding: 16px">{{ like.comment?.content }}</span>
                </van-space>
            </div>
        </div>
    </van-cell-group>

</template>

<script setup lang="ts">
import myAxios from "../plugins/my-axios.js";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../services/user";
import {showConfirmDialog, showFailToast} from "vant";
import {useRouter} from "vue-router";
import {MessageType} from "../models/like";

let emits = defineEmits(['refresh']);

interface LikeListProps {
    likeList: MessageType[]
}

const currentUser = ref()
let props = defineProps<LikeListProps>();
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
