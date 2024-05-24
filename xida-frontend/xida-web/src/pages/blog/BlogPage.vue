<template>
    <van-sticky>
        <van-nav-bar
            title="正文"
            left-arrow
            @click-left="onClickLeft"
            @click-right="showBottom=true"
        >
            <template #right>
                <van-icon class-prefix="my-icon" name="qita" color="#39a9ed"/>
            </template>
        </van-nav-bar>
    </van-sticky>
    <div v-if="images.length>0">
        <div style="width: 100%;height: 10px;background-color: black"/>
        <van-swipe :autoplay="3000" lazy-render style="height: 200px">
            <van-swipe-item v-for="image in images" :key="image">
                <img :src="image" style="height: 200px;width: 100%" alt=""/>
            </van-swipe-item>
        </van-swipe>
        <div style="width: 100%;height: 10px;background-color: black"/>
    </div>


    <van-cell-group inset>
        <van-cell :title="blog.title" title-style="font-size:18px"/>
        <van-cell center style="padding-right: 6px">
            <template #title>
                <span @click="toAuthor(author.id)">{{ author.username }}</span>
            </template>
            <template #icon>
                <van-image :src="author.avatarUrl" width="40" height="40" round style="margin-right: 10px"
                           @click="toAuthor(author.id)"/>
            </template>
            <template #right-icon>
                <div v-if="author.id!==currentUser.id">
                    <van-button v-if="author.isFollow" type="primary" size="small" color="#c1c1c1"
                                @click="followUser(author)">已关注
                    </van-button>
                    <van-button v-else icon="plus" type="primary" size="small" @click="followUser(author)">关注
                    </van-button>
                </div>
            </template>
        </van-cell>
        <van-cell :title="blog.content"/>
    </van-cell-group>

    <van-divider/>
    <van-cell-group inset>
        <!--        todo 排序-->
        <van-cell :title="`评论 ${blog.commentsNum}`"/>
    </van-cell-group>
    <div class="line"></div>

    <div style="padding-bottom: 80px">
        <comment-list :comment-list="commentList" @refresh="refresh"/>
    </div>


    <van-cell-group :border="false">
        <van-field v-model="comment" :autosize="{minHeight: 32}" type="textarea" rows="1" placeholder="评论"
                   :border="false"
                   style="position: fixed;bottom: 0;padding-left: 16px;border-top: 1px solid #C1C1C1;padding-right: 10px">
            <template #right-icon>
                <van-icon class-prefix="my-icon" name="shangchuan" size="30" color="#4387f6" @click="addComment"/>
            </template>
            <template #button>
                <van-icon name="envelop-o" size="15" style="margin-right: 5px">
                    <span style="margin-left: 2px">{{ blog.commentsNum }}</span>
                </van-icon>
                <van-icon name="good-job-o" v-if="!blog.isLike" size="15" @click="likeBlog(blog)">
                    <span style="margin-left: 2px">{{ blog.likedNum }}</span>
                </van-icon>
                <van-icon name="good-job-o" v-else color="red" size="15" @click="likeBlog(blog)"
                          style="margin-right: 2px">
                    <span style="margin-left: 2px">{{ blog.likedNum }}</span>
                </van-icon>
            </template>
        </van-field>
    </van-cell-group>
    <van-popup
        v-if="blog.userId===currentUser.id || currentUser.role===1"
        v-model:show="showBottom"
        round
        position="bottom"
        :style="{ height: '88px' }"
    >
        <van-grid :border="false">
            <van-grid-item @click="copyUrl">
                <template #icon>
                    <van-icon class-prefix="my-icon" name="lianjie" size="24"/>
                </template>
                <template #text>
                    <span class="grid-font">复制链接</span>
                </template>
            </van-grid-item>
            <van-grid-item @click="updateBlog">
                <template #icon>
                    <van-icon class-prefix="my-icon" name="tianxie" size="24"/>
                </template>
                <template #text>
                    <span class="grid-font">编辑</span>
                </template>
            </van-grid-item>
            <van-grid-item @click="deleteBlog">
                <template #icon>
                    <van-icon class-prefix="my-icon" name="shanchu" size="24"/>
                </template>
                <template #text>
                    <span class="grid-font">删除</span>
                </template>
            </van-grid-item>
            <van-grid-item @click="report">
                <template #icon>
                    <van-icon class-prefix="my-icon" name="jubao" size="24"/>
                </template>
                <template #text>
                    <span class="grid-font">举报</span>
                </template>
            </van-grid-item>
        </van-grid>
    </van-popup>

    <van-popup
        v-else
        v-model:show="showBottom"
        round
        position="bottom"
        :style="{ height: '88px' }"
    >
        <van-grid :border="false" :column-num="2">
            <van-grid-item @click="copyUrl">
                <template #icon>
                    <van-icon class-prefix="my-icon" name="lianjie" size="24"/>
                </template>
                <template #text>
                    <span class="grid-font">复制链接</span>
                </template>
            </van-grid-item>
            <van-grid-item @click="report">
                <template #icon>
                    <van-icon class-prefix="my-icon" name="jubao" size="24"/>
                </template>
                <template #text>
                    <span class="grid-font">举报</span>
                </template>
            </van-grid-item>
        </van-grid>
    </van-popup>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import myAxios from "../../plugins/my-axios.js";
import CommentList from "../../components/CommentList.vue";
import {showConfirmDialog, showFailToast, showSuccessToast} from "vant";
import {getCurrentUser} from "../../services/user.ts";

const showBottom = ref(false)
let router = useRouter();
const comment = ref("")
const toAuthor = (id) => {
    router.push({
        path: "/user/detail",
        query: {
            id: id
        }
    })
}
const onClickLeft = () => {
    router.push("/")
};
const images = ref([])
let route = useRoute();
const blog = ref({});
const author = ref({})
const commentList = ref([])
const currentUser = ref({})
const listComments = async () => {
    let id = route.query.id;
    let commentRes = await myAxios.get("/comments?blogId=" + id);
    if (commentRes?.data.code === 0) {
        commentList.value = commentRes.data.data
    }
}
onMounted(async () => {
    currentUser.value = await getCurrentUser();
    let id = route.query.id;
    let res = await myAxios.get("/blog/" + id);
    if (res?.data.code === 0) {
        blog.value = res.data.data
        author.value = res.data.data.author
        if (res.data.data.images) {
            let imgStrs = res.data.data.images.split(",");
            imgStrs.forEach((imgstr) => {
                images.value.push(imgstr)
            })
        }
      await listComments()
    } else {
      showFailToast("加载失败" + (res.data.description ? `,${res.data.description}` : ''))
    }

})
const likeBlog = async (blog) => {
    let res = await myAxios.put("/blog/like/" + blog.id);
    if (res?.data.code === 0) {
        let res_ = await myAxios.get("/blog/" + blog.id);
        if (res_?.data.code === 0) {
            blog.likedNum = res_.data.data.likedNum
            blog.isLike = res_.data.data.isLike
        }
    }
}
const addComment = async () => {
    if (comment.value === "") {
        showFailToast("请输入评论内容")
    } else {
        let res = await myAxios.post("/comments/add", {
            blogId: blog.value.id,
            content: comment.value
        });
        if (res?.data.code === 0) {
            showSuccessToast("添加成功")
        } else {
            showFailToast("添加失败" + (res.data.description ? `,${res.data.description}` : ''))
        }
        await listComments()
        comment.value = ""

        let id = route.query.id;
        let newBlogRes = await myAxios.get("/blog/" + id);
        if (newBlogRes?.data.code === 0) {
            blog.value.commentsNum = newBlogRes.data.data.commentsNum
        }
    }
}
const copyUrl = () => {
    var textArea = document.createElement('textarea')
    document.body.appendChild(textArea)
    textArea.readOnly = 'readonly'
    textArea.style.opacity = '0'
    textArea.value = document.location.href
    textArea.select()
    if (textArea.setSelectionRange)
        textArea.setSelectionRange(0, textArea.value.length)
    else
        textArea.select()
    document.execCommand("copy")
    document.body.removeChild(textArea)
    //指定toast样式，使文字在一行显示
    showSuccessToast({
        message: "已复制到剪切板",
        className: "copyToast"
    })
    showBottom.value = false
}
const report = () => {
    showFailToast("举报成功")
    showBottom.value = false
}
const deleteBlog = async () => {
    showConfirmDialog({
        title: '确定要删除博文吗',
        message:
            '此操作无法撤回',
    })
        .then(async () => {
            let res = await myAxios.delete("/blog/" + blog.value.id);
            if (res?.data.code === 0) {
                await router.replace("/")
                showSuccessToast("删除成功")
            } else {
              showFailToast("删除失败" + (res.data.description ? `,${res.data.description}` : ''))
            }
        })
        .catch(() => {
            showBottom.value = false
        });
}
const updateBlog = () => {
    router.push({
        path: "/blog/edit",
        query: {
            id: blog.value.id,
            images: images.value,
            title: blog.value.title,
            content: blog.value.content
        }
    })
}
const followUser = async (author) => {
    let res = await myAxios.post("/follow/" + author.id);
    if (res?.data.code === 0) {
        let res_ = await myAxios.get("/user/" + author.id);
        if (res_.data.code === 0) {
            author.isFollow = res_.data.data.isFollow
        }
    }
}
const refresh = () => {
    location.reload();
}
</script>

<style>
:deep(.van-field__value) {
    margin-right: 15px;
}

.line {
    width: 100%;
    height: 1px;
    background: #ededed;
    position: relative;
}

.line::after {
    position: absolute;
    top: 0;
    left: 0;
    content: " ";
    display: block;
    width: 10%;
    height: 100%;
    background-color: #39a9ed;
}

.grid-font {
    font-size: 13px;
    color: #949494;
    margin-top: 5px
}

.copyToast {
    width: 100px;
}
</style>
