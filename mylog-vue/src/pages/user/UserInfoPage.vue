<!--用户个人中心-->
<template>
  <div id="userInfoPage">
    <a-row :gutter="16">
      <a-col :xs="0" :md="6"></a-col>
      <a-col :xs="24" :md="4">
        <a-card hoverable>
          <template #actions>
            <!--            <setting-outlined key="setting" />-->
            <edit-outlined key="edit" class="edit-btn" @click="isEditModalVisible = true" />
          </template>
          <a-card-meta>
            <template #avatar>
              <div class="avatar-container">
                <a-avatar
                  class="large-avatar"
                  :src="loginUserStore.loginUser.userAvatar"
                  :style="loginUserStore.loginUser.userAvatar ? {} : { backgroundColor: '#1890ff' }"
                >
                  <template v-if="!loginUserStore.loginUser.userAvatar">
                    {{ (loginUserStore.loginUser.userName || '匿名').slice(0, 1) }}
                  </template>
                </a-avatar>
              </div>
              <h1 id="userinfoName">{{ loginUserStore.loginUser.userName || '匿名' }}</h1>
              <a-divider />
            </template>
            <template #description> <!-- 将简介移到description区域 -->
              <div class="user-intro">
                {{ loginUserStore.loginUser.userProfile || '暂无个人简介' }}
              </div>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :xs="24" :md="8">
        <a-spin :spinning="loading" tip="加载中..." size="large">
          <a-card title="我的收藏">
            <div v-for="item in listData" :key="item.id">
              <a-card
                class="card-item"
                :title="item.title"
                :style="{ marginTop: '16px' }"
                @click="toDetails(item.id!)"
              >
                {{ item.excerpt }}
              </a-card>
            </div>
            <a-divider />
            <a-pagination
              v-model:current="searchParams.pageNo"
              v-model:page-size="searchParams.pageSize"
              @change="onChange"
              :responsive="false"
              :total="total"
              show-less-items
            />
          </a-card>
        </a-spin>
      </a-col>
      <a-col :xs="0" :md="7"></a-col>
    </a-row>
    <!-- 编辑用户信息弹窗 -->
    <a-modal
      v-model:visible="isEditModalVisible"
      title="编辑个人信息"
      @ok="handleUpdate"
      :confirm-loading="saving"
      ok-text="提交"
      cancel-text="取消"
    >
      <a-form :model="formData" layout="vertical">
        <a-form-item label="用户名" required>
          <a-input v-model:value="formData.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="上传头像">
          <a-upload
            list-type="picture-card"
            :customRequest="customUpload"
            :before-upload="beforeUpload"
            :show-upload-list="false"
          >
            <img
              v-if="imageUrl"
              :src="imageUrl"
              alt="avatar"
              style="max-width: 100%; height: auto"
            />
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">Upload</div>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item label="简介">
          <a-input v-model:value="formData.userProfile" placeholder="请输入简介" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { EditOutlined } from '@ant-design/icons-vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { queryMyCollectUsingPost } from '@/api/homeController.ts'
import { message } from 'ant-design-vue'
import { onMounted, reactive, ref } from 'vue'
import router from '@/router'
import { updateUserUsingPost } from '@/api/userController.ts'
import myAxios from '@/request.ts'
import { baseUrl, uploadUrl } from '@/config/config.ts'

const loginUserStore = useLoginUserStore()
//loading 状态
const loading = ref(false)
// 查询数据
const listData = ref<API.QueryMyCollectVO[]>([])
// 总条数
const total = ref(0)
// 搜索条件
const searchParams = reactive<API.QueryMyCollectDTO>({
  pageNo: 1,
  pageSize: 5,
})
// 控制弹窗显示
const isEditModalVisible = ref(false)
// 提交状态
const saving = ref(false)
// 表单数据（预填当前用户信息）
const formData = reactive<API.UpdateUserDTO>({
  id: loginUserStore.loginUser.id,
  userName: loginUserStore.loginUser.userName || '',
  userAvatar: loginUserStore.loginUser.userAvatar || '',
  userProfile: loginUserStore.loginUser.userProfile || '',
})

const imageUrl = ref<string>('')

//上传校验
const beforeUpload = (file: File) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('只能上传 JPG 或 PNG 文件!')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('图片必须小于 2MB!')
  }
  return isJpgOrPng && isLt2M
}
// 查看收藏
const queryMyCollect = async () => {
  try {
    loading.value = true
    const res = await queryMyCollectUsingPost({ ...searchParams })
    if (res.data.code === 0) {
      listData.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    }
  } catch (error) {
    message.error('加载收藏数据失败')
  } finally {
    loading.value = false
  }
}

//分页
const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  searchParams.pageSize = pageSize
  queryMyCollect()
}
//跳转文章详情
const toDetails = (id: number) => {
  if (id !== undefined) {
    router.push({ name: '文章详情', params: { id } })
  } else {
    message.error('ID 无效，无法跳转')
  }
}
//修改用户信息
const handleUpdate = async () => {
  try {
    saving.value = true
    // 调用接口更新用户信息
    const res = await updateUserUsingPost({ ...formData })
    if (res.data.code === 0) {
      // 更新 Store 中的用户数据
      loginUserStore.setUserInfo({
        userInfo: {
          id: loginUserStore.loginUser.id,
          userRole: loginUserStore.loginUser.userRole,
          isLoggedIn: loginUserStore.loginUser.isLoggedIn,
          userName: formData.userName,
          userAvatar: formData.userAvatar,
          userProfile: formData.userProfile,
        },
      })
      message.success('修改成功')
      isEditModalVisible.value = false // 关闭弹窗
    } else {
      message.error(res.data.message)
    }
  } catch (error) {
    message.error('请求失败，请重试')
  } finally {
    saving.value = false
  }
}
// 生命周期钩子
onMounted(() => {
  queryMyCollect()
})
//上传地址
const actionUrl = baseUrl + uploadUrl
//上传操作
const customUpload = async (options: any) => {
  const { file, onProgress, onSuccess, onError } = options
  try {
    const fileData = new FormData()
    fileData.append('file', file)
    const response = await myAxios.post(actionUrl, fileData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress: (progressEvent) => {
        const percent =
          progressEvent.total && progressEvent.total > 0
            ? (progressEvent.loaded / progressEvent.total) * 100
            : 0
        onProgress({ percent })
      },
    })
    if (response.data.code === 0) {
      formData.userAvatar = response.data.data.fileUrl
      imageUrl.value = response.data.data.fileUrl
      onSuccess(response.data.data)
      message.success('头像上传成功')
    } else {
      throw new Error(response.data.msg)
    }
  } catch (error) {
    onError(error)
  }
}
</script>
<style scoped>
#userInfoPage {
}

#userInfoPage .avatar-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中（新增）*/
  margin: 20px 0;
  height: 100%; /* 确保容器占满父元素高度（可选） */
}

#userInfoPage .large-avatar {
  width: 120px !important;
  height: 120px !important;
  font-size: 36px !important;
  display: flex !important; /* 强制flex布局 */
  justify-content: center !important; /* 内容水平居中 */
  align-items: center !important; /* 内容垂直居中 */
  line-height: initial !important; /* 移除默认行高影响 */
}

#userInfoPage :where(.css-dev-only-do-not-override-1p3hq3p).ant-card .ant-card-meta {
  margin: -4px 0;
  display: flex;
  flex-direction: column;
}

#userInfoPage #userinfoName {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;
  font-size: 24px;
}

#userInfoPage .card-item {
  transition: background-color 0.3s; /* 添加过渡动画 */
}

#userInfoPage .card-item:hover {
  background-color: #f0f0f0; /* 悬停背景色 */
  cursor: pointer; /* 显示手型光标 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 悬停阴影 */
}
.user-intro {
  text-align: center;
  margin: 16px 0; /* 上下间距 */
  font-size: 14px;
  color: #666;
}
</style>
