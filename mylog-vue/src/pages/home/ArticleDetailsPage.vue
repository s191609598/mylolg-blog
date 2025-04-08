<!--首页-文章详情-->
<template>
  <div id="articleDetailsPage">
    <!-- 添加全局加载提示 -->
    <a-spin :spinning="loading" tip="加载中..." size="large" class="global-spin">
      <a-row>
        <!-- 左边 -->
        <a-col :xs="0" :sm="3" :md="3">
          <!-- 左边的悬浮按钮 -->
          <div id="articleFloatButton">
            <a-float-button-group shape="circle">
              <!--              点赞-->
              <a-float-button
                :badge="{ count: article.upNum, overflowCount: 99 }"
                @click="handleLike"
                :class="{ liked: isLiked }"
              >
                <template #icon>
                  <LikeOutlined :style="{ color: isLiked ? '#eb2f96' : '' }" />
                </template>
              </a-float-button>
              <!--              评论-->
              <a-float-button
                :badge="{ count: article.commentNum, overflowCount: 99 }"
                @click="scrollToCommentBox"
              >
                <template #icon>
                  <CommentOutlined />
                </template>
              </a-float-button>
              <!--              收藏-->
              <a-float-button
                :badge="{ count: article.collectNum, overflowCount: 99 }"
                @click="handleCollect"
                :class="{ collected: isCollected }"
              >
                <template #icon>
                  <StarOutlined :style="{ color: isCollected ? '#ffd700' : '' }" />
                </template>
              </a-float-button>
            </a-float-button-group>
          </div>
        </a-col>
        <!-- 中间 -->
        <a-col :xs="24" :sm="18" :md="18">
          <a-card style="width: 100%; height: 100%">
            <div id="articleTtele">
              <a-typography-title>{{ article.title }}</a-typography-title>
              <a-divider />
            </div>
            <div id="articleContent">
              <MdPreview :modelValue="article.content" :id="state.id" :autoFoldThreshold="100" />
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
        <!-- 右边 -->
        <a-col :xs="0" :sm="3" :md="3">
          <!-- 目录 -->
          <a-affix :offset-top="top">
            <MdCatalog :editorId="state.id" :scrollElement="scrollElement" />
          </a-affix>
          <!-- 悬浮按钮 -->
          <div>
            <a-float-button-group
              class="mobile-back-top"
              shape="circle"
              :style="{ right: '34px', bottom: '120px' }"
            >
              <a-back-top :visibility-height="1" />
            </a-float-button-group>
          </div>
        </a-col>
      </a-row>
    </a-spin>
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
import { noArticleUsingGet1, upArticleUsingGet } from '@/api/sysArticleUpController.ts'
import { collectArticleUsingGet, noArticleUsingGet } from '@/api/sysArticleCollectController.ts'
import { debounce } from 'lodash-es'

//loading 状态
const loading = ref(true)
// 获取路由参数
const route = useRoute()
const articleId = route.params.id as string
const top = ref<number>(10)
const scrollElement = document.documentElement
const state = reactive({
  id: 'my-editor',
})
const value = ref('')
const isLiked = ref(false)
const isCollected = ref(false)

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


//获取文章评论
const fetchArticleComments = async () => {
  try {
    const res = await queryCommentByArticleIdUsingPost(searchParams)
    if (res.data.code === 0 && res.data.data) {
      comments.value = res.data.data.records
      total.value = res.data.data.total
      searchParams.pageNo = res.data.data.current
      searchParams.pageSize = res.data.data.size
    }
  } catch (error) {
    console.error('加载评论失败', error)
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

// 滚动到评论框的方法
const scrollToCommentBox = () => {
  const commentBox = document.getElementById('comment-box')
  if (commentBox) {
    commentBox.scrollIntoView({ behavior: 'smooth' })
  }
}

// 查询文章详情
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
      isLiked.value = res.data.data.isUp
      isCollected.value = res.data.data.isCollect
      searchParams.articleId = parseInt(id)
      fetchArticleComments()
    } else {
      message.error('获取文章详情失败！' + res.data.msg)
    }
  } catch (error) {
    console.error('加载文章详情失败', error)
  }finally {
    loading.value = false // 加载完成
  }
}

watch(
  () => route.params.id,
  (newId) => {
    loadArticleData(newId.toString())
  },
  { immediate: true },
)

// 点赞
const handleLike = debounce(async () => {
  const action = isLiked.value ? 'cancelLike' : 'like'
  const loading = message.loading('处理中...', 0)
  try {
    if (action === 'like') {
      const res = await upArticleUsingGet({ articleId: article.value.id! })
      if (res.data.code === 0) {
        article.value.upNum = (article.value.upNum ?? 0) + 1
        isLiked.value = !isLiked.value
        message.success('点赞成功')
      } else {
        message.error(res.data.msg)
      }
    } else {
      const res = await noArticleUsingGet1({ articleId: article.value.id! })
      if (res.data.code === 0) {
        article.value.upNum = (article.value.upNum ?? 0) - 1
        isLiked.value = !isLiked.value
        message.success('已取消点赞')
      } else {
        message.error(res.data.msg)
      }
    }
  } catch (error) {
    message.error('操作失败')
  } finally {
    loading()
  }
}, 300)

// 收藏
const handleCollect = debounce(async () => {
  const action = isCollected.value ? 'cancelCollect' : 'collect'
  const loading = message.loading('处理中...', 0)
  try {
    if (action === 'collect') {
      const res = await collectArticleUsingGet({ articleId: article.value.id })
      if (res.data.code === 0) {
        article.value.collectNum = (article.value.collectNum ?? 0) + 1
        isCollected.value = !isCollected.value
        message.success('收藏成功')
      } else {
        message.error(res.data.msg)
      }
    } else {
      const res = await noArticleUsingGet({ articleId: article.value.id })
      if (res.data.code === 0) {
        article.value.collectNum = (article.value.collectNum ?? 0) - 1
        isCollected.value = !isCollected.value
        message.success('已取消收藏')
      } else {
        message.error(res.data.msg)
      }
    }
  } catch (error) {
    message.error('操作失败')
  } finally {
    loading()
  }
}, 300)
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
  right: 24px;
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

.global-spin {
  width: 100%;
  min-height: 600px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 添加状态样式 */
#articleFloatButton .liked .anticon svg {
  fill: #eb2f96 !important;
}

#articleFloatButton .collected .anticon svg {
  fill: #ffd700 !important;
}

@media (max-width: 576px) {
  .mobile-card {
    margin: 0 8px;
    width: calc(100% - 16px) !important;
  }

  #articleContent {
    padding: 0 8px;
  }

  .mobile-back-top {
    right: 16px !important;
    bottom: 80px !important;
  }

  .mobile-back-top .ant-float-btn {
    width: 36px !important;
    height: 36px !important;
  }

  .mobile-back-top .ant-float-btn-icon {
    font-size: 16px !important;
  }

  /* 调整评论框样式 */
  #articleComment {
    padding: 0 8px;
  }

  .ant-typography-title {
    font-size: 1.5rem !important;
    padding: 0 8px;
  }
}
</style>
