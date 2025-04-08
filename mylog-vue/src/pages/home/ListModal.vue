<!--首页-文章列表-->
<template>
  <div id="listModal">
    <a-row>
      <a-col :xs="0" />
      <a-col :xs="24" :sm="20" :md="18" :lg="16" :xl="24" >
        <a-list
          :loading="loading"
          item-layout="vertical"
          size="large"
          :pagination="false"
          :data-source="listData"
        >
          <template #renderItem="{ item }">
            <a-space direction="vertical">
              <a-card
                id="listCardPage"
                :bordered="true"
                :hoverable="true"
                class="card-container"
                @click="handleCardClick(item.id)"
              >
                <a-list-item>
                  <template #extra>
                    <div class="hidden-xs">
                      <img
                        alt="cover"
                        :src="item.cover"
                        :onerror="setDefaultImage"
                        class="cover-image"
                      />
                    </div>
                  </template>
                  <a-list-item-meta>
                    <template #title>
                      <div class="title-container">
                        <a-tag
                          v-if="route.path === '/' && item.isTop === 1"
                          color="error"
                          class="top-tag"
                        >
                          <PushpinOutlined />
                          置顶
                        </a-tag>
                        <span class="article-title">{{ item.title }}</span>
                      </div>
                    </template>
                  </a-list-item-meta>
                  <div class="excerpt">{{ item.excerpt }}</div>
                  <template #actions>
                    <span v-for="{ icon, text } in item.actions" :key="icon">
                      <component :is="icon" class="action-icon" />
                      {{ text }}
                    </span>
                  </template>
                </a-list-item>
              </a-card>
            </a-space>
          </template>
        </a-list>

        <a-divider />
        <div class="pageModule" >
          <a-pagination
            v-model:current="searchParams.pageNo"
            v-model:page-size="searchParams.pageSize"
            :total="total"
            @change="onChange"
            :responsive="true"
          />
        </div>
      </a-col>
      <a-col :xs="0" />
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { EyeOutlined, LikeOutlined, MessageOutlined, PushpinOutlined } from '@ant-design/icons-vue'
import { onMounted, reactive, ref, watch } from 'vue'
import { searchFromEsUsingPost } from '@/api/homeController.ts'
import { message } from 'ant-design-vue'
import router from '@/router'
import { useRoute } from 'vue-router'
import { debounce } from 'lodash-es'
import imgerror from '@/assets/imgERROR.jpg'

const props = defineProps<{
  tagId?: number | string
  categoryId?: number | string
}>()

//loading 状态
const loading = ref(false)

// 新增路由参数获取逻辑
const route = useRoute()
// 查询数据
const listData = ref<API.HomeArticleVO[]>([])
// 总条数
const total = ref(0)
// 搜索条件
const searchParams = reactive<API.SearchArticleByKeywordDTO>({
  pageNo: 1,
  pageSize: 4,
  showTop: undefined, // 是否显示置顶
  tagId: undefined, // 标签ID
  categoryId: undefined, // 类型ID
})

// 查询
const handleSearch = debounce(async () => {
  try {
    loading.value = true
    const res = await searchFromEsUsingPost({
      ...searchParams,
      showTop: route.path === '/' ? true : false,
    })
    if (res.data.code === 0 && res.data.data) {
      listData.value = res.data.data.records.map((record: API.HomeArticleVO) => ({
        ...record,
        actions: [
          { icon: EyeOutlined, text: record.readNum },
          { icon: LikeOutlined, text: record.upNum },
          { icon: MessageOutlined, text: record.commentNum },
        ],
      }))
      total.value = res.data.data.total ?? 0
    } else {
      message.error('获取数据失败！' + res.data.msg)
    }
  } catch (error) {
    message.error('请求异常：' + (error as Error).message)
  } finally {
    loading.value = false
  }
}, 300)

// 监听props变化
watch(
  () => [props.tagId, props.categoryId],
  ([newTagId, newCategoryId]) => {
    // 类型转换处理
    searchParams.tagId = typeof newTagId === 'string' ? parseInt(newTagId) : newTagId
    searchParams.categoryId =
      typeof newCategoryId === 'string' ? parseInt(newCategoryId) : newCategoryId
    // 重置页码到第一页
    searchParams.pageNo = 1
    handleSearch()
  },
  { immediate: true },
)

// 生命周期钩子
onMounted(() => {
  handleSearch()
})

//分页
const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  searchParams.pageSize = pageSize
  handleSearch()
}

// 设置默认图片
const setDefaultImage = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = imgerror // 替换为你的默认图片路径
  img.onerror = null // 避免无限循环
}

// 处理卡片点击事件
const handleCardClick = (id: number) => {
  router.push({ name: '文章详情', params: { id } })
}
</script>

<style scoped>
#listCardPage {
  margin: 14px 5px;
  width: 1000px;
  max-width: 100%;
  box-sizing: border-box;
}

@media (max-width: 1920px) {
  #listCardPage {
    margin: 14px 5px;
    max-width: 756px;
    box-sizing: border-box;
  }
}

/* 移动端适配 */
@media (max-width: 576px) {
  :deep(.ant-list-item-action) {
    display: flex !important;
    justify-content: space-between !important; /* 均匀分布 */
    padding: 0 16px !important; /* 增加边距 */
    margin-top: 8px !important;

    > li {
      flex: 1; /* 等宽分布 */
      text-align: center; /* 文字居中 */
      margin: 0 !important;

      > span {
        display: flex;
        flex-direction: column; /* 垂直排列 */
        align-items: center; /* 水平居中 */
        gap: 4px; /* 图标文字间距 */
      }
    }
  }

  /* 调整图标大小 */
  .action-icon {
    font-size: 16px !important; /* 放大图标 */
    margin-right: 0 !important; /* 移除原有右边距 */
  }

  /* 禁用所有文本选中 */
  .card-container {
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  /* 移除点击态背景 */
  :deep(.ant-list-item) {
    background: transparent !important;
  }

  /* 禁用点击波纹效果 */
  :deep(.ant-card):active {
    -webkit-tap-highlight-color: transparent;
  }

  /* 移除点击动画 */
  .card-container {
    transition: none !important;
  }

  /* 保留原有其他移动端样式 */
  #listCardPage {
    width: 100%;
    margin: 12px 0;
    padding: 12px !important;
  }

  .article-title {
    font-size: 18px !important;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    max-height: 2.8em;
    text-align: left;
  }

  .excerpt {
    font-size: 14px;
    color: #666;
    margin-top: 8px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    max-height: 4.2em;
  }

  .top-tag {
    margin-right: 8px;
    margin-bottom: 4px;
  }

  .action-icon {
    margin-right: 6px;
    font-size: 14px !important;
  }

  .pageModule {
    padding: 16px 0;
  }

  .ant-list-item {
    display: flex;
    flex-direction: column;
    width: 100% !important;
    min-height: 120px;
    justify-content: space-between;
  }

  .ant-list-item-action {
    margin-top: 12px;
    width: 100%;
    justify-content: space-around;
    font-size: 12px;
  }

  .title-container {
    justify-content: flex-start;
  }
}

/* 通用样式 */
.title-container {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  flex-wrap: wrap;
  justify-content: center;
}

.article-title {
  font-weight: 600 !important;
  text-align: center;
  font-size: 22px;
}

#listCardPage .pageModule {
  text-align: center; /* 新增居中属性 */
  overflow-x: visible; /* 修复可能存在的滚动条问题 */
  padding: 20px 0;
}
/* 精准定位分页组件 */
.pageModule :deep(.ant-pagination) {
  display: inline-block; /* 关键属性 */
  margin: 0 auto;
}
.cover-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.hidden-xs {
  display: none;
}

@media (min-width: 576px) {
  .hidden-xs {
    display: block;
  }
}

.card-container {
  width: 100%;
  height: 100%;
}

.ant-list-item-meta {
  width: 100%;
}

.ant-list-item-extra {
  margin-left: 24px;
}
</style>
