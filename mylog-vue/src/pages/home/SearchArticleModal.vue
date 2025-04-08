
<!--首页-搜索弹窗-->
<template>
  <div id="searchArticleModal">
    <a-modal
      :open="open"
      @cancel="handleOpenChange(false)"
      @ok="handleOpenChange(false)"
      :width="modalWidth"
      title="搜索"
      wrapClassName="search-modal-wrapper"
      :footer="null"
      :closable="true"
      :keyboard="true"
      :bodyStyle="modalBodyStyle"
    >
      <a-spin :spinning="loading" tip="搜索中..." size="large" class="search-spin">
        <a-space direction="vertical" :style="{ width: '100%' }" :size="[0, 1158]">
          <a-layout>
            <!-- 搜索输入框 -->
            <a-layout-header :style="headerStyle">
              <a-input-search
                v-model:value="searchParams.keyword"
                placeholder="搜索"                style="width: 200px"
                @search="onSearch"
                size="large"
                :style="searchStyle"
                allow-clear
                :loading="loading"
              />
            </a-layout-header>

            <!-- 数据列表/标签列表 -->
            <a-layout-content :style="contentStyle">
              <div class="mobile-content-wrapper">
                <TagListModal
                  v-show="searchArticleList.length <= 0"
                  @update:open="handleOpenChange"
                />
                <SearchArticleListModeal
                  :listData="searchArticleList"
                  :keyword="searchParams.keyword"
                  @update:open="handleOpenChange"
                />
              </div>
              <a-divider />
            </a-layout-content>

            <!-- 底部分页 -->
            <a-layout-footer :style="footerStyle">
              <a-pagination
                v-model:current="searchParams.pageNo"
                v-model:page-size="searchParams.pageSize"
                :total="total"
                @change="onChange"
                :hideOnSinglePage="true"
                show-less-items
                :disabled="loading"
              />
            </a-layout-footer>
          </a-layout>
        </a-space>
      </a-spin>
    </a-modal>
  </div>
</template>

<script setup lang="ts">import { computed, type CSSProperties, defineEmits, defineProps, reactive, ref } from 'vue'
import TagListModal from '@/pages/home/TagListModal.vue'
import SearchArticleListModeal from '@/pages/home/SearchArticleListModeal.vue'
import { searchFromEsUsingPost } from '@/api/homeController.ts'
import { message } from 'ant-design-vue'
import { useWindowSize } from '@vueuse/core'
import { EyeOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue'

const { width: windowWidth } = useWindowSize()

// 响应式参数
const isMobile = computed(() => windowWidth.value < 768)
const modalWidth = computed(() => isMobile.value ? '90%' : '1000px')
const modalBodyStyle = computed(() => ({
  padding: isMobile.value ? '16px' : '24px',
  maxHeight: isMobile.value ? '60vh' : '70vh',
  overflowY: 'auto'
}))

// 保持原有样式配置
const searchStyle: CSSProperties = {
  textAlign: 'center',
  color: '#fff',
  height: '50px',
  width: '100%',
  backgroundColor: 'white',
}

const headerStyle: CSSProperties = {
  textAlign: 'center',
  color: '#fff',
  backgroundColor: 'white',
  paddingInline: '0px',
}

const contentStyle: CSSProperties = {
  textAlign: 'center',
  minHeight: 120,
  lineHeight: '900px',
  color: '#fff',
  backgroundColor: 'white',
}

const footerStyle: CSSProperties = {
  textAlign: 'center',
  color: '#fff',
  backgroundColor: 'white',
  minHeight: 50,
  lineHeight: '50px',
}

// 组件状态
const loading = ref(false)
const searchArticleList = ref<API.SearchArticleByKeywordVO[]>([])
const total = ref(0)

defineProps<{ open: boolean }>()
const emit = defineEmits(['update:open'])

const searchParams = reactive<API.SearchArticleByKeywordDTO>({
  pageNo: 1,
  pageSize: 5,
  keyword: '',
})

const onSearch = async () => {
  if (!searchParams.keyword?.trim()) {
    searchArticleList.value = []
    return
  }

  try {
    loading.value = true
    const res = await searchFromEsUsingPost({ ...searchParams })
    if (res.data.code === 0 && res.data.data) {
      searchArticleList.value = res.data.data.records.map(
        (record: API.SearchArticleByKeywordVO) => ({
          ...record,
          actions: [
            { icon: EyeOutlined, text: record.readNum },
            { icon: LikeOutlined, text: record.upNum },
            { icon: MessageOutlined, text: record.commentNum },
          ],
        }),
      )
      total.value = res.data.data.total ?? 0
    } else {
      message.error('获取数据失败！' + res.data.msg)
    }
  } catch (error) {
    message.error('搜索失败：' + (error as Error).message)
  } finally {
    loading.value = false
  }
}

const handleOpenChange = (isOpen: boolean) => {
  emit('update:open', isOpen)
  searchArticleList.value = []
  searchParams.keyword = ''
  searchParams.pageNo = 1
  searchParams.pageSize = 5
  total.value = 0
}

const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  searchParams.pageSize = pageSize
  onSearch()
}
</script>

<style scoped>/* 保持原有样式 */
#searchArticleModal .search-spin {
  width: 100%;
  min-height: 600px;
  display: flex;
  flex-direction: column;
}

#searchArticleModal :deep(.ant-layout-content) {
  min-height: auto !important;
  line-height: normal !important;
  padding: 20px 0;
}

#searchArticleModal .ant-empty {
  margin: 50px 0;
}

/* 新增移动端适配 */
@media (max-width: 768px) {
  #searchArticleModal {
    :deep(.search-modal-wrapper) {
      top: 20px;
      max-width: 100vw;

      .ant-modal {
        &-content {
          border-radius: 8px;
        }
        &-body {
          padding: 16px;
          max-height: 60vh !important;
        }
      }
    }

    .mobile-content-wrapper {
      min-height: calc(60vh - 150px); /* 60%屏幕高度减去头部和底部空间 */
      overflow-y: auto;
    }

    /* 保持搜索框样式 */
    .ant-input-search {
      width: 200px !important;
      max-width: 80%;
      margin: 0 auto;
    }

    /* 分页样式微调 */
    .ant-pagination {
      padding: 8px 0;
    }
  }
}
</style>
