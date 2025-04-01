import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import UserloginPage from '@/pages/user/UserloginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserAdminPage from '@/pages/admin/user/UserAdminPage.vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import NoAuth from '@/pages/noauth/NoAuth.vue'
import ArticleAdminPage from '@/pages/admin/article/ArticleAdminPage.vue'
import ArticleDetailsPage from '@/pages/home/ArticleDetailsPage.vue'
import TagAdminPage from '@/pages/admin/tag/TagAdminPage.vue'
import CategoryAdminPage from '@/pages/admin/category/CategoryAdminPage.vue'
import ArticleTagList from '@/pages/article/ArticleTagList.vue'
import ArticleCategoryList from '@/pages/article/ArticleCategoryList.vue'
import { HomeOutlined,FileTextOutlined,SettingOutlined,TagsOutlined,FolderOpenOutlined } from '@ant-design/icons-vue'

//路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: '主页',
      component: HomePage,
      meta: {
        sort: 0,
        icon: HomeOutlined
      },
    },
    {
      path: '/article',
      name: '文章',
      meta: {
        sort: 1,
        icon: FileTextOutlined // 新增图标
      },
      children: [
        {
          path: 'articletaglist',
          name: '标签',
          component: ArticleTagList,
          meta: {
            sort: 2,
            hideInMenu: true, // 子菜单项不需要单独出现在菜单中
            icon: TagsOutlined,

          },
        },
        {
          path: 'articlecategorylist',
          name: '类型',
          component: ArticleCategoryList,
          meta: {
            sort: 3,
            hideInMenu: true, // 子菜单项不需要单独出现在菜单中
            icon: FolderOpenOutlined,
          },
        },
      ],
    },

    {
      path: '/user/login',
      name: '用户登录',
      component: UserloginPage,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/user/register',
      name: '用户注册',
      component: UserRegisterPage,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/home/articledetails/:id',
      name: '文章详情',
      component: ArticleDetailsPage,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/admin',
      name: '后台管理',
      meta: {
        access: ACCESS_ENUM.ADMIN,
        sort: 10,
        icon: SettingOutlined // 新增图标
      },
      children: [
        {
          path: 'user/useradmin',
          name: '用户管理',
          component: UserAdminPage,
          meta: {
            access: ACCESS_ENUM.ADMIN,
            sort: 11,
            hideInMenu: true,
          },
        },
        {
          path: 'article/articleadmin',
          name: '文章管理',
          component: ArticleAdminPage,
          meta: {
            access: ACCESS_ENUM.ADMIN,
            sort: 12,
            hideInMenu: true,
          },
        },
        {
          path: 'tag/tagadmin',
          name: '标签管理',
          component: TagAdminPage,
          meta: {
            access: ACCESS_ENUM.ADMIN,
            sort: 13,
            hideInMenu: true,
          },
        },
        {
          path: 'category/categoryadmin',
          name: '类型管理',
          component: CategoryAdminPage,
          meta: {
            access: ACCESS_ENUM.ADMIN,
            sort: 14,
            hideInMenu: true,
          },
        },
      ],
    },

    {
      path: '/about',
      name: '关于',
      component: () => import('../pages/AboutView.vue'),
      meta: {
        sort: 9,
      },
    },
    {
      path: '/noauth/noauth',
      name: '没有权限',
      component: NoAuth,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/article/tags',
      name: 'ArticleTags',
      component: () => import('@/pages/article/ArticleTagList.vue'),
      meta: {
        hideInMenu: true,
      },
    }
  ],
})

export default router
