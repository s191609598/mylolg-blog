<!--首页-文章详情评论树-->
<template>
  <div id="commentTreeModal">
    <div v-for="comment in rootComments" :key="comment.id">
      <a-comment>
        <template #actions>
          <span key="comment-like">
            <a-tooltip title="赞">
              <LikeOutlined
                :class="['like-icon', { liked: comment.isLiked }]"
                @click="like(comment)"
              />
            </a-tooltip>
            <span style="padding-left: 8px">{{ comment.upNum }}</span>
          </span>
          <span key="comment-reply" @click="toggleReply(comment)">回复</span>
        </template>
        <template #author>
          <a>{{ comment.createName }}</a>
        </template>
        <template #avatar>
          <a-avatar
            :src="comment.userAvatar || 'https://example.com/default-avatar.png'"
            :alt="comment.createName"
          />
        </template>
        <template #content>
          <p>{{ comment.content }}</p>
        </template>
        <template #datetime>
          <a-tooltip :title="comment.createTime">
            <span>{{ comment.createTime }}</span>
          </a-tooltip>
        </template>
        <!-- 回复区域 -->
        <div v-show="activeReplyId === comment.id && isReply" class="reply-container">
          <ReplyBoxModal
            :handleSubmit="handleSubmit"
            :articleId="props.articleId"
            :activeReplyId="activeReplyId"
          />
        </div>
        <!-- 递归渲染子评论 -->
        <CommentTreeModal
          v-if="parentMap[comment.id] && parentMap[comment.id].length > 0"
          :comments="parentMap[comment.id]"
          :handleSubmit="handleSubmit"
          :articleId="props.articleId"
        />
      </a-comment>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps, inject, ref, type Ref, watch } from 'vue'
import { LikeOutlined } from '@ant-design/icons-vue'
import { cloneDeep } from 'lodash'
import ReplyBoxModal from '@/pages/home/ReplyBoxModal.vue'

interface Props {
  comments: API.CommentsTreeVO[]
  handleSubmit: (comment: API.CommentHomeDTO) => Promise<void>
  articleId: number
}

const props = defineProps<Props>()

// 扁平化评论数据
const flattenComments = (
  comments: API.CommentsTreeVO[],
  pid: number | null = null,
): API.CommentsTreeVO[] => {
  return comments.reduce((acc, comment) => {
    acc.push({ ...comment, pid: pid as unknown as number })
    if (comment.children && comment.children.length > 0) {
      acc.push(...flattenComments(comment.children, comment.id))
    }
    return acc
  }, [] as API.CommentsTreeVO[])
}

// 存储扁平化的评论列表
const flatCommentsList = ref(flattenComments(props.comments))

// 存储父评论的映射
const parentMap = computed(() => {
  const map: { [key: number]: API.CommentsTreeVO[] } = {}
  flatCommentsList.value.forEach((comment) => {
    if (comment.pid !== null) {
      if (!map[comment.pid]) {
        map[comment.pid] = []
      }
      map[comment.pid].push(comment)
    }
  })
  return map
})

// 根评论（pid 为 null 的评论）
const rootComments = computed(() => {
  return flatCommentsList.value.filter((comment) => comment.pid === null)
})

// 共享状态：当前显示回复框的评论 ID
// 注入全局变量
const activeReplyId = inject('activeReplyId') as Ref<number | null>
const isReply = inject('isReply') as Ref<boolean | null>

// 点赞逻辑
const like = (comment: API.CommentsTreeVO) => {
  if (!comment.isLiked) {
    comment.upNum += 1
    comment.isLiked = true
  } else {
    comment.upNum -= 1
    comment.isLiked = false
  }
}

// 切换回复框显示
const toggleReply = (comment: API.CommentsTreeVO) => {
  activeReplyId.value = comment.id
  isReply.value = true
}

// 监听 props.comments 的变化并更新 localComments
watch(
  () => props.comments,
  (newComments) => {
    flatCommentsList.value = flattenComments(cloneDeep(newComments))
  },
  { deep: true, immediate: true }, // 立即执行一次以确保初始状态正确
)
</script>

<style scoped>
#commentTreeModal {
}

/* 添加一些样式 */
.like-icon {
  cursor: pointer;
  transition: color 0.3s;
}

.like-icon.liked {
  color: red;
}

.reply-container {
  display: flex;
  align-items: flex-start;
  margin-top: 10px;
}
</style>
