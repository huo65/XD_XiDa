<template>
    <van-notice-bar color="#1989fa" background="#ecf9ff" left-icon="info-o">
        若不设置过期时间则永久有效
    </van-notice-bar>
    <van-form @submit="onSubmit">
        <van-cell-group inset>
            <van-field
                v-model="addTeamData.name"
                name="name"
                label="名称"
                placeholder="请输入队伍名称"
                :rules="[{ required: true, message: '请输入队伍名称' }]"
            />
            <van-field
                v-model="addTeamData.description"
                rows="1"
                autosize
                label="描述"
                type="textarea"
                placeholder="请输入队伍描述"
            />
            <van-field
                v-model="addTeamData.expireTime"
                is-link
                readonly
                name="calendar"
                label="过期时间"
                placeholder="点击选择过期时间"
                @click="showCalendar = true"
            />
            <van-calendar v-model:show="showCalendar" @confirm="onConfirm" />
            <van-field name="stepper" label="请选择人数">
                <template #input>
                    <van-stepper v-model="addTeamData.maxNum" max="10" />
                </template>
            </van-field>
            <van-field name="radio" label="状态">
                <template #input>
                    <van-radio-group
                        v-model="addTeamData.status"
                        direction="horizontal"
                    >
                        <van-radio name="0">公开</van-radio>
                        <van-radio name="1">私有</van-radio>
                        <van-radio name="2">加密</van-radio>
                    </van-radio-group>
                </template>
            </van-field>
            <van-field
                v-if="addTeamData.status === '2'"
                v-model="addTeamData.password"
                type="password"
                name="password"
                label="密码"
                placeholder="请输入密码"
                :rules="[{ required: true, message: '请输入密码' }]"
            />
        </van-cell-group>
        <div style="margin: 16px">
            <van-button round block type="primary" native-type="submit">
                添加
            </van-button>
        </div>
    </van-form>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { ref } from "vue";
import myAxios from "../../plugins/my-axios.js";
import { showFailToast, showSuccessToast } from "vant";

let router = useRouter();
const showCalendar = ref(false);
const onConfirm = (date) => {
    let month: string | number = date.getMonth() + 1;
    month = month < 10 ? "0" + month : month;
    let day = date.getDate();
    day = day < 10 ? "0" + day : day;
    addTeamData.value.expireTime = `${date.getFullYear()}-${month}-${day}`;
    showCalendar.value = false;
};
const initFormData = {
    name: "",
    description: "",
    expireTime: "",
    maxNum: 1,
    password: "",
    status: "0",
};
const addTeamData = ref({ ...initFormData });
const onSubmit = async () => {
    const postData = {
        ...addTeamData.value,
        status: Number(addTeamData.value.status),
    };
    //todo 前端校验
    const res = await myAxios.post("/team/add", postData);
    if (res?.data.code === 0) {
        showSuccessToast("添加成功");
        await router.replace("/team");
    } else {
        showFailToast(
            "添加失败" +
            (res.data.description ? `,${res.data.description}` : ""),
        );
    }
};
</script>

<style scoped></style>
