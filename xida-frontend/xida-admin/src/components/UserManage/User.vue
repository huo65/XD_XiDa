<script setup lang="ts">
import {onMounted, ref, reactive} from "vue";
import myAxios from "../../plugins/my-axios.ts";
import {ArrowRight, Delete, Edit, Search} from "@element-plus/icons-vue";
import {FormRules} from "element-plus";
import {ElMessage, ElMessageBox} from 'element-plus'

const currentPage = ref(1)
const pageSize = ref(0)
const total = ref(0)
const tableData = ref([])
const searchUsername = ref('')
const addUserFormVisible = ref(false)
const beforeBanUserFormVisible = ref(false)
const formLabelWidth = '140px'
const updateUserFormVisible = ref(false)
const userInfoFormVisible = ref(false)
const genderValue = ref('')
const genderValueOption = [
  {
    value: '0',
    label: '女',
  },
  {
    value: '1',
    label: '男',
  }
]
const isAdmin = ref("0")
onMounted(async () => {
  await handleSearch()
})
const researchUser = () => {
  handleSearch()
}
const handleCurrentChange = async (val: number) => {
  tableData.value = []
  const url = "/user/search?username=" + searchUsername.value + "&currentPage=" + val
  let res = await myAxios.get(url);
  if (res?.data.code === 0) {
    res.data.data.records.forEach(user => {
      user.status = user.status == 0 ? '正常' : '封禁'
      user.role = user.role == 1 ? '管理员' : '普通用户'
      if (user.gender == 1) {
        user.gender = '男'
      } else if (user.gender == 0) {
        user.gender = '女'
      } else {
        user.gender = '未知'
      }
      tableData.value.push(user)
    })
    currentPage.value = res.data.data.current
    pageSize.value = res.data.data.size
    total.value = res.data.data.total
  }
}
const handleSearch = async () => {
  tableData.value = []
  currentPage.value = 1
  let res = await myAxios.get("/user/search?username=" + searchUsername.value + "&currentPage=" + currentPage.value);
  if (res?.data.code === 0) {
    res.data.data.records.forEach(user => {
      user.status = user.status == 0 ? '正常' : '封禁'
      user.role = user.role == 1 ? '管理员' : '普通用户'
      if (user.gender == 1) {
        user.gender = '男'
      } else if (user.gender == 0) {
        user.gender = '女'
      } else {
        user.gender = '未知'
      }
      tableData.value.push(user)
    })
    currentPage.value = res.data.data.current
    pageSize.value = res.data.data.size
    total.value = res.data.data.total
  }
}
const handleAddUser = async () => {
  let res = await myAxios.post("/user/admin/register", addUserForm);
  if (res.data.code === 0) {
    ElMessage({
      message: '用户添加成功,新用户ID为' + res.data.data,
      type: 'success',
    })
    addUserFormVisible.value = false
  } else {
    ElMessage.error(res.data.description)
  }
}
const getUserInfo = async (id: string) => {
  let res = await myAxios.get("/user/" + id);
  if (res.data.code === 0) {
    updateUserFormVisible.value = true
    genderValue.value = res.data.data.gender + ''
    isAdmin.value = res.data.data.role + ''
    updateUserForm.value = res.data.data
  } else {
    ElMessage.error(res.data.description)
  }
}
const searchedUser = ref({
  userAccount: '',
  username: '',
  avatarUrl: ''
})
const searchUser = async (id: string) => {
  let res = await myAxios.get("/user/" + id);
  if (res.data.code === 0) {
    beforeBanUserFormVisible.value = false
    userInfoFormVisible.value = true
    searchedUser.value = res.data.data
  } else {
    ElMessage.error(res.data.description)
  }
}
const banUser = async (id: number) => {
  let res = await myAxios.get("/user/ban?id=" + id);
  if (res.data.code === 0) {
    ElMessage({
      message: '用户封禁/解禁成功',
      type: 'success',
    })
    userInfoFormVisible.value = false
    await handleSearch()
  } else {
    ElMessage.error(res.data.description)
  }
}
const handleUpdateUser = async () => {
  const userData = {
    id: updateUserForm.value.id,
    username: updateUserForm.value.username,
    gender: genderValue.value,
    phone: updateUserForm.value.phone,
    email: updateUserForm.value.email,
    profile: updateUserForm.value.profile,
    role: isAdmin.value
  }
  let res = await myAxios.put("/user/admin/update", userData);
  if (res.data.code === 0) {
    updateUserFormVisible.value = false
    ElMessage({
      message: '用户更新成功',
      type: 'success',
    })
    await handleSearch()
  } else {
    ElMessage.error(res.data.description)
  }
}
const beforeBanUserForm = ref({
  id: '',
})
const updateUserForm = ref({
  id: '',
  userAccount: '',
  username: '',
  gender: '',
  profile: '',
  phone: '',
  email: '',
  role: ''
})
const addUserForm = reactive({
  userAccount: '',
  phone: '',
  userPassword: ''
})

interface RuleForm {
  userAccount: string,
  userPassword: string
}

const addUserFormRoles = reactive<FormRules<RuleForm>>({
  userAccount: [
    {required: true, message: '请输入用户名', trigger: 'blur'}
  ],
  userPassword: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ]
})

const confirmDelete = (id: string) => {
  ElMessageBox.confirm(
      '此操作无法撤销',
      '确认删除用户',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        let res = await myAxios.delete("/user/delete?id=" + id);
        if (res.data.code === 0) {
          ElMessage({
            type: 'success',
            message: '删除成功',
          })
          await handleSearch()
        } else {
          ElMessage.error(res.data.description)
        }
      })
}
</script>

<template>
  <el-breadcrumb :separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>用户管理</el-breadcrumb-item>
  </el-breadcrumb>
  <el-card>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input placeholder="请输入内容" v-model="searchUsername" clearable @change="researchUser">
          <template #append>
            <el-button :icon="Search" @click="handleSearch"/>
          </template>
        </el-input>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" @click="addUserFormVisible = true">添加用户</el-button>
      </el-col>
      <el-col :span="2">
        <el-button type="danger" @click="beforeBanUserFormVisible = true">封禁/解禁用户</el-button>
      </el-col>
    </el-row>
    <el-table :data="tableData" table-layout="fixed" border stripe>
      <el-table-column label="头像" width="80px" slot-scope="scope">
        <template #default="scope">
          <el-avatar
              :src="scope.row.avatarUrl"
              style="width: 50px;height: 50px"
          />
        </template>
      </el-table-column>
      <el-table-column prop="userAccount" label="账号"/>
      <el-table-column prop="username" label="昵称"/>
      <el-table-column prop="gender" label="性别" width="80px"/>
      <el-table-column prop="phone" label="手机号" width="120px"/>
      <el-table-column prop="email" label="邮箱"/>
      <el-table-column prop="role" label="角色" width="100px"/>
      <el-table-column prop="status" label="状态" width="80px"/>
      <el-table-column fixed="right" label="操作" width="120px" slot-scope="scope">
        <template #default="scope">
          <el-button type="primary" :icon="Edit" circle @click="getUserInfo(scope.row.id)"/>
          <el-button type="danger" :icon="Delete" circle @click="confirmDelete(scope.row.id)"/>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :background="true"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="handleCurrentChange"
    />
    <el-dialog v-model="addUserFormVisible" title="添加用户" width="40%" center>
      <el-form :model="addUserForm" :rules="addUserFormRoles">
        <el-form-item label="账户名/昵称" :label-width="formLabelWidth" prop="userAccount">
          <el-input v-model="addUserForm.userAccount" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="用户密码" :label-width="formLabelWidth" prop="userPassword">
          <el-input v-model="addUserForm.userPassword" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth">
          <el-input v-model="addUserForm.phone" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="addUserFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddUser">
          提交
        </el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog v-model="beforeBanUserFormVisible" title="请输入要封禁/解禁的用户ID" width="40%">
      <el-form :model="beforeBanUserForm">
        <el-form-item label="用户ID">
          <el-input v-model="beforeBanUserForm.id" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="beforeBanUserFormVisible = false">取消</el-button>
        <el-button type="primary" @click="searchUser(beforeBanUserForm.id)">
          提交
        </el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog v-model="userInfoFormVisible" width="20%" title="确认用户">
      <el-card :body-style="{ padding: '0px' }">
        <img
            :src="searchedUser.avatarUrl"
            class="image"
        />
        <div style="padding: 14px">
          <span>账户名:{{ searchedUser.userAccount }}</span><br/>
          <span>用户昵称:{{ searchedUser.username }}</span>
        </div>
      </el-card>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="userInfoFormVisible = false">取消</el-button>
        <el-button type="danger" @click="banUser(beforeBanUserForm.id)">
          封禁/解禁
        </el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog v-model="updateUserFormVisible" title="更新用户" width="40%" center>
      <el-form :model="updateUserForm">
        <el-form-item label="用户ID" :label-width="formLabelWidth">
          <el-input v-model="updateUserForm.id" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="账户名" :label-width="formLabelWidth">
          <el-input v-model="updateUserForm.userAccount" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="用户昵称" :label-width="formLabelWidth">
          <el-input v-model="updateUserForm.username" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth">
          <el-select
              v-model="genderValue"
              placeholder="性别"
              style="width: 240px"
          >
            <el-option
                v-for="item in genderValueOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth">
          <el-input v-model="updateUserForm.phone" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth">
          <el-input v-model="updateUserForm.email" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="自我介绍" :label-width="formLabelWidth">
          <el-input v-model="updateUserForm.profile" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="是否为管理员" :label-width="formLabelWidth">
          <el-switch v-model="isAdmin" active-value="1" inactive-value="0"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="updateUserFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateUser">
          提交
        </el-button>
      </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}

.image {
  width: 100%;
  display: block;
}
</style>
