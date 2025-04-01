<!--首页-搜索弹窗标签列表-->
<template>
  <div id="tagListModal">
    <a-spin :spinning="loading" tip="加载标签中..." size="large" class="tag-spin">
      <a-typography-title :level="3" :style="tagListTitle">
        <TagsOutlined />
        标签搜索
      </a-typography-title>
      <a-typography-paragraph :style="tagListParagraph">
        <template v-if="tagListData.length > 0">
          <a-space :size="[1, 'large']" wrap v-for="item in tagListData" :key="item">
            <a-tag :bordered="false" :style="tagData" @click="handleTagClick(item)">
              <a>{{ item.name }}</a>
            </a-tag>
          </a-space>
        </template>
      </a-typography-paragraph>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { TagsOutlined } from '@ant-design/icons-vue'
import { type CSSProperties, defineEmits, onMounted, ref } from 'vue'
import { queryHomeTagAllUsingGet } from '@/api/homeController.ts'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

//  loading 状态
const loading = ref(false)
const tagListTitle: CSSProperties = {
  textAlign: 'left',
}

const tagListParagraph: CSSProperties = {
  textAlign: 'left',
}

const tagData: CSSProperties = {
  fontSize: '20px',
  padding: '8px',
  margin: '10px',
}

const tagListData = ref<API.HomeTagVO[]>([])
/**
 * 首页-查询所有标签
 */
const queryTagAll = async () => {
  try {
    loading.value = true
    const res = await queryHomeTagAllUsingGet()
    if (res.data.code === 0) {
      tagListData.value = res.data.data || []
    }
  } catch (error) {
    message.error('加载标签失败')
  } finally {
    loading.value = false
  }
}

const router = useRouter()
const emit = defineEmits(['update:open'])
// 修改 TagListModal.vue（或对应的标签列表组件）中的标签点击处理
const handleTagClick = (tag: API.HomeTagVO) => {
  router.push({
    name: '标签',
    query: {
      // 新增查询参数
      tagId: tag.id,
    },
  })
  emit('update:open', false) // 关闭搜索弹窗
}

// 生命周期钩子
onMounted(() => {
  queryTagAll()
})
</script>

<style scoped>
#tagListModal {
  height: 600px;
  margin-bottom: 30px;
  position: relative; /* 确保spin定位准确 */
}

.tag-spin {
  width: 100%;
  min-height: 400px; /* 根据容器高度调整 */
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 保持左对齐 */
  padding: 20px;
}

/* 空状态样式 */
.ant-empty {
  margin-top: 50px;
}
</style>
