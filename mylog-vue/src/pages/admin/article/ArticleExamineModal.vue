<template>
  <div id="articleExamineModal">
    <a-modal
      :open="open"
      :title="'查看文章'"
      width="100%"
      :style="{ height:'100%' }"
      wrap-class-name="full-modal"
      :closable="true"
      :keyboard="true"
      @cancel="handleCancel"
      :footer="null"
    >

      <a-layout>
<!--        <a-layout-header :style="headerStyle">-->


<!--        </a-layout-header>-->
        <div id="articleTtele">
          <a-typography-title>{{ article.title }}</a-typography-title>
          <a-divider />
        </div>
        <a-layout-content :style="contentStyle">

          <div id="articleContent">
            <MdPreview :modelValue="article.content" :id="state.id" />
            <a-divider />
          </div>
        </a-layout-content>
<!--        <a-layout-footer :style="footerStyle">Footer</a-layout-footer>-->
      </a-layout>
      <!--      <a-row>-->
      <!--      左边-->
      <!--        <a-col :span="3" style="height: 100%"></a-col>-->
      <!--      中间-->
      <!--        <a-col :span="18">-->
      <!--          <a-card style="width: 100%; height: 100%">-->


      <!--          </a-card>-->
      <!--        </a-col>-->
      <!--      右边-->
      <!--        <a-col :span="3" style="width: 100%; height: 100%">-->
      <!--          &lt;!&ndash;        目录&ndash;&gt;-->
      <!--&lt;!&ndash;          <a-affix :offset-top="top">&ndash;&gt;-->
      <!--&lt;!&ndash;            <MdCatalog :editorId="state.id" :scrollElement="scrollElement" />&ndash;&gt;-->
      <!--&lt;!&ndash;          </a-affix>&ndash;&gt;-->
      <!--&lt;!&ndash;          &lt;!&ndash;        悬浮按钮&ndash;&gt;&ndash;&gt;-->
      <!--&lt;!&ndash;          <div>&ndash;&gt;-->
      <!--&lt;!&ndash;            <a-float-button-group shape="circle" :style="{ right: '34px', bottom: '120px' }">&ndash;&gt;-->
      <!--&lt;!&ndash;              <a-back-top :visibility-height="1" />&ndash;&gt;-->
      <!--&lt;!&ndash;            </a-float-button-group>&ndash;&gt;-->
      <!--&lt;!&ndash;          </div>&ndash;&gt;-->
      <!--        </a-col>-->
      <!--      </a-row>-->
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { MdCatalog, MdPreview } from 'md-editor-v3'
import { type CSSProperties, defineEmits, defineProps, reactive, ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { getHomeArticleByIdUsingGet } from '@/api/homeController.ts'

const props = defineProps<{
  open: boolean
  articleId: number
}>()

const top = ref<number>(10)
const scrollElement = document.documentElement
const state = reactive({
  id: 'my-editor',
})

// 文章详情
const article = ref<API.ArticleVO>({
  id: undefined,
  title: '',
  cover: '',
  excerpt: '',
  content: '',
  readNum: 0,
  upNum: 0,
  commentNum: 0,
  collectNum: 0,
  categoryId: undefined,
  tags: [],
  state: undefined,
  isTop: 0,
  isRecommend: 0,
  isCarousel: 0,
  sort: undefined,
  reprintUrl: '',
})

// 查询文章详情
const fetchArticleDetails = async () => {
  // const res = await getHomeArticleByIdUsingGet({ id: props.articleId })
  // if (res.data.code === 0 && res.data.data) {
  //   article.value = {
  //     ...res.data.data,
  //   }
  // } else {
  //   message.error('获取文章详情失败！' + res.data.msg)
  // }
}

// 监听 articleId 变化
watch(
  () => props.articleId,
  (newId) => {
    if (newId) {
      fetchArticleDetails()
    }
  },
)

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
}>()

// 处理取消事件
const handleCancel = () => {
  emit('update:open', false)
}


const headerStyle: CSSProperties = {
  textAlign: 'center',
  color: '#fff',
  height: 64,
  paddingInline: 50,
  lineHeight: '64px',
  backgroundColor: '#7dbcea',
};

const contentStyle: CSSProperties = {
  textAlign: 'center',
  minHeight: 120,
  lineHeight: '120px',
  color: '#fff',
  backgroundColor: '#108ee9',
};


const footerStyle: CSSProperties = {
  textAlign: 'center',
  color: '#fff',
  backgroundColor: '#7dbcea',
};
</script>

<style scoped>
#articleExamineModal {
  height: 100%;
}
</style>
