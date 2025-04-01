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
      <a-form-item name="captchaCode">
        <div style="display: flex; gap: 8px; align-items: center">
          <div style="flex: 3">
            <a-input v-model:value="formState.captchaCode" placeholder="请输入验证码" />
          </div>
          <div
            style="
              flex: 2;
              height: 32px;
              background: #f5f5f5;
              display: flex;
              justify-content: center;
              align-items: center;
              cursor: pointer;
            "
          >
            <img
              :src="captchaImage"
              style="max-width: 100%; height: 55px; object-fit: contain"
              v-if="captchaImage"
            />
          </div>
        </div>
      </a-form-item>
      <a-form-item name="captchaKey" style="display: none"></a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import router from '@/router'
import { userRegisterUsingPost } from '@/api/userController.ts'
import { getCaptchaUsingGet } from '@/api/homeController.ts'

const captchaImage = ref('')
const loading = ref(false)

const formState = reactive<API.UserRegisterDTO>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
  captchaCode: '',
  captchaKey: '',
})

// 获取验证码方法
const getCaptcha = async () => {
  try {
    loading.value = true
    const res = await getCaptchaUsingGet() // 需要创建对应的API接口
    if (res.data.code === 0) {
      captchaImage.value = res.data.data.captchaImg
      formState.captchaKey = res.data.data.captchaKey // 需要存储验证码key
      // console.log(res.data.data.captchaKey)
      // console.log(formState.captchaKey)
    } else {
      message.error(res.data.msg)
    }
  } catch (e) {
    message.error('获取验证码失败')
  } finally {
    loading.value = false
  }
}

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

// 初始化时获取验证码
onMounted(() => {
  getCaptcha()
})
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
