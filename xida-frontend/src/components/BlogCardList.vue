<template>
    <van-cell-group>
        <van-cell
            v-for="blog in props.blogList"
            :title="blog.title"
            @click="toBlog(blog.id)">
            <template #right-icon>
                <van-image v-if="blog.coverImage" :src="blog.coverImage" width="88px" height="88px"/>
            </template>
            <template #title>
                <van-text-ellipsis style="margin-left: 20px" :content="blog.title" rows="1"/>
            </template>
            <template #value>
                <div style="margin-top: 60px;margin-right: 10px">
                    <van-icon name="envelop-o" size="14" style="margin-right: 5px">
                        <span style="margin-left: 2px">{{ blog.commentsNum }}</span>
                    </van-icon>
                    <van-icon name="good-job-o" v-if="!blog.isLike" size="14">
                        <span style="margin-left: 2px">{{ blog.likedNum }}</span>
                    </van-icon>
                    <van-icon name="good-job-o" v-else color="red" size="14"
                              style="margin-right: 2px">
                        <span style="margin-left: 2px">{{ blog.likedNum }}</span>
                    </van-icon>
                </div>
            </template>
            <template #label>
                <span style="margin-left: 20px">{{ blog.createTime }}</span>
            </template>
        </van-cell>
    </van-cell-group>
</template>

<script setup lang="ts">
import {BlogType} from "../models/blog.js";
import {useRouter} from "vue-router";
import {getCurrentUser} from "../services/user.ts";
import {showFailToast} from "vant";

interface BlogCardListProps {
    blogList: BlogType[]
}

let props = defineProps<BlogCardListProps>();
let router = useRouter();
const toBlog = async (blogId) => {
    let currentUser = await getCurrentUser();
    if (currentUser) {
        await router.push({
            path: '/blog',
            query: {
                id: blogId
            }
        })
    } else {
        showFailToast("未登录")
        await router.replace("/user/login")
    }

}
</script>

<style scoped>
:deep(.van-cell__label) {
    margin-top: 38px;
}

:deep(.van-cell) {
    padding-left: 0;
    padding-right: 0;
}
</style>
