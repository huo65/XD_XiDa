<template>
    <van-notice-bar
        color="#1989fa"
        background="#ecf9ff"
        left-icon="volume-o"
        style="margin-bottom: 10px"
        :text="noticeText"
    />
    <div style="position: relative;height: 100%;width: 100%">
        <van-swipe class="my-swipe" :autoplay="3000" indicator-color="white" lazy-render
                   style="width: 90%;height: 150px;margin: 0 auto">
            <van-swipe-item v-for="image in images" :key="image">
                <img :src="image" style="width: 100%;height: 150px" />
            </van-swipe-item>
        </van-swipe>
        <van-tabs v-model:active="active" @change="tabsChange">
            <van-tab title="👑 匹配用户">
                <van-pull-refresh
                    v-model="refreshLoading"
                    success-text="刷新成功"
                    @refresh="onRefresh">
                    <van-search v-model="userSearch" placeholder="请输入搜索关键词" shape="round"
                                @search="searchUser" />
                    <van-list
                        v-model:loading="listLoading"
                        :finished="listFinished"
                        offset="0"
                        @load="onLoad"
                        style="margin: 15px"
                    >
                        <template #finished>
                            <span v-if="!searching">没有更多了</span>
                        </template>
                        <UserCardList :user-list="userList" />
                    </van-list>
                    <van-back-top right="20px" bottom="60px" />
                    <van-empty v-if="(!userList ||　userList.length===0) && !listLoading && !searching" image="search"
                               description="暂无用户" />
                </van-pull-refresh>
            </van-tab>
            <van-tab title="📚 热门博文">
                <van-pull-refresh
                    v-model="refreshLoading"
                    success-text="刷新成功"
                    @refresh="blogRefresh">
                    <van-search v-model="blogSearch" placeholder="请输入搜索关键词" shape="round"
                                @search="searchBlog" />
                    <van-list
                        v-model:loading="listLoading"
                        :finished="blogListFinished"
                        offset="0"
                        @load="blogLoad"
                        style="margin: 15px"
                    >
                        <template #finished>
                            <span v-if="!searching">没有更多了</span>
                        </template>
                        <blog-card-list :blog-list="blogList" />
                    </van-list>
                    <van-back-top right="20px" bottom="60px" />
                    <van-empty v-if="(!blogList ||　blogList.length===0) && !listLoading &&!searching" image="search"
                               description="暂无博文" />
                </van-pull-refresh>
            </van-tab>
            <van-loading vertical v-if="searching">
                <template #icon>
                    <van-icon name="star-o" size="30" />
                </template>
                加载中...
            </van-loading>
        </van-tabs>
    </div>
</template>
<script setup>
import { onMounted, ref } from "vue";
import myAxios from "../plugins/my-axios.js";
import { showFailToast, showSuccessToast } from "vant";
import UserCardList from "../components/UserCardList.vue";
import BlogCardList from "../components/BlogCardList.vue";

const searching = ref(false);
const listLoading = ref(false);
const listFinished = ref(false);
const userList = ref([]);
const refreshLoading = ref(false);
const currentPage = ref(0);
const userSearch = ref("");
const active = ref(0);
const blogList = ref([]);
const blogListFinished = ref(false);
const blogCurrentPage = ref(0);
const noticeText = ref("富强、民主、文明、和谐；自由、平等、公正、法治；爱国、敬业、诚信、友善。");
const images = ref([
    "http://sece3gfbx.hb-bkt.clouddn.com/LOGO.png",
    "http://sece3gfbx.hb-bkt.clouddn.com/u%3D3974458106%2C3461483911%26fm%3D253%26fmt%3Dauto%26app%3D138%26f%3DJPEG.webp",
    "http://sece3gfbx.hb-bkt.clouddn.com/cae82c3e-d8cd-466e-b8c0-fc6c15f779ca_proc.jpg",
]);

onMounted(async () => {
    await getNotice();
    await getSwiper();
    if (sessionStorage.getItem("tabIndex") === "1") {
        active.value = 1;
    }
});
const tabsChange = (index) => {
    sessionStorage.setItem("tabIndex", index);
};
const getNotice = async () => {
    let res = await myAxios.get("/config/notice");
    if (res.data.data !== null) {
        noticeText.value = res.data.data;
    }
};
const getSwiper = async () => {
    let res = await myAxios.get("/config/swiper");
    if (res.data.data !== null) {
        images.value = res.data.data;
    }
};
const blogLoad = async () => {
    blogCurrentPage.value++;
    await getBlogList(blogCurrentPage.value);
};

const getBlogList = async (currentPage) => {
    let res = await myAxios.get("/blog/list", {
        params: {
            currentPage: currentPage,
            title: blogSearch.value,
        },
    });
    if (res?.data.code === 0) {
        if (res.data.data.records.length > 0) {
            res.data.data.records.forEach(item => blogList.value.push(item));
        } else {
            blogListFinished.value = true;
        }
        listLoading.value = false;
    }
};

async function getUserList(currentPage) {
    const userListData = await myAxios.get("/user/match", {
        params: {
            currentPage: currentPage,
            username: userSearch.value,
        },
    });
    if (userListData?.data.code === 0) {
    } else {
        showFailToast("加载失败" + (res.data.description ? `,${res.data.description}` : ""));
    }
    if (userListData?.data.data.records.length === 0) {
        listFinished.value = true;
        return;
    }
    if (userListData?.data.data.records) {
        userListData.data.data.records.forEach(user => {
            if (user.tags) {
                user.tags = JSON.parse(user.tags);
            }
        });
        for (let i = 0; i < userListData.data.data.records.length; i++) {
            userList.value.push(userListData.data.data.records[i]);
        }
    }
}

const onLoad = async () => {
    currentPage.value++;
    await getUserList(currentPage.value);
    listLoading.value = false;
};
const onRefresh = async () => {
    currentPage.value = 1;
    userList.value = [];
    listFinished.value = false;
    await getUserList(currentPage.value);
    refreshLoading.value = false;
    listLoading.value = false;
};

const blogRefresh = async () => {
    blogCurrentPage.value = 1;
    blogList.value = [];
    blogListFinished.value = false;
    await getBlogList(blogCurrentPage.value);
    refreshLoading.value = false;
    listLoading.value = false;
};
const searchUser = async () => {
    searching.value = true;
    userList.value = [];
    currentPage.value = 1;
    await getUserList(currentPage.value);
    searching.value = false;
};
const blogSearch = ref("");
const searchBlog = async () => {
    searching.value = true;
    blogList.value = [];
    blogCurrentPage.value = 1;
    await getBlogList(blogCurrentPage.value);
    searching.value = false;
};
</script>

<style scoped>
.my-swipe .van-swipe-item {
    color: #fff;
    font-size: 20px;
    line-height: 150px;
    text-align: center;
    background-color: #39a9ed;
}

.my-swipe {
    margin: 15px;
    border-radius: 3%;
}


</style>
