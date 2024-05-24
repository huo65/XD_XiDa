<template>
    <van-sticky>
        <van-nav-bar
                title="添加博文"
                left-arrow
                @click-left="onClickLeft"
                @click-right="onClickRight"
        >
            <template #right>
                发布
            </template>
        </van-nav-bar>
    </van-sticky>
    <div style="padding: 15px">
        <van-uploader v-model="fileList" preview-size="100" multiple :max-count="5" :max-size="10 * 1024 * 1024"
                      @oversize="overSize" :preview-full-image="false">
            <template #default>
                <div class="updateArea">
                    <van-icon name="plus" class="updateIcon" size="20px"/>
                </div>
            </template>
        </van-uploader>
        <van-field v-model="title"
                   placeholder="填写标题"
                   :rules="[{ required: true, message: '请输入用户名' }]"/>
        <van-field
            v-model="content"
            rows="1"
            autosize
            type="textarea"
            placeholder="添加正文"
            :rules="[{ required: true, message: '请输入用户名' }]"
        />
    </div>
    <van-overlay :show="addingOverlay">
        <div class="wrapper">
            <van-loading vertical>
                <template #icon>
                    <van-icon name="star-o" size="30"/>
                </template>
                添加中...
            </van-loading>
        </div>
    </van-overlay>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {showFailToast, showSuccessToast} from "vant";
import {useRoute, useRouter} from "vue-router";
import {getCurrentUser} from "../../services/user.ts";
import myAxios from "../../plugins/my-axios.js";

const addingOverlay = ref(false)
const fileList = ref([])
const title = ref("")
const content = ref("")
const router = useRouter()
const user = ref()
const blogId = ref()
const onClickLeft = () => {
    router.push("/")
};
const onClickRight = async () => {
    if (title.value === '') {
        showFailToast("请填写标题")
    }
    if (content.value === '') {
        showFailToast("请填写正文")
    }
    addingOverlay.value = true
    if (!blogId.value) {
        let formData = new FormData();
        for (let i = 0; i < fileList.value.length; i++) {
            formData.append("images", fileList.value[i].file)
        }
        formData.append("title", title.value)
        formData.append("content", content.value)
        let res = await myAxios.post("/blog/add", formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
        if (res?.data.code === 0) {
            addingOverlay.value = false
            showSuccessToast("添加成功")
            await router.replace("/")
        } else {
            addingOverlay.value = false
            showFailToast("添加失败" + (res.data.description ? `,${res.data.description}` : ''))
        }
    } else {
        let formData = new FormData();
        formData.append("id", blogId.value)
        const imgStr = []
        for (let i = 0; i < fileList.value.length; i++) {
            if (fileList.value[i].url) {
                imgStr.push(fileList.value[i].url)
            } else {
                formData.append("images", fileList.value[i].file)
            }
        }
        let finalImgStr = imgStr.join(",");
        formData.append("imgStr", finalImgStr)
        formData.append("title", title.value)
        formData.append("content", content.value)
        let res = await myAxios.put("/blog/update", formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
        if (res?.data.code === 0) {
            addingOverlay.value = false
            showSuccessToast("更新成功")
            await router.replace("/blog?id=" + blogId.value)
        } else {
            addingOverlay.value = false
            showFailToast("更新失败," + (res.data.description ? `,${res.data.description}` : ''))
        }
    }
};
const overSize = () => {
    showFailToast("单个图片不能超过10M")
}
let route = useRoute();
onMounted(async () => {
    if (route.query.id) {
        blogId.value = route.query.id
    }
    if (route.query.images) {
        route.query.images.forEach((item) => {
            const image = {
                url: item,
                isImage: true
            }
            fileList.value.push(image)
        })
    }
    if (route.query.title) {
        title.value = route.query.title
    }
    if (route.query.content) {
        content.value = route.query.content
    }
    let currentUser = await getCurrentUser();
    if (currentUser) {
        user.value = currentUser
    } else {
        showFailToast("未登录")
        await router.replace("/user/login")
    }
})
</script>

<style scoped>
.updateIcon {
    left: 40px;
    top: 40px;
}

.updateArea {
    width: 100px;
    height: 100px;
    background-color: #f7f8fa;
    color: #dcdddf;
}

.wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
}
</style>
