<template>
    <template v-if="user">
        <div>
            <van-cell title="头像" is-link center>
                <van-uploader v-model="fileList" :max-count="1" :after-read="afterRead" preview-size="60px">
                    <template #preview-delete/>
                    <img :src="imgSrc" style="width: 60px;height: 60px;border-radius: 50%" alt=""/>
                </van-uploader>
            </van-cell>
            <van-cell title="用户账号" :value="user.userAccount"/>
            <van-cell title="昵称" is-link :value="user.username"
                      @click="toEdit('username','昵称',user.username)"/>
            <van-cell title="标签" to="/user/tag" is-link/>
            <van-cell title="个性签名" is-link
                      @click="toEdit('profile','个性签名',user.profile)">
                <template #value>
                    <van-text-ellipsis :content="user.profile || '还没有填写个性签名'"/>
                </template>
            </van-cell>
            <van-cell title="性别" is-link @click="()=>showPicker=true">
                <span v-if="user.gender===1">男</span>
                <span v-else-if="user.gender===0">女</span>
                <span v-else-if="user.gender===2">保密</span>
                <span v-else>{{ "还没有填写性别" }}</span>
            </van-cell>
            <van-cell title="电话" is-link :value="user.phone" @click="toEdit('phone','电话',user.phone)"/>
            <van-cell title="邮箱" is-link :value="user.email || '还没有填写邮箱'"
                      @click="toEdit('email','邮箱',user.email)"/>
            <van-cell title="修改密码" is-link to="/update/password"/>
            <van-popup v-model:show="showPicker" round position="bottom">
                <van-picker
                    title="性别"
                    :columns="genders"
                    @confirm="onConfirmGender"
                    @cancel="()=>showPicker=false"
                />
            </van-popup>
        </div>
    </template>
</template>

<script setup lang="ts">

import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {showFailToast, showSuccessToast} from "vant";
import {getCurrentUser} from "../../services/user.ts";
import myAxios from "../../plugins/my-axios.js";

let router = useRouter();
const fileList = ref([]);
const user = ref()
const imgSrc = ref('')
const showPicker = ref(false);
const genders = [
    {text: '男', value: '1'},
    {text: '女', value: '0'},
    {text: '保密', value: '2'}
];

async function getUser() {
    let currentUser = await getCurrentUser();
    if (currentUser) {
        user.value = currentUser
        imgSrc.value = currentUser.avatarUrl
    } else {
        showFailToast("未登录")
        await router.replace("/user/login")
    }
}

onMounted(async () => {
    await getUser();
})
const toEdit = (editKey: string, editName: string, editValue: string) => {
    router.push({
        path: '/user/edit',
        query: {
            editKey,
            editName,
            editValue
        }
    })
}
const afterRead = async () => {
    let formData = new FormData();
    formData.append("file", fileList.value[0].file)
    const res = await myAxios.post("/common/upload", formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
    if (res?.data.code === 0) {
        showSuccessToast("头像更新成功")
        imgSrc.value = res?.data.data
    } else {
      showFailToast("头像更新失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
    fileList.value = []
}
const onConfirmGender = async ({selectedValues}) => {
    const res = await myAxios.put("/user/update", {
        gender: selectedValues[0]
    })
    if (res?.data.code === 0) {
        showSuccessToast("修改成功")
    } else {
      showFailToast("修改失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
    showPicker.value = false
    await refresh()
};

const refresh = async () => {
    await getUser()
}
</script>

<style scoped>
:deep(.van-uploader__upload) {
    border-radius: 50%;
}

:deep(.van-uploader__preview-image) {
    border-radius: 50%;
}

:deep(.van-cell) {
    padding-left: 23px;
    padding-right: 16px;
}
</style>
