<!--首页-文章列表-->
<template>
  <div id="listModal">
    <a-list item-layout="vertical" size="large" :pagination="false" :data-source="listData">
      <template #renderItem="{ item }">
        <a-space direction="vertical">
          <a-card
            id="listCardPage"
            :bordered="true"
            :hoverable="true"
            style="padding: 0px"
            class="card-container"
            @click="handleCardClick(item.id)"
          >
            <p style="padding: 0px; margin: 0px">
              <a-list-item key="item.title">
                <template #extra>
                  <img
                    alt="cover"
                    :src="item.cover"
                    :onerror="setDefaultImage"
                    class="cover-image"
                  />
                </template>
                <a-list-item-meta>
                  <template #title>
                    <a-tag
                      color="error"
                      v-show="route.path === '/' && item.isTop === 1"
                      style="display: table-cell; vertical-align: middle"
                    >
                      <template #icon>
                        <PushpinOutlined />
                      </template>
                      置顶
                    </a-tag>
                    <a style="font-size: 22px">{{ item.title }}</a>
                  </template>
                </a-list-item-meta>
                {{ item.excerpt }}
                <template #actions>
                  <span v-for="{ icon, text } in item.actions" :key="icon">
                    <component :is="icon" style="margin-right: 8px; padding-top: 80px" />
                    {{ text }}
                  </span>
                </template>
              </a-list-item>
            </p>
          </a-card>
        </a-space>
      </template>
    </a-list>
    <!--    分页-->
    <a-divider />
    <div class="pageModule">
      <a-pagination
        v-model:current="searchParams.pageNo"
        v-model:page-size="searchParams.pageSize"
        :total="total"
        @change="onChange"
        :responsive="false"
        style="textalign: 'center'; margintop: '26px'"
      >
      </a-pagination>
    </div>
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
import imgerror from '@/assets/imgERROR.jpg';

const props = defineProps<{
  tagId?: number | string
  categoryId?: number | string
}>()

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
  const res = await searchFromEsUsingPost({
    ...searchParams,
    showTop: route.path === '/' ? true : false,
  })
  if (res.data.code === 0 && res.data.data) {
    listData.value = res.data.data.records.map((record:API.HomeArticleVO) => ({
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
#listModal {
}

#listCardPage {
  margin: 14px 5px;
  width: 1000px; /* 固定宽度 */
  max-width: 100%; /* 防止溢出父容器 */
}
/* 确保列表容器允许固定宽度 */
#listModal .ant-list {
  display: flex;
  flex-direction: column;
  align-items: center; /* 使卡片居中 */
}

.pageModule {
  display: flex;
  justify-content: center;
}

.cover-image {
  width: 200px;
  height: 200px;
  object-fit: cover; /* 保持图片宽高比并裁剪 */
  border-radius: 4px;
}
/* 调整卡片容器间距 */
.card-container {
  width: 100%; /* 继承父容器宽度 */
}

</style>
