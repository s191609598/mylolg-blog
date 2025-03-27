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
          <a-form-item :name="`id`" :label="`ID`">
            <a-input v-model:value="searchParams.id" :allowClear="true" placeholder="ID"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`name`" :label="`名称`">
            <a-input
              v-model:value="searchParams.name"
              :allowClear="true"
              placeholder="名称"
            ></a-input>
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
  <div id="TagAdminPage">
    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="false"
      :scroll="{ x: 1000 }"
      :expand-column-width="100"
      @change="onChangeSort"
      :locale="localeZh"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
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
  <TagFormModal
    :visible="modalVisible"
    :tag="editingTag"
    @update:visible="handleModalVisibleChange"
    @submit="handleSubmit"
  />
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import {
  addTagUsingPost,
  deleteTagByIdUsingPost,
  queryTagPageListUsingPost,
  updateTagUsingPost,
} from '@/api/tagController.ts'
import TagFormModal from '@/pages/admin/tag/TagFormModal.vue'
import type { SorterResult } from 'ant-design-vue/es/table/interface';

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 50,
    sorter: true,
    align: 'center',
  },
  {
    title: '名称',
    dataIndex: 'name',
    width: 100,
    align: 'center',
  },
  {
    title: '排序',
    dataIndex: 'sort',
    width: 50,
    align: 'center',
    sorter: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 100,
    align: 'center',
    sorter: true,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 100,
    align: 'center',
    sorter: true,
  },
  {
    title: '操作',
    key: 'action',
    align: 'center',
    width: 100,
    fixed: 'right',
  },
]
// 定义数据
const dataList = ref<API.TagPageListVO[]>([])
// 总条数
const total = ref(0)
//分页
const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  searchParams.pageSize = pageSize
  handleSearch()
}
// 搜索条件
const searchParams = reactive<API.CategoryPageListDTO>({
  pageNo: 1,
  pageSize: 10,
  id: undefined,
  name: '',
  sortField: 'createTime',
  sortOrder: 'asc',
})

// 查询
const handleSearch = async () => {
  const res = await queryTagPageListUsingPost({ ...searchParams })
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
  sorter: SorterResult<API.TagPageListVO> | SorterResult<API.TagPageListVO>[]
) => {
  if (Array.isArray(sorter)) {
    // 多列排序取第一个排序字段（根据业务需求调整）
    const currentSorter = sorter[0]
    searchParams.sortField = currentSorter?.field?.toString()
    searchParams.sortOrder = currentSorter?.order === 'ascend' ? 'asc' : 'desc'
  } else {
    const singleSorter = sorter as SorterResult<API.TagPageListVO>
    searchParams.sortField = singleSorter?.field?.toString()
    searchParams.sortOrder = singleSorter?.order === 'ascend' ? 'asc' : 'desc'
  }
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
  const res = await deleteTagByIdUsingPost({ id })
  if (res.data.code === 0) {
    message.success('删除成功！')
    handleSearch()
  } else {
    message.error('删除失败！' + res.data.msg)
  }
}

// 编辑
const editingTag = ref<API.EditTagDTO | null>(null)
const modalVisible = ref(false)

const handleEdit = (dto: API.EditTagDTO) => {
  editingTag.value = { ...dto }
  modalVisible.value = true
}

// 新增
const handleAdd = () => {
  editingTag.value = null
  modalVisible.value = true
}

// 弹窗显示状态变化
const handleModalVisibleChange = (visible: boolean) => {
  modalVisible.value = visible
}

// 表单提交
const handleSubmit = async (dto: API.EditTagDTO) => {
  if (dto.id) {
    // 编辑
    const res = await updateTagUsingPost(dto)
    if (res.data.code === 0) {
      clearDto(dto)
      message.success('编辑成功！')
      handleSearch()
    } else {
      message.error('编辑失败！' + res.data.msg)
    }
  } else {
    // 新增
    const res = await addTagUsingPost(dto)
    if (res.data.code === 0) {
      clearDto(dto)
      message.success('新增成功！')
      handleSearch()
    } else {
      message.error('新增失败！' + res.data.msg)
    }
  }
  modalVisible.value = false
}

const onFinish = (values: any) => {
  searchParams.pageNo = 1
  handleSearch()
}

// 重置
const handleReset = () => {
  ;(searchParams.pageNo = 1),
    (searchParams.pageSize = 10),
    (searchParams.name = ''),
    (searchParams.id = undefined)
}

const clearDto = (dto: API.EditTagDTO) => {
  dto.id = undefined
  dto.name = ''
  dto.sort = undefined
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
#TagAdminPage {
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
