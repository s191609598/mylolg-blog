<template>
  <div id="userRegisterPage">
    <h2 class="title">mylog-用户注册</h2>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item
        name="userAccount"
        :rules="[
          { required: true, message: '请输入账号!' },
          { min: 6, message: '账号长度不能小于6位' },
          { max: 16, message: '账号长度不能大于16位' },
        ]"
      >
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>

      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码!' },
          { min: 6, message: '密码长度不能小于6位' },
          { max: 16, message: '密码长度不能大于16位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>

      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '请输入确认密码!' },
          { min: 6, message: '密码长度不能小于6位' },
          { max: 16, message: '密码长度不能大于16位' },
          { validator: validateCheckPassword, trigger: 'change' },
        ]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请输入确认密码" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from 'vue'
import { message } from 'ant-design-vue'
import router from '@/router'
import { userRegisterUsingPost } from '@/api/userController.ts'

const formState = reactive<API.UserRegisterDTO>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const validateCheckPassword = (rule: any, value: string) => {
  if (value !== formState.userPassword) {
    return Promise.reject('两次输入的密码不一致!')
  }
  return Promise.resolve()
}
const handleSubmit = async (values: any) => {
  try {
    const res = await userRegisterUsingPost(values)
    if (res.data.code === 0) {
      message.success('注册成功')
      router.push({
        path: '/user/login',
        replace: true,
      })
    } else {
      message.error(res.data.msg)
    }
  } catch (e) {
    message.error(e instanceof Error ? e.message : '请求失败')
  }
}
</script>
<style scoped>
#userRegisterPage {
  max-width: 360px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

</style>
