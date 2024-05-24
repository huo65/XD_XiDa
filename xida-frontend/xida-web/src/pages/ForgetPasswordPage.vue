<template>
    <van-sticky>
        <van-nav-bar
            title="忘记密码"
            left-arrow
            @click-left="onClickLeft"
        >
        </van-nav-bar>
    </van-sticky>
    <van-row justify="center">
        <van-image
            width="343"
            src="../../public/super.jpg"
            style="background-position:center"
        />
    </van-row>
    <div v-if="one">
        <van-form @submit="onSubmit">
            <van-cell-group inset>
                <van-field
                    v-model="phone"
                    name="userAccount"
                    label="手机号"
                    placeholder="请输入绑定的手机号"
                    :rules="[{ required: true, message: '请输入绑定的手机号' }]"
                />
            </van-cell-group>
            <div style="margin: 30px 16px 16px;">
                <van-button style="margin-top: 10px" round block type="primary" native-type="submit">
                    确认
                </van-button>
            </div>
        </van-form>
    </div>
    <div v-if="two">
        <van-cell center>
            <template #title>
                <span style="font-size: 20px;font-weight: bold;margin-left: 16px">为{{ username }}修改密码</span>
            </template>
            <template #label>
                <span style="font-size: 15px;margin-left: 16px">{{ phone }}</span>
            </template>
        </van-cell>
        <van-password-input
            :value="code"
            :length="4"
            :mask="false"
            :focused="showKeyboard"
            @focus="showKeyboard = true"
        />
        <van-number-keyboard
            v-model="code"
            :show="showKeyboard"
            @blur="showKeyboard = false"
        />
        <div style="margin: 30px 16px 16px;">
            <van-button style="margin-top: 10px" round block type="primary" @click="checkCode">
                确认
            </van-button>
        </div>
    </div>
    <div v-if="three">
        <van-field
            v-model="password"
            required
            type="password"
            label="新密码"
            placeholder="请输入新密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
        <van-field
            v-model="confirmPassword"
            required
            type="password"
            label="确认新密码"
            placeholder="请输入新密码"
            :rules="[{ validator, message: '两次输入的密码不一致' }]"
        />
        <div style="margin: 30px 16px 16px;">
            <van-button style="margin-top: 10px" round block type="primary" @click="confirmUpdate">
                确认
            </van-button>
        </div>
    </div>
</template>

<script setup>
import {useRouter} from "vue-router";
import {ref} from "vue";
import myAxios from "../plugins/my-axios.js";
import {showFailToast, showSuccessToast} from "vant";

const one = ref(true)
const two = ref(false)
const three = ref(false)
const confirmPassword = ref('')
const password = ref('');
const code = ref('');
const showKeyboard = ref(false);
let router = useRouter();
const phone = ref()
const onClickLeft = () => {
    router.push("/")
};
const validator = () => {
    return password.value === confirmPassword.value;
}
const username = ref("")
const onSubmit = async () => {
    let res = await myAxios.get("/user/forget?phone=" + phone.value);
    if (res?.data.code === 0) {
        username.value = res.data.data
        one.value = false
        two.value = true
    } else {
        showFailToast("失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
}
const checkCode = async () => {
    let res = await myAxios.get("/user/check?phone=" + phone.value + "&code=" + code.value);
    if (res?.data.code === 0) {
        two.value = false
        three.value = true
    } else {
        showFailToast("失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
}
const confirmUpdate = async () => {
    let res = await myAxios.put("/user/forget", {
        phone: phone.value,
        code: code.value,
        password: password.value,
        confirmPassword: confirmPassword.value
    });
    if (res?.data.code === 0) {
        showSuccessToast("修改成功")
        await router.replace("/user")
    } else {
        showFailToast("失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
}
</script>

<style scoped>

</style>
