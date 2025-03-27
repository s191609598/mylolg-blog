<!--首页-文章详情评论回复框-->
<template>
  <div id="replyBoxModal">
    <a-list
      v-if="comments.length"
      :data-source="comments"
      :header="`${comments.length} ${comments.length > 1 ? 'replies' : 'reply'}`"
      item-layout="horizontal"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <a-comment
            :author="item.author"
            :avatar="item.avatar"
            :content="item.content"
            :datetime="item.datetime"
          />
        </a-list-item>
      </template>
    </a-list>
    <a-comment>
      <template #avatar>
        <a-avatar
          :src="loginUserStore.loginUser.userAvatar"
          :alt="loginUserStore.loginUser.userName"
        />
      </template>
      <template #content>
        <div class="full-width-content" :style="{ width: '100%' }">
          <a-form-item class="full-width-form-item" :style="{ width: '100%' }">
            <a-textarea
              v-model:value="value"
              :rows="4"
              class="full-width-textarea"
              :style="{ width: '100%' }"
            />
          </a-form-item>
          <a-form-item>
            <a-space>
              <a-button
                :size="size"
                html-type="submit"
                :loading="submitting"
                type="primary"
                @click="handleSubmit(commenthomedto)"
              >
                回复
              </a-button>
              <a-button :size="size" @click="handleCancel" danger> 取消</a-button>
            </a-space>
          </a-form-item>
        </div>
      </template>
    </a-comment>
  </div>
</template>

<script lang="ts" setup>
import { defineProps, inject, type Ref, ref } from 'vue'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'

interface Props {
  handleSubmit: (comment: API.CommentHomeDTO) => Promise<void>
  articleId: number
  activeReplyId?: number | null
}

const props = defineProps<Props>()

dayjs.extend(relativeTime)

type Comment = Record<string, string>
const loginUserStore = useLoginUserStore()
const comments = ref<Comment[]>([])
const submitting = ref<boolean>(false)
const value = ref<string>('')
const size = ref('small')

// 提交评论DTO
const commenthomedto = ref<API.CommentHomeDTO>({
  articleId: undefined,
  content: '',
  pid: undefined,
})
// 提交评论DTO
const handleSubmit = async (comment: API.CommentHomeDTO) => {
  if (!value.value) {
    return
  }
  if (value.value?.trim() === '') {
    message.warning('回复内容不能为空')
    return
  }
  comment.articleId = props.articleId
  comment.content = value.value
  if (!props.activeReplyId || props.activeReplyId === 0) {
    comment.pid = undefined
  } else {
    comment.pid = props.activeReplyId
  }
  props.handleSubmit(comment)
  comment.content = undefined
  comment.pid = undefined
  value.value = ''
  handleCancel()
}

const isReply = inject('isReply') as Ref<boolean | null>
const activeReplyId = inject('activeReplyId') as Ref<number | null>
const handleCancel = () => {
  isReply.value = false
  activeReplyId.value = null
}
</script>

<style scoped>
#replyBoxModal {
}

/* 确保内容区域的宽度为 100% */
.full-width-content {
  width: 100%;
  display: flex;
  flex-direction: column;
}

/* 确保 a-form-item 的宽度为 100% */
.full-width-form-item {
  width: 100%;
  margin-bottom: 10px; /* 添加一些间距 */
}

/* 确保 a-textarea 的宽度为 100% */
.full-width-textarea {
  width: 100%;
}

/* 覆盖 Ant Design Vue 的默认样式 */
.ant-input {
  width: 100% !important;
}

:where(.css-dev-only-do-not-override-1p3hq3p)[class^='ant-comment'] [class^='ant-comment'],
:where(.css-dev-only-do-not-override-1p3hq3p)[class*=' ant-comment'] [class^='ant-comment'],
:where(.css-dev-only-do-not-override-1p3hq3p)[class^='ant-comment'] [class*=' ant-comment'],
:where(.css-dev-only-do-not-override-1p3hq3p)[class*=' ant-comment'] [class*=' ant-comment'] {
  box-sizing: border-box;
  width: 100% !important;
}
</style>
