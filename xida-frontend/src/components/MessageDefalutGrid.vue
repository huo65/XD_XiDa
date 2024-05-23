<template>
    <van-grid :border="false">
        <van-grid-item icon="comment" icon-color="#767ffe" text="所有评论" to="/user/comment"/>
        <van-grid-item icon="good-job" icon-color="#639efc" text="赞"/>
        <van-grid-item icon="friends" icon-color="#a778fc" text="关注" to="/user/follow"/>
        <van-grid-item to="/fans">
            <template #icon>
                <van-icon class-prefix="my-icon" name="wodefensi" color="#65cdf2" size="28"/>
            </template>
            <template #text>
                <span style="margin-top: 8px;font-size: 14px">粉丝</span>
            </template>
        </van-grid-item>
    </van-grid>
    <van-divider/>
</template>

<script>

</script>

<style scoped>

</style>
