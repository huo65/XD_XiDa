<template>
    <van-notice-bar color="#1989fa" background="#ecf9ff" left-icon="info-o">
        若不设置过期时间则永久有效
    </van-notice-bar>
    <van-form @submit="onSubmit">
        <van-cell-group inset>
            <van-field
                v-model="updateTeamData.name"
                name="name"
                label="名称"
                placeholder="请输入队伍名称"
                :rules="[{ required: true, message: '请输入队伍名称' }]"
            />
            <van-field
                    v-model="updateTeamData.description"
                    rows="1"
                    autosize
                    label="描述"
                    type="textarea"
                    placeholder="请输入队伍描述"
            />
            <van-field
                    v-model="updateTeamData.expireTime"
                    is-link
                    readonly
                    name="calendar"
                    label="过期时间"
                    placeholder="点击选择过期时间"
                    @click="showCalendar = true"
            />
            <van-calendar v-model:show="showCalendar" @confirm="onConfirm"/>
            <van-field name="radio" label="状态">
                <template #input>
                    <van-radio-group v-model="updateTeamData.status" direction="horizontal">
                        <van-radio name="0">公开</van-radio>
                        <van-radio name="1">私有</van-radio>
                        <van-radio name="2">加密</van-radio>
                    </van-radio-group>
                </template>
            </van-field>
            <van-field
                    v-if="updateTeamData.status==='2'"
                    v-model="updateTeamData.password"
                    type="password"
                    name="password"
                    label="密码"
                    placeholder="请输入密码"
                    :rules="[{ required: true, message: '请输入密码' }]"
            />
        </van-cell-group>
        <div style="margin: 16px;">
            <van-button round block type="primary" native-type="submit">
                更新
            </van-button>
        </div>
    </van-form>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../../plugins/my-axios.js";
import {showFailToast, showSuccessToast} from "vant";

let router = useRouter();
let route = useRoute();
const updateTeamData = ref({})
const showCalendar = ref(false);
const onConfirm = (date) => {
    let month: string | number = date.getMonth() + 1;
    month = month < 10 ? '0' + month : month
    let day = date.getDate();
    day = day < 10 ? '0' + day : day
    updateTeamData.value.expireTime = `${date.getFullYear()}-${month}-${day}`;
    showCalendar.value = false;
};
onMounted(async () => {
    const res = await myAxios.get("/team/" + route.query.id)
    if (res?.data.code === 0) {
        res.data.data.status = String(res.data.data.status)
        updateTeamData.value = res.data.data
    } else {
        showFailToast("队伍查询失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
})
const onSubmit = async () => {
    const postData = {
        ...updateTeamData.value,
        status: Number(updateTeamData.value.status),
    }
    //todo 前端校验
    const res = await myAxios.post("/team/update", postData)
    if (res?.data.code === 0) {
        showSuccessToast("更新成功")
        router.replace("/team")
    } else {
        showFailToast("更新失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
}

</script>

<style scoped>

</style>
