import router from '@/router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import checkAccess from '@/access/checkAccess.ts'
import type { Ref } from 'vue'
import { inject } from 'vue'

/**
 * 全局权限校验，每次路由跳转时，校验用户是否登录，以及用户是否有权限访问该页面
 */
//是否为首次获取用户信息
const first = inject('first') as Ref<boolean | null>

router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser
  //确保页面刷新时，首次加载时，能等待后端返回用户信息后再校验权限
  if (first) {
    await loginUserStore.getUserInfo()
    loginUser = loginUserStore.loginUser
    first.value = false
  }
  // // 如果之前没登陆过，自动登录
  if (!loginUser || !loginUser.userRole) {
    // 加 await 是为了等用户登录成功之后，再执行后续的代码
    await loginUserStore.getUserInfo()
    loginUser = loginUserStore.loginUser
  }

  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN

  // 要跳转的页面必须要登陆
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    // 如果没登陆，跳转到登录页面
    if (!loginUser || !loginUser.userRole || loginUser.userRole === ACCESS_ENUM.NOT_LOGIN) {
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
    // 如果已经登陆了，但是权限不足，那么跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      next('/noauth/noauth')
      return
    }
  }
  next()
})
