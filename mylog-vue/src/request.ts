import axios from 'axios'
import { message } from 'ant-design-vue'
import { baseUrl } from '@/config/config.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

// 创建 Axios 实例
const myAxios = axios.create({
  baseURL: baseUrl,
  timeout: 60000,
  withCredentials: true,
})

// 全局请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    const authData = localStorage.getItem('auth_data')
    if (authData) {
      try {
        const { timestamp, token } = JSON.parse(authData)
        // 设置30天有效期
        if (Date.now() - timestamp > 30 * 24 * 60 * 60 * 1000) {
          const loginUserStore = useLoginUserStore()
          localStorage.removeItem('auth_data')
          loginUserStore.$reset()
          loginUserStore.setUserInfo({
            userInfo: {
              userName: '未登录',
              id: null,
              userAvatar: null,
            },
          })
          config.headers.delete('mylog-token')
        } else {
          config.headers.set('mylog-token', token)
        }
      } catch (e) {
        localStorage.removeItem('auth_data')
      }
    }
    return config
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  },
)

// 全局响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    const { data } = response
    // 未登录
    if (data.code === 40100) {
      // 不是获取用户信息的请求，并且用户目前不是已经在用户登录页面，则跳转到登录页面
      if (
        !response.request.responseURL.includes('/mylog/user/getuserinfo') &&
        !window.location.pathname.includes('/user/login')
      ) {
        message.warning('请先登录')
        window.location.href = `/user/login?redirect=${window.location.href}`
      }
    }
    return response
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error)
  },
)

export default myAxios
