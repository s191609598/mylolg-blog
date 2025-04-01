<!--首页-搜索数据列表-->
<template>
  <div class="list-container">
    <a-spin :spinning="listLoading" tip="加载文章..." size="large" class="list-spin">
      <a-list
        item-layout="vertical"
        size="large"
        :pagination="false"
        :data-source="listData"
        :locale="{ emptyText: '暂无数据' }"
        class="styled-list"
      >
        <template #renderItem="{ item }">
          <a-list-item key="item.title" class="list-item">
            <template #actions>
              <span><FolderOutlined /> {{ item.categoryName }}</span>
              <span><EyeOutlined /> {{ item.readNum }}</span>
              <span>{{ item.createTime }}</span>
            </template>
            <a-list-item-meta :description="highlightText(item.description)">
              <template #title>
                <a-button
                  type="link"
                  @click="handleCardClick(item.id)"
                  style="font-size: 22px; padding: 0"
                  :loading="itemLoading === item.id"
                >
                  <span v-html="highlightText(item.title)"></span>
                </a-button>
              </template>
            </a-list-item-meta>
            <span v-html="highlightText(item.excerpt)"></span>
          </a-list-item>
        </template>
      </a-list>
    </a-spin>
  </div>
</template>
<script setup lang="ts">
import { EyeOutlined, FolderOutlined } from '@ant-design/icons-vue'
import { defineEmits, defineProps, ref, watch } from 'vue'
import router from '@/router'

// 新增加载状态
const listLoading = ref(false)
const itemLoading = ref<number | null>(null)

const emit = defineEmits(['update:open'])

interface Props {
  listData: API.SearchArticleByKeywordVO[]
  keyword?: string
}

const props = defineProps<Props>()
// 高亮处理方法
const highlightText = (text: string = '') => {
  const keyword = props.keyword?.trim()
  if (!keyword) return text

  // 转义特殊字符
  const escapedKeyword = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  const regex = new RegExp(`(${escapedKeyword})`, 'gi')

  return text.replace(regex, '<mark class="highlight">$1</mark>')
}

const handleCardClick = async (id: number) => {
  try {
    itemLoading.value = id
    await router.push({ name: '文章详情', params: { id: id.toString() } })
    emit('update:open', false)
  } finally {
    itemLoading.value = null
  }
}

// 监听父组件数据变化
watch(
  () => props.listData,
  (newVal) => {
    listLoading.value = false
  },
  { immediate: true },
)
</script>
<style scoped>
.list-container {
  border-radius: 8px;
  overflow: hidden;
}

.styled-list {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.list-item {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

.list-item:last-child {
  border-bottom: none;
}

.list-item:hover {
  background-color: #fafafa;
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .list-item {
    padding: 12px 16px;
  }

  .styled-list {
    border-radius: 6px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  }
}

.list-spin {
  width: 100%;
  min-height: 200px;
}

.empty-state img {
  width: 80px;
  height: 80px;
  margin-bottom: 10px;
}

.empty-state p {
  font-size: 14px;
  color: #999;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px; /* 根据需要调整高度 */
}

.empty-state p {
  font-size: 14px;
  color: #999;
}
</style>

<style>
/* 在组件内添加非scoped样式块 */
.highlight {
  background-color: #69b1ff !important;
  color: #003a8c !important;
}

.highlight:hover {
  background-color: #4096ff !important;
}
</style>
