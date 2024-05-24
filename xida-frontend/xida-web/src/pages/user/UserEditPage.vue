<template>
    <van-form @submit="onSubmit">
        <van-field
                v-model="editUser.currentValue"
                :name="`${editUser.editKey}`"
                required
                :label="`新${editUser.editName}`"
                :placeholder="`请输入${editUser.editName}`"
                :rules="[{ required: true, message: `请填写${editUser.editName}` }]"
        >
            <template v-if="editUser.editKey==='phone'|| editUser.editKey==='email'" #button>
                <van-button v-if="editUser.editKey==='phone'" size="small" type="primary" @click="sendMessage">
                    <span v-if="!codeTime">发送验证码</span>
                    <span v-else>{{ codeTime }}秒</span>
                </van-button>

                <van-button v-else size="small" type="primary" @click="sendEmailMessage">
                    <span v-if="!codeTime">发送验证码</span>
                    <span v-else>{{ codeTime }}秒</span>
                </van-button>
            </template>
        </van-field>

        <van-field
                v-if="!lock"
                v-model="code"
                required
                label="验证码"
                placeholder="请输入验证码"
                :rules="[{ required: true, message: '请填写验证码' }]"
        />
        <div style="margin: 16px;">
            <van-button round block type="primary" native-type="submit">
                确定
            </van-button>
        </div>
    </van-form>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import myAxios from "../../plugins/my-axios.js";
import {ref} from "vue";
import {showFailToast, showNotify, showSuccessToast} from "vant";
import {getCurrentUser} from "../../services/user.ts";

const codeTime = ref(0)
const code = ref('')
const router = useRouter();
const route = useRoute();
const editUser = ref({
    editKey: route.query.editKey,
    editName: route.query.editName,
    currentValue: route.query.editValue
})
const reg_tel = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
const onSubmit = async () => {
    const currentUser = await getCurrentUser();
    let response = await myAxios.put("/user/update", {
        'id': currentUser.id,
        [editUser.value.editKey]: editUser.value.currentValue,
        code: code.value
    });
    if (response.data.code === 0) {
        showSuccessToast("修改成功")
        await router.replace("/user/update");
    } else {
        showFailToast("修改失败" + (response.data.description ? `,${response.data.description}` : ''))
    }
};
const lock = ref(true)
const sendMessage = async () => {
    let phone = editUser.value.currentValue;
    if (phone === '') {
        showNotify({message: '请先输入手机号'});
    } else {
        if (!reg_tel.test(<string>phone)) {
            showNotify({message: '手机号格式错误'});
        } else {
            let flag = countDown();
            if (flag) {
                const res = await myAxios.get("/user/message/update/phone?phone=" + phone)
                lock.value = false
                if (res?.data.code === 0) {
                    showSuccessToast("短信发送成功，15分钟内有效")
                } else {
                    showFailToast("短信发送失败," + res?.data.description ?? '')
                }
            }
        }
    }
}
const sendEmailMessage = async () => {
    let email = editUser.value.currentValue;
    if (email === '') {
        showNotify({message: '请先输入手机号'});
    } else {
        let flag = countDown();
        if (flag) {
            let res = await myAxios.get("/user/message/update/email?email=" + email);
            lock.value = false
            if (res?.data.code === 0) {
                showSuccessToast("邮件发送成功，15分钟内有效")
            } else {
                showFailToast("邮件发送失败," + res?.data.description ?? '')
            }
        }

    }
}
const countDown = () => {
    if (codeTime.value > 0) {
        showFailToast("不能重复获取")
        return false;
    } else {
        codeTime.value = 60
        let time = setInterval(() => {
            codeTime.value--
            if (codeTime.value < 1) {
                clearInterval(time)
                codeTime.value = 0
            }
        }, 1000)
        return true;
    }
}
</script>

<style scoped>

</style>
