import { defineStore } from 'pinia'
import { getUserInfoUsingGet } from '@/api/userController.ts'
import ACCESS_ENUM from '@/access/accessEnum.ts'

export const useLoginUserStore = defineStore('loginUser', {
  state: () => ({
    loginUser: {
      userName: '未登录',
      id: undefined,
      userAvatar: undefined,
      userRole: ACCESS_ENUM.NOT_LOGIN,
      isLoggedIn: false,
    },
  }),
  actions: {
    async getUserInfo() {
      const res = await getUserInfoUsingGet()
      if (res.data.code === 0 && res.data.data) {
        this.loginUser = res.data.data
      }
    },
    setUserInfo({ userInfo }: { userInfo: any }) {
      this.loginUser = userInfo
    },
  },
  persist: true, // 持久化存储
})
