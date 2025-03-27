<!--首页-文章详情-->
<template>
  <div id="articleDetailsPage">
    <a-row>
      <!--      左边-->
      <a-col :span="3">
        <!--        左边的悬浮按钮-->
        <div id="articleFloatButton">
          <a-float-button-group shape="circle">
            <a-float-button :badge="{ count: article.upNum, overflowCount: 999 }">
              <template #icon>
                <LikeOutlined />
              </template>
            </a-float-button>
            <a-float-button
              :badge="{ count: article.commentNum, overflowCount: 999 }"
              @click="scrollToCommentBox"
            >
              <template #icon>
                <CommentOutlined />
              </template>
            </a-float-button>
            <a-float-button :badge="{ count: article.collectNum, overflowCount: 999 }">
              <template #icon>
                <StarOutlined />
              </template>
            </a-float-button>
          </a-float-button-group>
        </div>
      </a-col>
      <!--      中间-->
      <a-col :span="18">
        <a-card style="width: 100%; height: 100%">
          <div id="articleTtele">
            <a-typography-title>{{ article.title }}</a-typography-title>
            <a-divider />
          </div>
          <div id="articleContent">
            <MdPreview :modelValue="article.content" :id="state.id" />
            <a-divider id="comment-box" />
          </div>
          <div id="articleComment">
            <div>
              <a-textarea v-model:value="value" placeholder="评论" :rows="4" />
              <a-flex gap="middle" align="start" vertical>
                <a-flex :style="{ ...boxStyle }" justify="flex-end" align="0">
                  <a-button
                    type="primary"
                    @click="handleSubmit({ articleId: article.id, content: value })"
                    >提交评论
                  </a-button>
                </a-flex>
              </a-flex>
            </div>
            <a-divider />
            <div>全部评论（{{ article.commentNum }}）</div>
            <CommentTreeModal
              :comments="comments"
              :handleSubmit="handleSubmit"
              :articleId="parseInt(articleId)"
            />
            <div class="pagination-container">
              <a-pagination
                v-model:current="searchParams.pageNo"
                :total="total"
                v-model:page-size="searchParams.pageSize"
                :showSizeChanger="false"
                @change="onChange"
                style="textalign: 'center'; margintop: '26px'"
              />
            </div>
          </div>
        </a-card>
      </a-col>
      <!--      右边-->
      <a-col :span="3">
        <!--        目录-->
        <a-affix :offset-top="top">
          <MdCatalog :editorId="state.id" :scrollElement="scrollElement" />
        </a-affix>
        <!--        悬浮按钮-->
        <div>
          <a-float-button-group shape="circle" :style="{ right: '34px', bottom: '120px' }">
            <a-back-top :visibility-height="1" />
          </a-float-button-group>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import { type CSSProperties, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import {
  CommentOutlined,
  EyeOutlined,
  LikeOutlined,
  MessageOutlined,
  StarOutlined,
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { MdCatalog, MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'
import CommentTreeModal from '@/pages/home/CommentTreeModal.vue'
import {
  getHomeArticleByIdUsingGet,
  queryCommentByArticleIdUsingPost,
  submitCommentUsingPost,
} from '@/api/homeController.ts'

// 获取路由参数
const route = useRoute()
const articleId = route.params.id as string
const top = ref<number>(10)
const scrollElement = document.documentElement
const state = reactive({
  id: 'my-editor',
})
const value = ref('')

const boxStyle: CSSProperties = {
  width: '100%',
  borderRadius: '6px',
  marginTop: '20px',
}

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

const total = ref<number>(0)
// 搜索条件
const searchParams = reactive<API.queryCommentDTO>({
  pageNo: 1,
  pageSize: 5,
  articleId: parseInt(articleId),
})
// 评论数据
const comments = ref<API.CommentsTreeVO[]>([])

// 查询文章详情
const fetchArticleDetails = async () => {
  const res = await getHomeArticleByIdUsingGet({ id: parseInt(articleId) })
  if (res.data.code === 0 && res.data.data) {
    article.value = {
      ...res.data.data,
      actions: [
        { icon: EyeOutlined, text: res.data.data.readNum },
        { icon: LikeOutlined, text: res.data.data.upNum },
        { icon: MessageOutlined, text: res.data.data.commentNum },
      ],
    }
    await fetchArticleComments()
  } else {
    message.error('获取文章详情失败！' + res.data.msg)
  }
}
//获取文章评论
const fetchArticleComments = async () => {
  const res = await queryCommentByArticleIdUsingPost(searchParams)
  if (res.data.code === 0 && res.data.data) {
    comments.value = res.data.data.records
    total.value = res.data.data.total
    searchParams.pageNo = res.data.data.current
    searchParams.pageSize = res.data.data.size
  }
}

//分页
const onChange = (pageNumber: number, pageSize: number) => {
  searchParams.pageNo = pageNumber
  fetchArticleComments()
}

// 提交/回复 评论
const handleSubmit = async (comment: API.CommentHomeDTO) => {
  const loading = message.loading('提交中...', 0)
  try {
    const res = await submitCommentUsingPost(comment)
    if (res.data.code === 0) {
      message.success('评论成功！')
      await fetchArticleComments()
      article.value.commentNum = (article.value.commentNum ?? 0) + 1
      value.value = ''
    }
  } catch (error) {
    const errorMessage =
      error instanceof Error ? error.message : typeof error === 'string' ? error : '未知错误'
    message.error('操作失败: ' + errorMessage)
  } finally {
    loading()
    // 确保在异步操作完成后才关闭弹窗
  }
}

// 生命周期钩子
onMounted(() => {
  fetchArticleDetails()
})
// 滚动到评论框的方法
const scrollToCommentBox = () => {
  const commentBox = document.getElementById('comment-box')
  if (commentBox) {
    commentBox.scrollIntoView({ behavior: 'smooth' })
  }
}
const loadArticleData = async (id: string) => {
  try {
    const res = await getHomeArticleByIdUsingGet({ id: parseInt(id) })
    if (res.data.code === 0 && res.data.data) {
      article.value = {
        ...res.data.data,
        actions: [
          { icon: EyeOutlined, text: res.data.data.readNum },
          { icon: LikeOutlined, text: res.data.data.upNum },
          { icon: MessageOutlined, text: res.data.data.commentNum },
        ],
      }
      searchParams.articleId = parseInt(id)
      fetchArticleComments()
    } else {
      message.error('获取文章详情失败！' + res.data.msg)
    }
  } catch (error) {
    console.error('加载文章详情失败', error)
  }
}

watch(
  () => route.params.id,
  (newId) => {
    loadArticleData(newId.toString())
  },
  { immediate: true },
)
</script>
<style scoped>
#articleFloatButton {
  position: fixed;
  left: 13%;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 16px;
  z-index: 1000;
}

/* 只针对左侧按钮组 */
#articleFloatButton .ant-float-btn-group {
  width: 40px;
  height: 40px;
}

.ant-col-span-3:last-child {
  /* 右侧列 */
  position: relative; /* 添加相对定位 */
}

/* 确保右侧按钮不受影响 */
.ant-col-span-3:last-child .ant-float-btn-group {
  position: absolute;
  right: 10px; /* 调整右边距 */
  bottom: 120px;
  z-index: 1000;
}

#articleDetailsPage {
  width: 100%;
  height: 100%;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 26px; /* 调整与上方内容的距离 */
}
</style>
