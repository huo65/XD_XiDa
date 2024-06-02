<template>
    <van-sticky>
        <van-nav-bar
                :title="title"
                left-arrow
                @click-left="onClickLeft"
                @click-right="onClickRight"
        >
            <template #right>
                <van-icon name="search" size="18"/>
            </template>
        </van-nav-bar>
    </van-sticky>
    <div class="content">
        <router-view/>
    </div>
    <van-tabbar route v-model="active">
        <van-tabbar-item to="/" icon="home-o" name="index">主页</van-tabbar-item>
        <van-tabbar-item to="/team" icon="flag-o" name="team">队伍</van-tabbar-item>
        <van-tabbar-item replace class="van-tabbar-addBlog" @click="checkLogin('/blog/edit',2)">
            <div class="center-wrap">
                <div class="bgc-wrap">
                    <van-icon name="plus" size="27" class="icon"/>
                </div>
            </div>
        </van-tabbar-item>
        <van-tabbar-item v-if="hasMessage" icon="smile-comment-o" name="message" class="message"
                         @click="checkLogin('/message',3)" dot>
            消息
        </van-tabbar-item>
        <van-tabbar-item v-else icon="smile-comment-o" name="message" class="message" @click="checkLogin('/message',3)">
            消息
        </van-tabbar-item>
        <van-tabbar-item to="/user" icon="user-o" name="user">个人</van-tabbar-item>
    </van-tabbar>
</template>
<script setup>
import {showConfirmDialog, showToast} from "vant";
import {useRouter} from "vue-router";
import routes from "../config/routes.ts";
import {ref} from "vue";
import {getCurrentUser} from "../services/user.ts";
import myAxios from "../plugins/my-axios.js";

const hasMessage = ref(false)
let router = useRouter();
const DEFAULT_TITLE = "嘻搭XIDA"
const title = ref(DEFAULT_TITLE)
const active = ref(0)
router.beforeEach(async (to) => {
    const toPath = to.path
    const route = routes.find((routes) => {
        return routes.path === toPath
    })
    document.title = "嘻搭XIDA"
    title.value = route?.title ?? DEFAULT_TITLE
    if (to.path !== '/user/login') {
        let res = await myAxios.get("/message");
        if (res?.data.code === 0) {
            if (res.data.data) {
                hasMessage.value = true
            } else {
                hasMessage.value = false
            }
        }
    }
})
const onClickLeft = () => {
    router.back()
};
const onClickRight = () => {
    router.push("/search")
};

const checkLogin = async (to, index) => {
    let user = await getCurrentUser();
    if (!user) {
        showConfirmDialog({
            message:
                "该功能需要登陆后使用,是否登录",
            confirmButtonText: "去登录"
        })
            .then(() => {
                router.replace("/user/login")
            })
            .catch(() => {
            });
    } else {
        await router.push(to)
        if (active.value === 'message') {
            let message = document.getElementsByClassName("message")
            message.item(0).style.color = '#1989fa'
        }
    }
}
</script>

<style scoped>
.content {
    padding-bottom: 70px;
}

.center-wrap {
    width: 80px;
    height: 80px;
    margin-top: -20px;
    z-index: 100000;
    background-color: white;
    border-radius: 50% 50% 0 0;
    position: relative;
    box-shadow: 0 0 10px rgba(0, 0, 0, .4);
}

.bgc-wrap {
    width: 50px;
    height: 50px;
    border-radius: 50% 50% 50% 50%;
    background-color: #3c89fc;
    position: absolute;
    left: 15px;
    top: 10px;
}

.icon {
    margin-left: -3px;
    margin-top: -3px;
    color: white;
}

[class*=van-hairline]::after {
    border: none !important
}
</style>
