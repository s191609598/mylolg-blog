<template>
  <div id="userloginPage">
    <h2 class="title">mylog-用户登录</h2>
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
      <div class="tips">
        <RouterLink to="/user/register">没有账号？去注册</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from 'vue'
import { loginUsingPost } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import router from '@/router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const formState = reactive<API.UserLoginDTO>({
  userAccount: '',
  userPassword: '',
})

const loginUserStore = useLoginUserStore()

const handleSubmit = async (values: any) => {
  try {
    const res = await loginUsingPost(values)
    if (res.data.code === 0) {
      const token = res.data.data?.tokenValue
      const tokenTimeout = res.data.data?.tokenTimeout
      if (token) {
        localStorage.setItem(
          'auth_data',
          JSON.stringify({
            token: token,
            timestamp: Date.now() + tokenTimeout,
          }),
        )
      }
      await loginUserStore.getUserInfo()
      message.success('登录成功')
      router.push({
        path: '/',
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
#userloginPage {
  max-width: 360px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.tips {
  text-align: right;
  margin-bottom: 20px;
}
</style>
