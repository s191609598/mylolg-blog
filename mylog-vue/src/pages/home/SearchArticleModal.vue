<!--首页-搜索弹窗-->
<template>
  <div id="searchArticleModal">
    <a-modal
      :open="open"
      @cancel="handleOpenChange(false)"
      @ok="handleOpenChange(false)"
      width="1000px"
      title="搜索"
      :footer="null"
      :closable="true"
      :keyboard="true"
      :style="{ height: '1350px', overflowY: 'auto' }"
    >
      <a-spin :spinning="loading" tip="搜索中..." size="large" class="search-spin">
        <a-space direction="vertical" :style="{ width: '100%' }" :size="[0, 1158]">
          <a-layout>
            <!-- 搜索输入框 -->
            <a-layout-header :style="headerStyle">
              <a-input-search
                v-model:value="searchParams.keyword"
                placeholder="搜索"
                style="width: 200px"
                @search="onSearch"
                size="large"
                :style="searchStyle"
                allow-clear
                :loading="loading"
              />
            </a-layout-header>

            <!-- 数据列表/标签列表 -->
            <a-layout-content :style="{ ...contentStyle }">
              <TagListModal
                v-show="searchArticleList.length <= 0"
                @update:open="handleOpenChange"
              />
              <SearchArticleListModeal
                :listData="searchArticleList"
                :keyword="searchParams.keyword"
                @update:open="handleOpenChange"
              />
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
<script setup lang="ts">
import { type CSSProperties, defineEmits, defineProps, reactive, ref } from 'vue'
import TagListModal from '@/pages/home/TagListModal.vue'
import SearchArticleListModeal from '@/pages/home/SearchArticleListModeal.vue'
import { searchFromEsUsingPost } from '@/api/homeController.ts'
import { message } from 'ant-design-vue'
import { EyeOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue'

//loading 状态
const loading = ref(false)
defineProps<{ open: boolean }>()
const emit = defineEmits(['update:open'])

const searchArticleList = ref<API.SearchArticleByKeywordVO[]>([])
// 总条数
const total = ref(0)
// 搜索条件
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

//分页
const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  searchParams.pageSize = pageSize
  onSearch()
}

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
</script>
<style scoped>
.search-spin {
  width: 100%;
  min-height: 600px;
  display: flex;
  flex-direction: column;
}

/* 调整内容区域样式 */
:deep(.ant-layout-content) {
  min-height: auto !important;
  line-height: normal !important;
  padding: 20px 0;
}

/* 空状态样式 */
.ant-empty {
  margin: 50px 0;
}
</style>
