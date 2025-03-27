<!--首页-搜索数据列表-->
<template>
  <!-- 添加外层容器和样式 -->
  <div class="list-container">
    <a-list
      item-layout="vertical"
      size="large"
      :pagination="false"
      :data-source="listData"
      :locale="{ emptyText: '暂无数据' }"
      class="styled-list"
    >
      <template #renderItem="{ item }">
        <!-- 添加列表项样式 -->
        <a-list-item key="item.title" class="list-item">
          <!-- 保持原有内容 -->
          <template #actions>
            <span><FolderOutlined /> {{ item.categoryName }}</span>
            <span><EyeOutlined /> {{ item.readNum }}</span>
            <span>{{ item.createTime }}</span>
          </template>
          <a-list-item-meta :description="highlightText(item.description)">
            <template #title>
              <a
                @click="handleCardClick(item.id)"
                style="font-size: 22px"
                v-html="highlightText(item.title)"
              ></a>
            </template>
          </a-list-item-meta>
          <span v-html="highlightText(item.excerpt)"></span>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>
<script setup lang="ts">
import { EyeOutlined, FolderOutlined } from '@ant-design/icons-vue'
import { defineEmits, defineProps, ref } from 'vue'
import router from '@/router'

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
const loading = ref(false)

const handleCardClick = async (id: number) => {
  loading.value = true
  try {
    await router.push({ name: '文章详情', params: { id: id.toString() } })
  } finally {
    loading.value = false
    emit('update:open', false)
  }
}
// 处理卡片点击事件
// const handleCardClick = (id: number) => {
//   // router.push({ name: '文章详情', params: { id }, })
//   // setTimeout(() => {
//   //   emit('update:open', false)
//   // }, 100)
//   // emit('update:open', false)
//
// }
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

</style>

<style>/* 在组件内添加非scoped样式块 */
.highlight {
  background-color: #69b1ff !important;
  color: #003a8c !important;
  padding: 0 2px;
  border-radius: 2px;
  font-style: normal;
  display: inline-block;
  line-height: 1.4;
  transition: background-color 0.3s ease;
}

.highlight:hover {
  background-color: #4096ff !important;
}
</style>
