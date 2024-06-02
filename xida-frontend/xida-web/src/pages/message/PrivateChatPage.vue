<script setup lang="ts">
import { onMounted, ref } from "vue";
import myAxios from "../../plugins/my-axios";
import defaultImg from "../../../public/defalutTeamImg.jpg";
import { useRouter } from "vue-router";

let router = useRouter();
const userList = ref([]);
onMounted(async () => {
    let res = await myAxios.get("/chat/private");
    if (res?.data.code === 0) {
        userList.value = res.data.data;
    }
});
const toPrivateChat = (user) => {
    myAxios.put("/chat/private/read?remoteId=" + user.userId);
    router.push({
        path: "/chat",
        query: {
            id: user.userId,
            username: user.username,
            userType: 1,
        },
    });
};
</script>

<template>
    <van-cell-group v-for="user in userList">
        <van-cell @click="toPrivateChat(user)">
            <template #title>
                <span class="cell-span">{{ user.username }}</span>
            </template>
            <template #label>
                <span class="cell-span">{{ user.lastMessage }}</span>
            </template>
            <template #icon>
                <van-badge v-if="user.unReadNum>0" :content="user.unReadNum" max="99">
                    <van-image :src="user.avatarUrl || defaultImg" round width="50" height="50"
                               style="margin-left: 10px" />
                </van-badge>
                <van-badge v-else>
                    <van-image :src="user.avatarUrl || defaultImg" round width="50" height="50"
                               style="margin-left: 10px" />
                </van-badge>
            </template>
            <template #value>
                {{ user.lastMessageDate }}
            </template>
        </van-cell>
    </van-cell-group>
    <van-empty
        v-if="!userList || userList.length===0"
        image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
        image-size="80"
        description="暂无私聊对象"
    />
</template>

<style scoped>

</style>
