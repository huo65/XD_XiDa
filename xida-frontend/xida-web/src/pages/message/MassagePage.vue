<template>
    <van-grid :border="false">
        <van-grid-item icon="comment" icon-color="#767ffe" text="所有评论" to="/user/comment" />
        <van-grid-item v-if="likeNum===0" icon="good-job" icon-color="#639efc" text="赞" to="/user/like" />
        <van-grid-item v-else icon="good-job" icon-color="#639efc" text="赞" :badge="likeNum" to="/user/like" />
        <van-grid-item icon="friends" icon-color="#a778fc" text="关注" to="/user/follow" />
        <van-grid-item to="/fans">
            <template #icon>
                <van-icon class-prefix="my-icon" name="wodefensi" color="#65cdf2" size="28" />
            </template>
            <template #text>
                <span style="margin-top: 8px;font-size: 14px">粉丝</span>
            </template>
        </van-grid-item>
    </van-grid>
    <van-divider />
    <van-cell to="/user/follow/blog">
        <template #title>
            <span class="cell-span">我的关注</span>
        </template>
        <template #icon>
            <van-badge v-if="blogNum>0" :content="blogNum" max="99">
                <div class="icon_area">
                    <van-icon color="#2a2e31" class-prefix="my-icon" name="guanzhu" size="25"
                              style="margin-left: 12px;margin-top: 13px" />
                </div>
            </van-badge>
            <van-badge v-else>
                <div class="icon_area">
                    <van-icon color="#2a2e31" class-prefix="my-icon" name="guanzhu" size="25"
                              style="margin-left: 12px;margin-top: 13px" />
                </div>
            </van-badge>
        </template>
    </van-cell>
    <van-cell to="/private">
        <template #title>
            <span class="cell-span">私聊</span>
        </template>
        <template #icon>
            <van-badge v-if="unReadPrivateNum>0" :content="unReadPrivateNum" max="99">
                <div class="icon_area">
                    <van-icon name="envelop-o" size="25"
                              style="margin-left: 12px;margin-top: 13px" />
                </div>
            </van-badge>
            <van-badge v-else>
                <div class="icon_area">
                    <van-icon name="envelop-o" size="25"
                              style="margin-left: 12px;margin-top: 13px" />
                </div>
            </van-badge>
        </template>
    </van-cell>
    <van-cell @click="toHallChat">
        <template #title>
            <span class="cell-span">公共聊天室</span>
        </template>
        <template #label>
            <span class="cell-span">XIDA嘻搭官方聊天室</span>
        </template>
        <template #icon>
            <div class="icon_area">
                <van-image src="public/xda.svg" width="25" height="25" round
                           style="margin-left: 12px;margin-top: 12px" />
            </div>
        </template>
        <template #value>
            <van-tag color="#ffe1e1" text-color="#ad0000">官方</van-tag>
        </template>
    </van-cell>
    <van-cell-group v-for="team in teamList">
        <van-cell @click="toChatRoom(team.id,team.name)">
            <template #title>
                <span class="cell-span">{{ `${team.name}` + "聊天室" }}</span>
            </template>
            <template #label>
                <span class="cell-span">{{ team.description }}</span>
            </template>
            <template #icon>
                <van-image :src="team.coverImage || defaultImg" round width="50" height="50"
                           style="margin-left: 10px" />
                <!--                    <van-icon name="volume" color="#2a2e31" class="icon" size="20px"/>-->
            </template>
        </van-cell>
    </van-cell-group>
</template>

<script setup>
import { onMounted, ref } from "vue";
import myAxios from "../../plugins/my-axios.js";
import { useRouter } from "vue-router";
import defaultImg from "../../../public/defalutTeamImg.jpg";

const teamList = ref();
const likeNum = ref(0);
const blogNum = ref(0);
const unReadPrivateNum = ref(0);
onMounted(async () => {
    let res = await myAxios.get("/team/list/my/join/all");
    if (res?.data.code === 0) {
        teamList.value = res.data.data;
    }
    let res2 = await myAxios.get("/message/like/num");
    if (res2.data.code === 0) {
        likeNum.value = Number(res2.data.data);
    }
    let res3 = await myAxios.get("/message/blog/num");
    if (res3?.data.code === 0) {
        blogNum.value = Number(res3.data.data);
    }
    let res4 = await myAxios.get("/chat/private/num");
    if (res4?.data.code === 0) {
        unReadPrivateNum.value = res4.data.data;
    }
});
let router = useRouter();
const toChatRoom = (id, name) => {
    router.push({
        path: "/chat",
        query: {
            teamId: id,
            teamName: name,
            teamType: 2,
        },
    });
};
const toHallChat = () => {
    router.push("/chat");
};
</script>

<style>
.icon_area {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background-color: #ededed;
    position: relative;
    margin-left: 10px;
}

.van-cell {
    padding-left: 5px;
}

.icon {
    position: absolute;
    left: 14px;
    top: 14px
}

.van-divider {
    margin: 2px;
}

.cell-span {
    margin-left: 10px;
}

:root:root {
    --van-grid-item-text-font-size: 14px
}
</style>
