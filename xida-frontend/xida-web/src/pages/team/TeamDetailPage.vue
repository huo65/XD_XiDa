<template>
    <van-image
        width="100%"
        height="120"
        :src="team?.coverImage ||　defaultImg"
    />
    <van-cell-group inset>
        <van-cell v-if="currentUser?.role==1 || currentUser?.id==team?.userId" title="修改封面" icon="brush-o">
            <template #value>
                <van-uploader :max-count="1" :after-read="upload" v-model="fileList" :preview-image="false">
                    <van-button v-if="uploading===false" icon="plus" type="primary" size="small">上传图片</van-button>
                </van-uploader>
                <van-button v-if="uploading===true" icon="plus" type="primary" size="small" uploading
                            uploading-text="上传中...">
                    上传图片
                </van-button>
            </template>
        </van-cell>
        <van-cell title="队伍名" icon="flag-o" :value="team.name"/>
        <van-cell title="队伍描述" icon="label-o" :value="team.description"/>
        <van-cell title="队长" icon="manager-o" :value="team.leaderName"/>
        <van-cell title="队伍状态" icon="shield-o" :value="teamStatusEnum[team.status]"/>
        <van-cell title="队伍人数" icon="friends-o" :value="`${team.hasJoinNum}/${team.maxNum}`"/>
        <van-cell title="过期时间" icon="clock-o" :value="team.expireTime || '永不过期'"/>
        <van-cell title="队伍聊天室" icon="chat-o" is-link @click="toChat" v-if="team.hasJoin"/>
    </van-cell-group>
    <van-divider
        :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
    >
        队伍成员
    </van-divider>
    <team-member-card-list :login-user="currentUser" :user-list="teamMemberList" :team="team" @refresh="onRefresh"/>
    <van-empty
        v-if="(!teamMemberList || teamMemberList.length===0) && loading===false"
        image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
        image-size="80"
        description="暂时还没有队员"
    />
</template>

<script setup lang="ts">
import defaultImg from "../../../public/defalutTeamImg.jpg"
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../../plugins/my-axios.js";
import {showFailToast} from "vant";
import {teamStatusEnum} from "../../constants/team.ts";
import {getCurrentUser} from "../../services/user.ts";
import TeamMemberCardList from "../../components/TeamMemberCardList.vue";

const loading = ref(true)
const fileList = ref([])
const teamMemberList = ref()
const uploading = ref(false)
let route = useRoute();
const team = ref({})
const currentUser = ref()
onMounted(async () => {
    currentUser.value = await getCurrentUser();
    let res = await myAxios.get("/team/" + route.query.id);
    if (res?.data.code === 0) {
        team.value = res.data.data
        let res_ = await myAxios.get("/team/member/" + route.query.id);
        if (res_.data.code === 0) {
            res_.data.data.forEach((user) => {
                if (user.tags) {
                    user.tags = JSON.parse(user.tags)
                }
            })
            teamMemberList.value = res_.data.data
        } else {
          showFailToast("获取队伍成员失败" + (res.data.description ? `,${res.data.description}` : ''))
        }
    } else {
      showFailToast("队伍查询失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
    loading.value = false
})
let router = useRouter();
const toChat = () => {
    router.push({
        path: "/chat",
        query: {
            teamId: team.value.id,
            teamName: team.value.teamName,
            teamType: 2
        }
    })
}
const upload = async (file) => {
    uploading.value = true
    let formData = new FormData();
    formData.append("file", file.file)
    formData.append("id", team.value.id)
    await myAxios.put("/team/cover", formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
    uploading.value = false
    location.reload()
}
const onRefresh = () => {
    location.reload()
}
</script>

<style scoped>

</style>
