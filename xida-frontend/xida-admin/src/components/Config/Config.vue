<script setup lang="ts">

import { ArrowRight } from "@element-plus/icons-vue";
import { onMounted, ref } from "vue";
import myAxios from "../../plugins/my-axios.ts";
import { UploadInstance, UploadProps } from "element-plus";
import { ElMessage } from "element-plus";

const noticeText = ref("");
const input = ref("");
const fileList = ref([]);
const imgs = ref([]);
const isDisplay = ref(true);

onMounted(async () => {
    await getNotice();
    await getSwiper();
    checkSuccessImage();
});

const checkSuccessImage = () => {
    if (fileList.value.length === 0) {
        isDisplay.value = true;
    }
    fileList.value.forEach((item) => {
        if (item.status === "success") {
            isDisplay.value = false;
        }
    });
};

const handleChange = () => {
    checkSuccessImage();
};
const getNotice = async () => {
    let res = await myAxios.get("/config/notice");
    if (res?.data.code === 0) {
        noticeText.value = res.data.data;
    }
};

const getSwiper = async () => {
    let res = await myAxios.get("/config/swiper");
    if (res.data.code === 0) {
        if (res.data.data !== null) {
            let index = 1;
            res.data.data.forEach((item) => {
                const temp = { "name": index, "url": item };
                fileList.value.push(temp);
                index = index + 1;
            });
            fileList.value.forEach((item) => {
                imgs.value.push(item.url);
            });
        }
    }
};
const updateNotice = async () => {
    let res = await myAxios.post("/config/notice", input.value, {
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (res?.data.code === 0) {
        ElMessage({
            message: "更新成功",
            type: "success",
        });
        await getNotice();
        input.value = "";
    } else {
        ElMessage.error(res.data.description);
    }
};
const uploadRef = ref<UploadInstance>();

const submitUpload = () => {
    uploadRef.value!.submit();
};
const handleRemove: UploadProps["onRemove"] = async (file) => {
    let res = await myAxios.post("/config/remove", file.url, {
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (res.data.code === 0) {
        ElMessage({
            message: "更新成功",
            type: "success",
        });
        imgs.value=[]
        fileList.value = [];
        await getSwiper();
    } else {
        ElMessage.error(res.data.description);
    }
    checkSuccessImage();
};

const handleUpload = async (param) => {
    let fileObj = param.file; // 相当于input里取得的files
    let fd = new FormData();// FormData 对象
    fd.append("file", fileObj);// 文件对象
    let res = await myAxios.post("/config/upload", fd, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
    if (res.data.code === 0) {
        ElMessage({
            message: "更新成功",
            type: "success",
        });
        imgs.value=[]
        fileList.value = [];
        await getSwiper();
    } else {
        ElMessage.error(res.data.description);
    }
    checkSuccessImage();
};
</script>

<template>
    <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>配置</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
        <el-tabs tab-position="left">
            <el-tab-pane label="通知栏">
                <el-row>
                    <span style="margin-right: 10px">目前通知:</span>
                    <span>{{ noticeText }}</span>
                </el-row>
                <el-row>
                    <span style="margin-right: 10px;line-height: 30px">修改通知:</span>
                    <el-input style="width: 300px;margin-right: 10px" v-model="input"
                              placeholder="请输入内容"></el-input>
                    <el-button type="primary" plain @click="updateNotice">提交</el-button>
                </el-row>
            </el-tab-pane>
            <el-tab-pane label="轮播图">
                <el-carousel v-if="!isDisplay" :interval="4000" type="card" height="200px" trigger="click">
                    <el-carousel-item v-for="item in imgs" :key="item">
                        <img :src="item" height="300px" width="100%">
                    </el-carousel-item>
                </el-carousel>
                <el-empty v-if="isDisplay" description="暂无自定义及待上传图片" />
                <el-divider></el-divider>
                <el-upload
                    ref="uploadRef"
                    v-model:file-list="fileList"
                    class="upload-demo"
                    :http-request="handleUpload"
                    :on-remove="handleRemove"
                    :on-change="handleChange"
                    :auto-upload="false"
                >
                    <template #trigger>
                        <el-button type="primary">选择文件</el-button>
                    </template>

                    <el-button style="margin-left: 5px" type="success" @click="submitUpload">
                        上传
                    </el-button>

                    <template #tip>
                        <div class="el-upload__tip">
                            jpg/png files with a size less than 500kb
                        </div>
                    </template>
                </el-upload>
            </el-tab-pane>
        </el-tabs>
    </el-card>
</template>

<style scoped>
.el-row {
    margin-bottom: 20px;

    &:last-child {
        margin-bottom: 0;
    }
}

.el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
}
</style>
