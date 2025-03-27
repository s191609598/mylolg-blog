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
      <a-space direction="vertical" :style="{ width: '100%' }" :size="[0, 1158]">
        <a-layout>
          <!--          搜索输入框-->
          <a-layout-header :style="headerStyle">
            <a-input-search
              v-model:value="searchParams.keyword"
              placeholder="搜索"
              style="width: 200px"
              @search="onSearch"
              size="large"
              :style="searchStyle"
              allow-clear
            />
          </a-layout-header>
          <!--          数据列表/标签列表-->
          <a-layout-content :style="{ ...contentStyle }">
            <TagListModal v-show="searchArticleList.length <= 0" @update:open="handleOpenChange" />
            <SearchArticleListModeal
              :listData="searchArticleList"
              :keyword="searchParams.keyword"
              @update:open="handleOpenChange"
            />
            <a-divider />
          </a-layout-content>
          <!--          底部分页-->
          <a-layout-footer :style="footerStyle">
            <a-pagination
              v-model:current="searchParams.pageNo"
              v-model:page-size="searchParams.pageSize"
              :total="total"
              @change="onChange"
              :hideOnSinglePage="true"
              show-less-items
            />
          </a-layout-footer>
        </a-layout>
      </a-space>
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
import { useRouter } from 'vue-router'

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
  if (!searchParams.keyword || !searchParams.keyword.trim()) {
    searchArticleList.value = []
    return
  }
  const res = await searchFromEsUsingPost({ ...searchParams })
  if (res.data.code === 0 && res.data.data) {
    searchArticleList.value = res.data.data.records
    total.value = res.data.data.total ?? 0
    res.data.data.records.map((record: API.SearchArticleByKeywordVO) => ({
      ...record,
      actions: [
        { icon: EyeOutlined, text: record.readNum },
        { icon: LikeOutlined, text: record.upNum },
        { icon: MessageOutlined, text: record.commentNum },
      ],
    }))
  } else {
    message.error('获取数据失败！' + res.data.msg)
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

const router = useRouter()

// 修改 TagListModal.vue（或对应的标签列表组件）中的标签点击处理
const handleTagClick = (tag: API.HomeTagVO) => {
  router.push('/article/tags') // 跳转到标签列表页路由
  emit('update:open', false) // 关闭搜索弹窗
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
#searchArticleModal {
}
</style>
