<template>
  <div id="articleCategoryList">
    <a-row>
      <a-col :xs="0" :sm="4" :md="4"></a-col>
      <a-col :xs="24" :sm="10" :md="10">
        <a-layout style="background: white; padding-inline: 8px">
          <a-layout-header class="header">
            <div class="scroll-container">
              <div class="buttons-wrapper">
                <a-segmented
                  v-model:value="vo.id"
                  :options="
                    ListData.map((item) => ({
                      label: item.name,
                      value: item.id,
                    }))
                  "
                  size="large"
                  @change="handleSegmentChange"
                  style="
                    border-radius: 8px;
                    padding: 8px; /* 增加容器内边距 */
                    width: 100%;
                    box-shadow: none;
                    gap: 8px; /* 新增选项间距 */
                    font-size: 36px;
                  "
                />
              </div>
            </div>
          </a-layout-header>
          <a-layout-content class="content">
            <ListModal :categoryId="vo?.id" />
          </a-layout-content>
          <a-layout-footer class="footer"></a-layout-footer>
        </a-layout>
      </a-col>
      <a-col :xs="0" :sm="6" :md="6">
        <div id="rightCard">
          <CardModal />
        </div>
      </a-col>
      <a-col :xs="0" :sm="4" :md="4"></a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import CardModal from '@/pages/home/CardModal.vue'
import ListModal from '@/pages/home/ListModal.vue'
import { onMounted, ref } from 'vue'
import { getHomeCategoryAllUsingGet } from '@/api/homeController.ts'

const vo = ref<API.CategoryVO>({ id: undefined }) // 初始化为包含id属性的对象
// 新增处理方法
const handleSegmentChange = (selectedId: number) => {
  const foundItem = ListData.value.find((item) => item.id === selectedId)
  if (foundItem) {
    vo.value = { ...foundItem } // 创建新对象触发响应式更新
  }
}
const ListData = ref<API.CategoryVO[]>([])
const queryCategoryAll = async () => {
  const res = await getHomeCategoryAllUsingGet()
  if (res.data.code === 0) {
    ListData.value = res.data.data
    vo.value.id = res.data.data[0]?.id // 设置默认选中第一个
  }
}

// 生命周期钩子
onMounted(() => {
  queryCategoryAll()
})
</script>
<style scoped>
#articleCategoryList {
  text-align: center;
}

#articleCategoryList .footer {
  background: white;
}

#articleCategoryList .header {
  background: white;
  width: 100%;
  height: 100%;
  padding-inline: 6px;
}

/* 增加滑动指示效果 */
.scroll-container {
  mask-image: linear-gradient(to right, transparent, black 20px calc(100% - 20px), transparent);
}

@media (max-width: 768px) {
  .scroll-container {
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
    width: 100%;
    padding-bottom: 8px; /* 留出滚动条空间 */
  }

  .buttons-wrapper {
    display: flex;
    min-width: max-content;
    gap: 8px;
    padding: 0 12px;
  }

  /* 隐藏滚动条 */
  .scroll-container::-webkit-scrollbar {
    display: none;
  }

  /* 调整分段控件样式 */
  :deep(.ant-segmented) {
    font-size: 14px !important;
    padding: 4px !important;
    gap: 4px !important;
  }

  :deep(.ant-segmented-item) {
    flex-shrink: 0;
    white-space: nowrap;
    padding: 0 8px !important;
  }

  :deep(.ant-segmented-item-label) {
    overflow: visible;
    text-overflow: unset;
  }
}
</style>
