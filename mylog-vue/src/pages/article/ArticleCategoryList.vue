<template>
  <div id="articleCategoryList">
    <a-row>
      <a-col :span="4"></a-col>
      <a-col :span="10">
        <a-layout style="background: white; padding-inline: 8px">
          <a-layout-header class="header">
            <div class="scroll-container">
              <div class="buttons-wrapper">
                <a-button
                  v-for="(item, index) in ListData"
                  :key="index"
                  size="large"
                  type="primary"
                  ghost
                  :icon="h(FolderOpenOutlined)"
                  :class="{ 'active-btn': vo?.id === item.id }"
                  @click="getCategory(item)"
                >
                  {{ item.name }}
                </a-button>
              </div>
            </div>
          </a-layout-header>
          <a-layout-content class="content">
            <ListModal :categoryId="vo?.id" />
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
import { h, onMounted, ref } from 'vue'
import { getHomeCategoryAllUsingGet } from '@/api/homeController.ts'
import { FolderOpenOutlined } from '@ant-design/icons-vue'

const vo = ref<API.CategoryVO>()

const ListData = ref<API.CategoryVO[]>([])
const queryCategoryAll = async () => {
  const res = await getHomeCategoryAllUsingGet()
  if (res.data.code === 0) {
    ListData.value = res.data.data
    // 默认选中第一个标签
    vo.value = res.data.data[0]
    // 主动触发首次查询
    getCategory(res.data.data[0])
  }
}
const getCategory = (category: API.CategoryVO) => {
  vo.value = category
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

.scroll-container {
  max-height: 500px; /* 替换原来的 height */
  min-height: 65px; /* 设置最小高度保持可用性 */
  overflow-y: auto;
  padding: 8px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  height: auto; /* 确保高度自适应 */
}

.buttons-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center; /* 新增居中布局 */
  padding: 8px 0; /* 添加垂直内边距 */
}
.active-btn {
  background-color: rgba(24, 144, 255, 0.1) !important;
  border-color: #1890ff !important;
}

/* 保留原有header样式 */
.header {
  background: white;
  width: 100%;
  height: 100%;
  padding-inline: 6px;
}

@media (max-width: 1600px) {
  .buttons-wrapper > .ant-btn {
    flex-basis: calc(20% - 10px); /* 屏幕较小时5个/行 */
  }
}

@media (max-width: 1200px) {
  .buttons-wrapper > .ant-btn {
    flex-basis: calc(25% - 10px); /* 4个/行 */
  }
}

/* 大屏适配 */
@media (min-width: 1920px) {
  .buttons-wrapper > .ant-btn {
    flex-basis: calc(12.5% - 10px); /* 8个/行 */
    min-width: auto; /* 在大屏解除最小宽度限制 */
  }
}
</style>
