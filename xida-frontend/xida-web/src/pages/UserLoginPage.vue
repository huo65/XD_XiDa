<template>
    <div id="login">
        <van-row justify="center">
            <van-image
                    width="343"
                    src="../../public/super.jpg"
                    style="background-position:center"
            />
        </van-row>
        <van-form @submit="onSubmit">
            <van-cell-group inset>
                <van-field
                        v-model="userAccount"
                        name="userAccount"
                        label="账号"
                        placeholder="请输入账号"
                        :rules="[{ required: true, message: '请输入用户名' }]"
                />
                <van-field
                    v-model="password"
                    type="password"
                    name="password"
                    label="密码"
                    placeholder="请输入密码"
                    :rules="[{ required: true, message: '请输入密码' }]"
                />
            </van-cell-group>
            <span style="right: 22px;position: fixed;font-size: 12px;color: #3c89fc;text-decoration: underline"
                  @click="toForget">忘记密码</span>
            <div style="margin: 30px 16px 16px;">
                <van-button style="margin-top: 10px" round block type="primary" native-type="submit">
                    登录
                </van-button>
              <van-button style="margin-top: 10px" color="#FFA034" round block type="primary"
                            to="/user/signup">
                    注册
                </van-button>
            </div>
        </van-form>
    </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import myAxios from "../plugins/my-axios.js";
import {showFailToast, showSuccessToast} from "vant";
import {useRoute, useRouter} from "vue-router";

const userAccount = ref('');
const password = ref('');
let router = useRouter();
let route = useRoute();
const onSubmit = async () => {
    const response = await myAxios.post("/user/login", {
        "userAccount": userAccount.value,
        "userPassword": password.value
    })
    if (response.data.code === 0 && response.data.data) {
        sessionStorage.setItem("token", response.data.data)
        showSuccessToast("登录成功")
        window.location.href = <string>route.query.redirectUrl ?? '/'
    } else {
        showFailToast("登录失败" + (response.data.description ? `,${response.data.description}` : ''))
    }
};
const toForget = () => {
    router.push("/forget")
}
</script>

<style scoped>

</style>
