<template>
  <!--  搜索-->
  <div>
    <a-form
      name="advanced_search"
      class="ant-advanced-search-form"
      :model="searchParams"
      @finish="onFinish"
    >
      <a-row :gutter="24">
        <a-col :span="4">
          <a-form-item :name="`id`" :label="`用户ID`">
            <a-input
              v-model:value="searchParams.id"
              :allowClear="true"
              placeholder="用户ID"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`userName`" :label="`用户名称`">
            <a-input
              v-model:value="searchParams.userName"
              :allowClear="true"
              placeholder="用户名称"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`userAccount`" :label="`用户账号`">
            <a-input
              v-model:value="searchParams.userAccount"
              :allowClear="true"
              placeholder="用户账号"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`phonenumber`" :label="`手机号码`">
            <a-input
              v-model:value="searchParams.phonenumber"
              :allowClear="true"
              placeholder="手机号码"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`email`" :label="`用户邮箱`">
            <a-input
              v-model:value="searchParams.email"
              :allowClear="true"
              placeholder="用户邮箱"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`userProfile`" :label="`用户简介`">
            <a-input
              v-model:value="searchParams.userProfile"
              :allowClear="true"
              placeholder="用户简介"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`remark`" :label="`用户备注`">
            <a-input
              v-model:value="searchParams.remark"
              :allowClear="true"
              placeholder="用户备注"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`sex`" :label="`用户性别`">
            <a-select v-model:value="searchParams.sex" :allowClear="true">
              <a-select-option value="0">男</a-select-option>
              <a-select-option value="1">女</a-select-option>
              <a-select-option value="2">未知</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`userRole`" :label="`用户角色`">
            <a-select v-model:value="searchParams.userRole" :allowClear="true">
              <a-select-option value="user">普通用户</a-select-option>
              <a-select-option value="admin">管理员</a-select-option>
              <a-select-option value="ban">非法用户</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="4">
          <a-button type="primary" @click="handleAdd()">新增</a-button>
        </a-col>
        <a-col :span="20" style="text-align: right">
          <a-button type="primary" html-type="submit">搜索</a-button>
          <a-button style="margin: 0 8px" @click="handleReset">重置</a-button>
        </a-col>
      </a-row>
    </a-form>
  </div>

  <!--  数据列表-->
  <div id="userAdminPage">
    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="false"
      :scroll="{ x: 3000 }"
      :expand-column-width="100"
      @change="onChangeSort"
      :locale="localeZh"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="60" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <span v-if="record.userRole === 'admin'"><a-tag color="green">管理员</a-tag></span>
          <span v-else-if="record.userRole === 'ban'"><a-tag color="volcano">非法用户</a-tag></span>
          <span v-else><a-tag color="geekblue">普通用户</a-tag></span>
        </template>
        <template v-if="column.dataIndex === 'sex'">
          <span v-if="record.sex === 0">男</span>
          <span v-else-if="record.sex === 1">女</span>
          <span v-else>未知</span>
        </template>
        <template v-else-if="column.key === 'action'">
          <span>
            <a-button type="primary" @click="handleEdit(record)">编辑</a-button>
            <a-divider type="vertical" />
            <a-button danger @click="handleDelete(record.id)">删除</a-button>
          </span>
        </template>
      </template>
    </a-table>
    <!--    分页-->
    <div class="pageModule">
      <a-pagination
        v-model:current="searchParams.pageNo"
        v-model:page-size="searchParams.pageSize"
        :total="total"
        show-size-changer
        @change="onChange"
        :showTotal="(total: number) => `共 ${total} 条`"
        :responsive="false"
      >
        <template #buildOptionText="props">
          <span v-if="props.value !== '50'">{{ props.value }}条/页</span>
          <span v-else>全部</span>
        </template>
      </a-pagination>
    </div>
  </div>
  <!-- 弹窗组件 -->
  <UserFormModal
    :visible="modalVisible"
    :user="editingUser"
    @update:visible="handleModalVisibleChange"
    @submit="handleSubmit"
  />
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import {
  addUserUsingPost,
  deleteUserUsingPost,
  editUserUsingPost,
  queryUserListUsingPost,
} from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import UserFormModal from '@/pages/admin/user/UserFormModal.vue'
import type { SorterResult } from 'ant-design-vue/es/table/interface'

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 100,
    fixed: true,
    sorter: true,
    align: 'center',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    width: 160,
    align: 'center',
    fixed: true,
  },
  {
    title: '用户昵称',
    dataIndex: 'userName',
    width: 160,
    align: 'center',
    ellipsis: true,
    fixed: true,
  },
  {
    title: '用户头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '用户简介',
    dataIndex: 'userProfile',
    width: 160,
    ellipsis: true,
  },
  {
    title: '用户邮箱',
    dataIndex: 'email',
    width: 160,
  },
  {
    title: '手机号码',
    dataIndex: 'phonenumber',
    width: 160,
  },
  {
    title: '当前登陆IP',
    dataIndex: 'loginIp',
    width: 160,
  },
  {
    title: '上一次登录IP',
    dataIndex: 'lastLoginIp',
    width: 160,
  },
  {
    title: '当前登陆时间',
    dataIndex: 'loginDate',
    width: 160,
    align: 'center',
    sorter: true,
  },
  {
    title: '上一次登录时间',
    dataIndex: 'lastLoginDate',
    width: 160,
    align: 'center',
    ellipsis: true,
    sorter: true,
  },
  {
    title: '用户性别',
    dataIndex: 'sex',
    width: 100,
    align: 'center',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    width: 100,
  },
  {
    title: '编辑时间',
    dataIndex: 'editTime',
    width: 160,
    align: 'center',
    sorter: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
    align: 'center',
    sorter: true,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 160,
    align: 'center',
    sorter: true,
  },
  {
    title: '创建者',
    dataIndex: 'createBy',
  },
  {
    title: '更新者',
    dataIndex: 'updateBy',
  },
  {
    title: '备注',
    dataIndex: 'remark',
    ellipsis: true,
  },
  {
    title: '操作',
    key: 'action',
    align: 'center',
    width: 200,
    fixed: 'right',
  },
]
// 定义数据
const dataList = ref<API.QueryUserVO[]>([])
// 总条数
const total = ref(0)
//分页
const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  searchParams.pageSize = pageSize
  handleSearch()
}
// 搜索条件
const searchParams = reactive<API.UserQueryDTO>({
  pageNo: 1,
  pageSize: 10,
  id: undefined,
  userAccount: '',
  userName: '',
  phonenumber: '',
  email: '',
  userProfile: '',
  remark: '',
  userRole: '',
  sex: undefined,
  sortField: 'createTime',
  sortOrder: 'asc',
})

// 查询
const handleSearch = async () => {
  const res = await queryUserListUsingPost({ ...searchParams })
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败！' + res.data.msg)
  }
}

const onChangeSort = async (
  pagination: unknown,
  filters: unknown,
  sorter: SorterResult<API.QueryUserVO> | SorterResult<API.QueryUserVO>[],
) => {
  if (Array.isArray(sorter)) return
  searchParams.sortField = sorter.field as string
  searchParams.sortOrder = sorter.order === 'ascend' ? 'asc' : 'desc'
  await handleSearch()
}
// const onChangeSort = async (pagination, filters, sorter, extra) => {
//   searchParams.sortField = sorter.field
//   if (sorter.order === 'ascend') {
//     searchParams.sortOrder = 'asc'
//   } else if (sorter.order === 'descend') {
//     searchParams.sortOrder = 'desc'
//   }
//   handleSearch()
// }

//删除
const handleDelete = async (id: number) => {
  const res = await deleteUserUsingPost({ id })
  if (res.data.code === 0) {
    message.success('删除成功！')
    handleSearch()
  } else {
    message.error('删除失败！' + res.data.msg)
  }
}

// 编辑
const editingUser = ref<API.EditUserDTO | null>(null)
const modalVisible = ref(false)

const handleEdit = (user: API.EditUserDTO) => {
  // //console.log('Editing user:', user)
  editingUser.value = { ...user }
  modalVisible.value = true
}

// 新增
const handleAdd = () => {
  editingUser.value = null
  modalVisible.value = true
}

// 弹窗显示状态变化
const handleModalVisibleChange = (visible: boolean) => {
  modalVisible.value = visible
}

// 表单提交
const handleSubmit = async (user: API.EditUserDTO) => {
  if (user.id) {
    // 编辑用户
    const res = await editUserUsingPost(user)
    if (res.data.code === 0) {
      message.success('编辑成功！')
      handleSearch()
    } else {
      message.error('编辑失败！' + res.data.msg)
    }
  } else {
    // 新增用户
    const res = await addUserUsingPost(user)
    if (res.data.code === 0) {
      message.success('新增成功！')
      handleSearch()
    } else {
      message.error('新增失败！' + res.data.msg)
    }
  }
  modalVisible.value = false
}

// 重置密码
// const handleResetPassword = async (id: number) => {
//   const res = await resetUserPasswordUsingPost({ id })
//   if (res.data.code === 0) {
//     message.success('重置密码成功！')
//   } else {
//     message.error('重置密码失败！' + res.data.msg)
//   }
// }

const onFinish = (values: any) => {
  searchParams.pageNo = 1
  handleSearch()
}

// 重置
const handleReset = () => {
  ;(searchParams.pageNo = 1),
    (searchParams.pageSize = 10),
    (searchParams.userAccount = ''),
    (searchParams.userName = ''),
    (searchParams.phonenumber = ''),
    (searchParams.email = ''),
    (searchParams.remark = ''),
    (searchParams.userRole = ''),
    (searchParams.sex = undefined),
    (searchParams.id = undefined)
}

// 生命周期钩子
onMounted(() => {
  handleSearch()
})

//语言包
const localeZh = {
  cancelSort: '取消排序',
  triggerAsc: '点击升序',
  triggerDesc: '点击降序',
}
</script>

<style scoped>
#userAdminPage {
  position: absolute;
  top: 250px;
  left: 20px;
  right: 20px;
  bottom: 110px;
  padding: 20px;
  background-color: #fff;
  overflow-y: auto; /* 当内容超出容器高度时，显示垂直滚动条 */
}

.pageModule {
  margin-top: 20px;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  padding: 16px;
  position: fixed;
  bottom: 63px;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
