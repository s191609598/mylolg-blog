<template>
  <div id="articleTagList">
    <a-row>
      <a-col :span="4"></a-col>
      <a-col :span="10">
        <a-layout style="background: white; padding-inline: 8px">
          <a-layout-header class="header">
            <a-card
              title="标签"
              :body-style="{ padding: '8px', maxHeight: '500px', overflowY: 'auto' }"
            >
              <div class="tag-grid">
                <a-spin v-if="tagListData.length === 0" tip="标签加载中..." />
                <a-card-grid
                  v-for="(tag, index) in tagListData"
                  :key="index"
                  :class="['tag-item', { 'active-tag': vo?.id === tag.id }]"
                  @click="getTag(tag)"
                >
                  {{ tag.name }}
                  <span v-show="(tag.num ?? 0) > 0"  class="tag-count">{{ tag.num }}</span>
                </a-card-grid>
              </div>
            </a-card>
          </a-layout-header>
          <a-layout-content class="content">
            <ListModal :tag-id="vo?.id" />
          </a-layout-content>
          <a-layout-footer class="footer"></a-layout-footer>
        </a-layout>
      </a-col>
      <a-col :span="6">
        <div id="rightCard">
          <CardModal />
        </div>
      </a-col>
      <a-col :span="4"></a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import CardModal from '@/pages/home/CardModal.vue'
import ListModal from '@/pages/home/ListModal.vue'
import { onMounted, ref, watch } from 'vue'
import { queryHomeTagAllUsingGet } from '@/api/homeController.ts'
import { useRoute } from 'vue-router'

const vo = ref<API.HomeTagVO>()

const tagListData = ref<API.HomeTagVO[]>([])

const queryTagAll = async () => {
  try {
    const res = await queryHomeTagAllUsingGet()
    if (res.data.code === 0 && res.data.data) {
      tagListData.value = res.data.data
      console.log('标签数据加载完成', tagListData.value)

      // 自动选择逻辑优化
      vo.value = tagListData.value.find(tag => tag.id === Number(route.query.tagId)) ?? tagListData.value[0]
      console.log('初始标签:', vo.value)
    }
  } catch (error) {
    console.error('加载标签失败:', error)
  }
}


const getTag = (tag: API.HomeTagVO) => {
  vo.value = tag
}
const route = useRoute()
// 生命周期钩子
onMounted(() => {
  queryTagAll().then(() => {
    // 挂载时检查路由参数
    if (route.query.tagId) {
      const initialId = Number(route.query.tagId)
      const targetTag = tagListData.value.find(tag => tag.id === initialId)
      if (targetTag) vo.value = targetTag
    }
  })
})

// 根据 tagId 执行搜索逻辑
watch(
  () => route.query.tagId,
  async (newVal) => {
    console.log('路由参数变化:', newVal)
    if (newVal) {
      // 确保数据已加载
      if (tagListData.value.length === 0) {
        await queryTagAll()
      }

      const numericId = Number(newVal)
      if (isNaN(numericId)) {
        console.error('无效的标签ID:', newVal)
        return
      }

      // 添加调试日志
      console.log('当前标签列表:', tagListData.value)
      const targetTag = tagListData.value.find(tag => tag.id === numericId)
      console.log('查找结果:', targetTag)

      if (targetTag) {
        vo.value = targetTag
        getTag(targetTag)
      } else {
        console.warn('未找到对应标签，使用默认标签')
        vo.value = tagListData.value[0]
      }
    }
  },
  { immediate: true }
)
</script>
<style scoped>
#articleTagList {
  text-align: center;
}

#articleTagList .footer {
  background: white;
}

#articleTagList .header {
  background: white;
  width: 100%;
  height: 100%;
  padding-inline: 6px;
}

.tag-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 12px;
  padding: 12px;
}

.tag-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  min-width: 100px;
  transition: all 0.3s;
  cursor: pointer;
  text-align: center;
  padding: 8px;
  margin: 2px;
}

.tag-item:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .tag-grid {
    grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
    gap: 8px;
    padding: 8px;
  }

  .tag-item {
    height: 50px;
    min-width: 80px;
    font-size: 12px;
    padding: 4px;
  }
}

/* 增加超大屏幕适配 */
@media (min-width: 1600px) {
  .tag-grid {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 16px;
  }

  .tag-item {
    min-width: 130px;
    height: 70px;
  }
}

.active-tag {
  background-color: #f0f5ff;
  border: 2px solid #18baff;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
  transform: scale(1.05);
}

/* 优化原有悬停效果 */
.tag-item:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.2s ease-in-out;
}

.tag-count {
  /* 基础样式 */
  font-size: 0.8em;
  margin-left: 4px;
  color: #1890ff; /* 使用antd主色 */

  /* 立体效果 */
  display: inline-block;
  padding: 2px 6px;
  background: rgba(24, 144, 255, 0.1);
  border-radius: 10px;

  /* 动态效果 */
  transition: all 0.2s ease-in-out;

  /* 与现有样式对齐 */
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial;
}
/* 激活状态适配 */
.active-tag .tag-count {
  background: rgba(255, 255, 255, 0.9);
  color: #1677ff;
  font-weight: 500;
}

/* 悬停互动效果 */
.tag-item:hover .tag-count {
  transform: scale(1.1);
  box-shadow: 0 2px 6px rgba(24, 144, 255, 0.2);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .tag-count {
    font-size: 0.7em;
    padding: 1px 4px;
  }
}
</style>
