<template>
    <van-card v-for="user in userList"
              :title="user.username"
              :desc="user.profile"
    >
        <template #thumb>
            <van-image :src="user?.avatarUrl" width="88" height="88" @click="showUserDetail(user?.id)"/>
        </template>
        <template #tags>
            <van-tag v-for="tag in user?.tags" plain type="danger" style="margin-right: 8px;margin-top: 8px"
                     @click="showUserDetail(user?.id)">
                {{ tag }}
            </van-tag>
        </template>
        <template #num>
            <van-button v-if="loginUser.id===team.userId || loginUser.role===1" size="mini" plain type="danger"
                        style="width: 48px;height: 28px;margin-top: 10px" @click="kickOut(user.id)">
                踢出
            </van-button>
            <!--            <van-button v-if="user?.isFollow" size="mini" plain type="primary"-->
            <!--                        style="width: 48px;height: 28px;margin-top: 10px"-->
            <!--                        color="#c1c1c1" @click="followUser(user)">-->
            <!--                已关注-->
            <!--            </van-button>-->
            <!--            <van-button v-else size="mini" plain type="primary" style="width: 48px;height: 28px;margin-top: 10px"-->
            <!--                        @click="followUser(user)">-->
            <!--                关注-->
            <!--            </van-button>-->
        </template>
    </van-card>
</template>

<script setup lang="ts">
import {UserType} from "../models/user.js";
import myAxios from "../plugins/my-axios.js";
import {useRouter} from "vue-router";
import {TeamType} from "../models/team";

interface UserCardListProps {
    userList: UserType[],
    loginUser: UserType,
    team: TeamType
}

const props = defineProps<UserCardListProps>()
let emits = defineEmits(['refresh']);
let router = useRouter();
const showUserDetail = (id) => {
    router.push({
        path: "/user/detail",
        query: {
            id: id
        }
    })
}
const kickOut = async (userId) => {
    let res = await myAxios.post("/team/kick", {
        teamId: props.team.id,
        userId: userId
    });
    if (res?.data.code === 0) {
        emits("refresh")
    }
}
</script>

<style scoped>

</style>
