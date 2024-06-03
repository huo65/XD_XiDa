<script setup lang="ts">
import {Lock, User} from "@element-plus/icons-vue";
import {reactive, ref} from 'vue'
import type {FormInstance, FormRules} from 'element-plus'
import myAxios from "../plugins/my-axios.ts";
import {ElMessage} from 'element-plus'

const loginFormRef = ref<FormInstance>()
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let response = await myAxios.post("/user/admin/login", loginForm.value);
      if (response.data.code === 0 && response.data.data) {
        sessionStorage.setItem("token", response.data.data)
        ElMessage({
          message: '登陆成功!',
          type: 'success',
        })
        window.location.href = '/dashboard'
      } else {
        ElMessage({
          message: '登录失败!'+response.data.description,
          type: 'warning',
        })
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}

interface RuleForm {
  userAccount: string,
  userPassword: string
}

const loginForm = ref({
  userAccount: '',
  userPassword: ''
})
const loginFormRoles = reactive<FormRules<RuleForm>>({
  userAccount: [
    {required: true, message: '请输入用户名', trigger: 'blur'}
  ],
  userPassword: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ]
})
</script>

<template>
  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">
        <img src="../../public/LOGO.png" alt="">
      </div>
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRoles" label-width="0px" class="login_form">
        <el-form-item prop="userAccount">
          <el-input v-model="loginForm.userAccount" :prefix-icon="User"
                    placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="userPassword">
          <el-input v-model="loginForm.userPassword" :prefix-icon="Lock" type="password" show-password
                    placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item class="btn">
          <el-button type="primary" @click="submitForm(loginFormRef)">登录</el-button>
          <el-button type="info" @click="resetForm(loginFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login_container {
  background-color: #2b4b6b;
  height: 100%;
}

.login_box {
  width: 450px;
  height: 300px;
  background-color: #fff;
  border-radius: 10px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);

  .avatar_box {
    height: 130px;
    width: 130px;
    border: 1px solid #eee;
    border-radius: 50%;
    padding: 10px;
    box-shadow: 0 0 10px #ddd;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;

    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      background-color: #eee;
    }
  }
}

.login_form {
  position: absolute;
  left: 50%;
  top: 60%;
  transform: translate(-50%, -50%);
  width: 300px;
  padding: 0 20px;
  box-sizing: border-box;
  margin: 0 auto;
}

:deep(.el-form-item__content) {
  display: flex;
  justify-content: center;
}
</style>
