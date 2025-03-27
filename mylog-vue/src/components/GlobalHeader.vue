<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <!--      菜单-->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <!--      搜索-->
      <a-col>
        <a-button :icon="h(SearchOutlined)" @click="onSearch">搜索</a-button>
        <SearchArticleModal v-model:open="open" />
        <!--        头像-->
      </a-col>
      <a-col flex="100px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
                <span class="username">{{ loginUserStore.loginUser.userName ?? '匿名' }}</span>
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <router-link to="/my_space">
                      <UserOutlined />
                      个人中心
                    </router-link>
                  </a-menu-item>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" @click="onLogin">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import { LogoutOutlined, SearchOutlined, UserOutlined } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { logoutUsingPost } from '@/api/userController.ts'
import checkAccess from '@/access/checkAccess.ts'
import SearchArticleModal from '@/pages/home/SearchArticleModal.vue'
import type { MenuInfo } from 'ant-design-vue/es/menu/src/interface'

const current = ref<string[]>(['/'])
const router = useRouter()
const loginUserStore = useLoginUserStore()
const routes = router.getRoutes()
const open = ref<boolean>(false)

//路由跳转事件
const doMenuClick = (info: MenuInfo) => {
  router.push({ path: info.key as string })
}

const onLogin = () => {
  router.push({
    name: '用户登录',
    replace: true,
  })
}

//退出登录
const doLogout = async () => {
  const res = await logoutUsingPost()
  localStorage.removeItem('auth_data')
  loginUserStore.$reset()
  loginUserStore.setUserInfo({
    userInfo: {
      userName: '未登录',
      id: null,
      userAvatar: null,
    },
  })
  //清除用户信息
  message.success('退出成功')
  await router.push({
    // path: '/user/login',
    name: '用户登录',
    replace: true,
  })
}

//当前路由高亮
router.afterEach((to) => {
  current.value = [to.path]
})

// 把路由项转换为菜单项
const menuToRouteItem = (item: any) => {
  const hasChildren = item.children?.length > 0
  return {
    key: hasChildren ? undefined : item.path, // 有子菜单时不设置 key
    label: item.name,
    title: item.name,
    icon: item.meta?.icon ? h(item.meta.icon) : undefined, // 读取路由配置中的图标
    sort: item.meta?.sort ?? 0,
    children: hasChildren
      ? item.children.map((child: any) => ({
          key: child.path.startsWith('/') ? child.path : `${item.path}/${child.path}`,
          label: child.name,
          icon: child.meta?.icon ? h(child.meta.icon) : undefined, // 读取路由配置中的图标
        }))
      : undefined,
  }
}

// // 过滤菜单项
const items = computed(() => {
  return routes
    .filter((item) => {
      // 只保留没有父路由的顶级路由 或 显式声明要显示的父路由

      return (
        !item.meta?.hideInMenu && checkAccess(loginUserStore.loginUser, item.meta?.access as string)
      )
    })
    .map(menuToRouteItem)
    .sort((a, b) => (a.sort ?? 0) - (b.sort ?? 0))
})
const onSearch = () => {
  open.value = true
}
</script>

<style scoped>
#globalHeader {
  max-width: 100%;
  margin: 0 auto;
}

.user-login-status {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  max-width: 100px;
}

.username {
  white-space: nowrap; /* 防止文本换行 */
  overflow: hidden; /* 隐藏溢出的文本 */
  text-overflow: ellipsis; /* 显示省略号 */
  max-width: 100px; /* 设置最大宽度，根据需要调整 */
}
</style>
