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
          <a-form-item :name="`id`" :label="`文章ID`">
            <a-input
              v-model:value="searchParams.id"
              :allowClear="true"
              placeholder="文章ID"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`title`" :label="`标题`">
            <a-input
              v-model:value="searchParams.title"
              :allowClear="true"
              placeholder="标题"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`content`" :label="`文章内容`">
            <a-input
              v-model:value="searchParams.content"
              :allowClear="true"
              placeholder="文章内容"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :name="`excerpt`" :label="`摘要`">
            <a-input
              v-model:value="searchParams.excerpt"
              :allowClear="true"
              placeholder="摘要"
            ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="2">
          <a-form-item :name="`articleType`" :label="`文章类别`">
            <a-select v-model:value="searchParams.articleType" :allowClear="true">
              <a-select-option value="0">原创</a-select-option>
              <a-select-option value="1">转载</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="2">
          <a-form-item :name="`state`" :label="`状态`">
            <a-select v-model:value="searchParams.state" :allowClear="true">
              <a-select-option value="0">公开</a-select-option>
              <a-select-option value="1">会员查看</a-select-option>
              <a-select-option value="2">私密</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="2">
          <a-form-item :name="`isTop`" :label="`是否置顶`">
            <a-select v-model:value="searchParams.isTop" :allowClear="true">
              <a-select-option value="0">否</a-select-option>
              <a-select-option value="1">是</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="2">
          <a-form-item :name="`isRecommend`" :label="`是否推荐`">
            <a-select v-model:value="searchParams.isRecommend" :allowClear="true">
              <a-select-option value="0">否</a-select-option>
              <a-select-option value="1">是</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="2">
          <a-form-item :name="`isCarousel`" :label="`是否轮播图`">
            <a-select v-model:value="searchParams.isCarousel" :allowClear="true">
              <a-select-option value="0">否</a-select-option>
              <a-select-option value="1">是</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :label="`阅读数量`">
            <a-input-group compact>
              <a-input
                v-model:value="searchParams.minReadNum"
                style="width: 100px; text-align: center"
                placeholder="Minimum"
              />
              <a-input
                class="site-input-split"
                style="width: 30px; border-left: 0; pointer-events: none"
                placeholder="~"
                disabled
              />
              <a-input
                v-model:value="searchParams.maxReadNum"
                class="site-input-right"
                style="width: 100px; text-align: center"
                placeholder="Maximum"
              />
            </a-input-group>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :label="`点赞数量`">
            <a-input-group compact>
              <a-input
                v-model:value="searchParams.minUpNum"
                style="width: 100px; text-align: center"
                placeholder="Minimum"
              />
              <a-input
                class="site-input-split"
                style="width: 30px; border-left: 0; pointer-events: none"
                placeholder="~"
                disabled
              />
              <a-input
                v-model:value="searchParams.maxUpNum"
                class="site-input-right"
                style="width: 100px; text-align: center"
                placeholder="Maximum"
              />
            </a-input-group>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item :label="`评论数量`">
            <a-input-group compact>
              <a-input
                v-model:value="searchParams.minCommentNum"
                style="width: 100px; text-align: center"
                placeholder="Minimum"
              />
              <a-input
                class="site-input-split"
                style="width: 30px; border-left: 0; pointer-events: none"
                placeholder="~"
                disabled
              />
              <a-input
                v-model:value="searchParams.maxCommentNum"
                class="site-input-right"
                style="width: 100px; text-align: center"
                placeholder="Maximum"
              />
            </a-input-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="4">
          <a-button type="primary" @click.stop="handleAdd()">新增</a-button>
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
        <template v-if="column.dataIndex === 'cover'">
          <a-image :src="record.cover" :width="60" />
        </template>
        <template v-else-if="column.dataIndex === 'state'">
          <span v-if="record.state === 1"><a-tag color="green">会员查看</a-tag></span>
          <span v-else-if="record.state === 2"><a-tag color="volcano">私密</a-tag></span>
          <span v-else><a-tag color="geekblue">公开</a-tag></span>
        </template>
        <template v-if="column.dataIndex === 'articleType'">
          <span v-if="record.articleType === 0">原创</span>
          <span v-else-if="record.articleType === 1">转载</span>
        </template>
        <template v-if="column.dataIndex === 'isTop'">
          <span v-if="record.isTop === 0">否</span>
          <span v-else-if="record.isTop === 1">是</span>
        </template>
        <template v-if="column.dataIndex === 'isRecommend'">
          <span v-if="record.isRecommend === 0">否</span>
          <span v-else-if="record.isRecommend === 1">是</span>
        </template>
        <template v-if="column.dataIndex === 'isCarousel'">
          <span v-if="record.isCarousel === 0">否</span>
          <span v-else-if="record.isCarousel === 1">是</span>
        </template>
        <template v-else-if="column.dataIndex === 'tags'">
          <span>
            <a-tag v-for="tag in record.tags" :key="tag">
              {{ tag.toUpperCase() }}
            </a-tag>
          </span>
        </template>
        <template v-else-if="column.key === 'action'">
          <span>
            <a-button
              :size="size"
              type="primary"
              v-show="record.state !== 0"
              @click="handleUpdateStatus(record.id, 0)"
              >公开</a-button
            >
            <a-divider type="vertical" v-show="record.state !== 0" />
            <a-button
              :size="size"
              type="dashed"
              v-show="record.state !== 2"
              @click="handleUpdateStatus(record.id, 2)"
              >私密</a-button
            >
            <a-divider type="vertical" v-show="record.state !== 2" />
            <a-button
              :size="size"
              type="dashed"
              v-show="record.state !== 1"
              @click="handleUpdateStatus(record.id, 1)"
              >会员查看</a-button
            >
            <a-divider type="vertical" v-show="record.state !== 1" />
            <a-button
              :size="size"
              type="primary"
              v-show="record.state === 2"
              @click.stop="handleEdit(record.id)"
              >编辑</a-button
            >
            <a-divider type="vertical" v-show="record.state === 2" />
            <a-button
              :size="size"
              v-show="record.state === 2"
              danger
              @click="handleDelete(record.id)"
              >删除</a-button
            >
<!--            <a-button-->
<!--              :size="size"-->
<!--              type="primary"-->
<!--              v-show="record.state !== 2"-->
<!--              @click="handleExamine(record.id)"-->
<!--              >查看</a-button-->
<!--            >-->
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
  <ArticleFormModal
    :open="modalVisible"
    :article="editingArticle"
    @update:open="handleModalVisibleChange"
    @submit="handleSubmit"
  />
  <ArticleExamineModal
    :open="examineVisible"
    :articleId="articleId"
    @update:open="handleModalExamineVisibleChange"
  />
</template>
<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import {
  addArticleUsingPost,
  deleteArticleUsingPost,
  getArticleByIdUsingGet,
  queryArticleListUsingPost,
  updateArticleStatusUsingPost,
  updateArticleUsingPost,
} from '@/api/articleController.ts'
import ArticleFormModal from '@/pages/admin/article/ArticleFormModal.vue'
import ArticleExamineModal from '@/pages/admin/article/ArticleExamineModal.vue'
import type { SorterResult, TablePaginationConfig } from 'ant-design-vue/es/table/interface'

const size = ref('small')

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 100,
    fixed: true,
    align: 'center',
    sorter: true,
  },
  {
    title: '标题',
    dataIndex: 'title',
    width: 160,
    align: 'center',
    ellipsis: true,
  },
  {
    title: '文章类型',
    dataIndex: 'categoryName',
    width: 120,
    align: 'center',
    ellipsis: true,
  },
  {
    title: '文章内容',
    dataIndex: 'content',
    width: 160,
    ellipsis: true,
  },
  {
    title: '文章封面',
    dataIndex: 'cover',
    width: 160,
    ellipsis: true,
  },
  {
    title: '摘要',
    dataIndex: 'excerpt',
    width: 160,
    ellipsis: true,
  },
  {
    title: '状态',
    dataIndex: 'state',
    width: 160,
  },
  {
    title: '文章类别',
    dataIndex: 'articleType',
    width: 160,
  },
  {
    title: '是否置顶',
    dataIndex: 'isTop',
    width: 160,
  },
  {
    title: '是否推荐',
    dataIndex: 'isRecommend',
    width: 160,
    align: 'center',
    ellipsis: true,
  },
  {
    title: '是否轮播图',
    dataIndex: 'isCarousel',
    width: 120,
    align: 'center',
  },
  {
    title: '轮播排序',
    dataIndex: 'sort',
    width: 100,
  },
  {
    title: '转载地址',
    dataIndex: 'reprintUrl',
    width: 160,
    align: 'center',
  },
  {
    title: '阅读数量',
    dataIndex: 'readNum',
    width: 160,
    align: 'center',
    sorter: true,
  },
  {
    title: '点赞数量',
    dataIndex: 'upNum',
    width: 160,
    align: 'center',
    sorter: true,
  },
  {
    title: '评论数量',
    width: 160,
    align: 'center',
    dataIndex: 'commentNum',
    sorter: true,
  },
  {
    title: '收藏数量',
    width: 160,
    align: 'center',
    dataIndex: 'collectNum',
    sorter: true,
  },
  {
    title: '编辑时间',
    width: 160,
    align: 'center',
    dataIndex: 'editTime',
    sorter: true,
  },
  {
    title: '创建时间',
    width: 160,
    align: 'center',
    dataIndex: 'createTime',
    sorter: true,
  },
  {
    title: '更新时间',
    width: 160,
    align: 'center',
    dataIndex: 'updateTime',
    sorter: true,
  },
  {
    title: '标签',
    dataIndex: 'tags',
    align: 'center',
    width: 380,
    ellipsis: true,
  },
  {
    title: '操作',
    key: 'action',
    align: 'center',
    width: 300,
    fixed: 'right',
  },
]
// 查询数据
const dataList = ref<API.QueryArticleVO[]>([])
// 总条数
const total = ref(0)
//分页
const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  searchParams.pageSize = pageSize
  handleSearch()
}
// 搜索条件
const searchParams = reactive<API.QueryArticleDTO>({
  pageNo: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'desc',
  id: undefined,
  articleType: undefined,
  categoryId: undefined,
  content: '',
  excerpt: '',
  isCarousel: undefined,
  isRecommend: undefined,
  isTop: undefined,
  maxCommentNum: undefined,
  maxReadNum: undefined,
  maxUpNum: undefined,
  minCommentNum: undefined,
  minReadNum: undefined,
  minUpNum: undefined,
  state: undefined,
  title: '',
})

// 查询
const handleSearch = async () => {
  const res = await queryArticleListUsingPost({ ...searchParams })
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败！' + res.data.msg)
  }
}
const onChangeSort = (
  pagination: TablePaginationConfig,
  filters: Record<string, any>,
  sorter: SorterResult<API.QueryArticleVO> | SorterResult<API.QueryArticleVO>[]
) => {
  if (Array.isArray(sorter)) return // 处理多列排序情况
  searchParams.sortField = sorter?.field?.toString()
  searchParams.sortOrder = sorter?.order === 'ascend' ? 'asc' : 'desc'
  handleSearch()
}

//删除
const handleDelete = async (id: number) => {
  const res = await deleteArticleUsingPost({ id })
  if (res.data.code === 0) {
    message.success('删除成功！')
    handleSearch()
  } else {
    message.error('删除失败！' + res.data.msg)
  }
}
//查看文章详情
const handleExamine = async (id: number) => {
  articleId.value = id
  examineVisible.value = true
}

// 编辑
const editingArticle = ref<API.EditArticleDTO | null>(null)
const modalVisible = ref(false)
const examineVisible = ref(false)
const articleId = ref<number>(0)

const handleEdit = async (id: number) => {
  try {
    const loading = message.loading('正在加载文章数据...', 0)
    const res = await getArticleByIdUsingGet({ id: id })
    loading()
    if (res.data.code === 0 && res.data.data) {
      editingArticle.value = {
        ...res.data.data,
        // 根据接口返回数据格式可能需要转换
        state: String(res.data.data.state),
        articleType: String(res.data.data.articleType),
        tags: res.data.data.tags?.map(String),
      }
      modalVisible.value = true
    } else {
      message.error('获取文章失败：' + res.data.msg)
    }
  } catch (error) {
    const errorMessage = error instanceof Error
      ? error.message
      : typeof error === 'string'
        ? error
        : '未知错误'
    message.error('操作失败: ' + errorMessage)
  }
}

const handleUpdateStatus = async (id: number, status: number) => {
  const res = await updateArticleStatusUsingPost({ id, status })
  if (res.data.code === 0) {
    message.success('更新状态成功！')
    handleSearch()
  } else {
    message.error('更新状态失败！' + res.data.msg)
  }
}

// 新增
const handleAdd = () => {
  editingArticle.value = null
  modalVisible.value = true
}

// 弹窗显示状态变化

// 修改所有状态更新处
const handleModalVisibleChange = (visible: boolean) => {
  nextTick(() => {
    modalVisible.value = visible
  })
}

const handleModalExamineVisibleChange = (visible: boolean) => {
  nextTick(() => {
    examineVisible.value = visible
  })
}

// 表单提交
const handleSubmit = async (article: API.EditArticleDTO) => {
  const loading = message.loading('提交中...', 0)
  try {
    if (article.id) {
      // 编辑文章
      const res = await updateArticleUsingPost(article)
      if (res.data.code === 0) {
        message.success('编辑成功！')
        handleSearch()
      } else {
        message.error('编辑失败！' + res.data.msg)
      }
    } else {
      // 新增文章
      const res = await addArticleUsingPost(article)
      if (res.data.code === 0) {
        message.success('新增成功！')
        handleSearch()
      } else {
        message.error('新增失败！' + res.data.msg)
      }
    }
  } catch (error) {
    const errorMessage = error instanceof Error
      ? error.message
      : typeof error === 'string'
        ? error
        : '未知错误'
    message.error('操作失败: ' + errorMessage)
  } finally {
    loading()
    // 确保在异步操作完成后才关闭弹窗
    modalVisible.value = false
  }
}

const onFinish = (values: any) => {
  searchParams.pageNo = 1
  handleSearch()
}

// 重置
const handleReset = () => {
  ;(searchParams.pageNo = 1),
    (searchParams.pageSize = 10),
    (searchParams.sortField = 'createTime'),
    (searchParams.sortOrder = 'asc'),
    (searchParams.id = undefined),
    (searchParams.title = ''),
    (searchParams.content = ''),
    (searchParams.excerpt = ''),
    (searchParams.articleType = undefined),
    (searchParams.categoryId = undefined),
    (searchParams.isCarousel = undefined),
    (searchParams.isRecommend = undefined),
    (searchParams.isTop = undefined),
    (searchParams.maxCommentNum = undefined),
    (searchParams.maxReadNum = undefined),
    (searchParams.maxUpNum = undefined),
    (searchParams.minCommentNum = undefined),
    (searchParams.minReadNum = undefined),
    (searchParams.minUpNum = undefined),
    (searchParams.state = undefined)
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
