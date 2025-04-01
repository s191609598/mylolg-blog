<template>
  <a-modal
    :open="open"
    :title="isEdit ? '编辑文章' : '新增文章'"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="提交"
    cancelText="取消"
    width="100%"
    wrap-class-name="full-modal"
  >
    <a-form :model="formState" :label-col="{ span: 6 }" :wrapper-col="{ span: 14 }">
      <a-form-item label="标题" name="title">
        <a-input v-model:value="formState.title" />
      </a-form-item>
      <a-form-item label="文章类型" name="categoryId">
        <a-select
          v-model:value="formState.categoryId"
          :size="size"
          style="width: 200px"
          :options="categoryData"
          :field-names="{ label: 'name', value: 'id' }"
        ></a-select>
      </a-form-item>
      <a-form-item label="文章封面" name="cover">
        <div class="clearfix">
          <a-upload
            list-type="picture-card"
            v-model:file-list="fileList"
            @preview="handlePreview"
            :customRequest="customUpload"
            :before-upload="beforeUpload"
            @remove="handleRemoveUpload"
          >
            <div v-if="fileList.length < 1">
              <plus-outlined />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancelPreview">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </div>
      </a-form-item>
      <a-form-item label="摘要" name="excerpt">
        <a-textarea v-model:value="formState.excerpt" placeholder="摘要" :rows="4" />
      </a-form-item>

      <a-form-item label="文章类别" name="articleType">
        <a-select v-model:value="formState.articleType" style="width: 200px">
          <a-select-option value="0">原创</a-select-option>
          <a-select-option value="1">转载</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="是否置顶" name="isTop">
        <a-switch v-model:checked="isTopSwitch" />
      </a-form-item>
      <a-form-item v-show="formState.isCarousel === 0" label="是否推荐" name="isRecommend">
        <a-switch v-model:checked="isRecommendSwitch" />
      </a-form-item>
      <a-form-item v-show="formState.isRecommend === 0" label="是否轮播图" name="isCarousel">
        <a-switch v-model:checked="isCarouselSwitch" />
      </a-form-item>
      <a-form-item
        v-show="formState.isCarousel === 1 || formState.isRecommend === 1"
        label="排序"
        name="sort"
      >
        <a-input-number id="inputNumber" v-model:value="formState.sort" />
      </a-form-item>
      <a-form-item v-show="formState.articleType == 1" label="转载地址" name="reprintUrl">
        <a-textarea v-model:value="formState.reprintUrl" placeholder="转载地址" :rows="4" />
      </a-form-item>
      <a-form-item label="标签" name="reprintUrl">
        <a-select
          v-model:value="formState.tags"
          mode="tags"
          style="width: 100%"
          placeholder="标签"
          :options="options"
          @change="handleChange"
          :token-separators="[',']"
          allow-clear
        ></a-select>
      </a-form-item>
      <a-form-item label="文章内容" name="content">
        <MdEditor
          v-model="formState.content"
          :showToolbarName="true"
          @onUploadImg="onUploadImg"
          @onGetCatalog="onGetCatalog"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, defineEmits, defineProps, onMounted, reactive, ref, watch } from 'vue'
import { queryTagAllUsingGet } from '@/api/tagController.ts'
import { message, type SelectProps } from 'ant-design-vue'
import { config, MdEditor, XSSPlugin } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { getDefaultWhiteList } from 'xss'
import { getCategoryAllUsingGet } from '@/api/categoryController.ts'
import { baseUrl, uploadUrl, uploadcoverUrl } from '@/config/config.ts'
import myAxios from '@/request.ts'

const props = defineProps<{
  open: Boolean
  article: API.EditArticleDTO | null
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'submit', value: API.EditArticleDTO): void
}>()

const size = ref<SelectProps['size']>('middle')

const actionUrl = baseUrl + uploadcoverUrl

// MdEditor 的 xss 配置
config({
  markdownItPlugins(plugins) {
    return [
      ...plugins,
      {
        type: 'xss',
        plugin: XSSPlugin,
        options: {
          // 方式一：自行扩展全部
          xss() {
            return {
              whiteList: Object.assign({}, getDefaultWhiteList(), {
                // 如果你需要使用任务列表，请保留这项配置
                img: ['class', 'onerror'],
                input: ['class', 'disabled', 'type', 'checked'],
                // 如果你需要使用嵌入视频代码，请保留这项配置
                iframe: [
                  'class',
                  'width',
                  'height',
                  'src',
                  'title',
                  'border',
                  'frameborder',
                  'framespacing',
                  'allow',
                  'allowfullscreen',
                ],
              }),
            }
          },
          // 方式二：在默认白名单的基础上新增。^4.15.6
          extendedWhiteList: {
            img: ['onerror'],
          },
        },
      },
    ]
  },
})

const formState = reactive<API.EditArticleDTO>({
  id: undefined,
  title: '',
  categoryId: undefined,
  content: '',
  cover: '',
  excerpt: '',
  state: undefined,
  articleType: undefined,
  isTop: undefined,
  isRecommend: undefined,
  isCarousel: undefined,
  sort: undefined,
  reprintUrl: '',
  tags: [],
})
const onGetCatalog = (list: any) => {
  // //console.log("onGetCatalog",list)
}
const handleChange = (value: string[]) => {
  // //console.log("handleChange",`selected ${value}`)
  formState.tags = value.filter((tag) => tag.trim() !== '') // 过滤掉空标签
}
const options = ref<{ value: string }[]>([])
const categoryData = ref<{ value: API.CategoryVO }[]>([])

// 获取文章类型
const fetchCategory = async () => {
  const res = await getCategoryAllUsingGet()
  if (res.data.code === 0 && res.data.data) {
    categoryData.value = res.data.data
  }
}
//获取标签
const fetchTags = async () => {
  try {
    const res = await queryTagAllUsingGet()
    if (res.data.code === 0) {
      options.value = res.data.data.map((tag: string) => ({ value: tag }))
    } else {
      message.error('获取标签失败！' + res.data.msg)
    }
  } catch (error) {
    const errorMessage =
      error instanceof Error ? error.message : typeof error === 'string' ? error : '未知错误'
    message.error('获取标签失败！: ' + errorMessage)
  }
}

// 生命周期钩子
onMounted(() => {
  fetchTags()
  fetchCategory()
})

const isTopSwitch = computed({
  get: () => formState.isTop === 1,
  set: (value) => {
    formState.isTop = value ? 1 : 0
  },
})

const isRecommendSwitch = computed({
  get: () => formState.isRecommend === 1,
  set: (value) => {
    formState.isRecommend = value ? 1 : 0
  },
})

const isCarouselSwitch = computed({
  get: () => formState.isCarousel === 1,
  set: (value) => {
    formState.isCarousel = value ? 1 : 0
  },
})

const isEdit = computed(() => !!props.article?.id)

// 定义 FileItem 类型
interface FileItem {
  id: number
  uid: string
  name?: string
  status?: string
  response?: string
  percent?: number
  url?: string
  preview?: string
  originFileObj?: File
}

// 上传
const fileList = ref<FileItem[]>([])

watch(
  () => props.article,
  (newVal) => {
    if (newVal) {
      Object.assign(formState, newVal)
      // 将数字转换为对应的中文文本
      articleType: Number(newVal.articleType) || 0;
      // 仅当 cover 不为空字符串时，添加到 fileList
      if (newVal.cover) {
        fileList.value = [
          {
            id: -1,
            uid: '-1', // 添加必要字段
            name: 'cover', // 添加必要字段
            status: 'done', // 必须字段
            url: newVal.cover,
          },
        ]
      } else {
        fileList.value = []
      } // 将图片地址添加到 fileList 中
    } else {
      Object.assign(formState, {
        id: undefined,
        title: '',
        categoryId: undefined,
        content: '',
        cover: '',
        excerpt: '',
        state: undefined,
        articleType: '0', // 默认值设置为字符串形式的数字
        isTop: 0,
        isRecommend: 0,
        isCarousel: 0,
        sort: undefined,
        reprintUrl: '',
        tags: [],
      })
      fileList.value = [] // 清空 fileList
    }
  },
  { immediate: true },
)

const handleOk = () => {
  // 直接提交数字值，无需转换
  emit('submit', formState)
  emit('update:open', false)
}

const handleCancel = () => {
  emit('update:open', false)
}
// 关闭预览
const handleCancelPreview = () => {
  previewVisible.value = false
}

// 上传
// 上传图片-校验
const beforeUpload = (file: File) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('只能上传 JPG 或 PNG 文件!')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('图片必须小于 2MB!')
  }
  return isJpgOrPng && isLt2M
}

// const handleChangeUpload = async (info: any) => {
//   if (info.file.status === 'done') {
//     try {
//       // 统一使用封装请求工具
//       const formData = new FormData()
//       formData.append('file', info.file.originFileObj)
//
//       const response = await myAxios.post(actionUrl, formData, {
//         headers: { 'Content-Type': 'multipart/form-data' },
//       })
//
//       if (response.data.code === 0) {
//         formState.cover = response.data.data.fileUrl
//         message.success('封面图片上传成功')
//       } else {
//         message.error(response.data.msg || '上传失败')
//       }
//     } catch (error) {
//       message.error('上传失败：' + (error as Error).message)
//     }
//   }
// }

// 上传图片-删除
const handleRemoveUpload = async (fileinfo: any) => {
  // console.log(fileinfo.id)
  // const id = fileinfo.id
  // if (!id) {
  //   const id = fileinfo.response.data.id
  //   console.log(id)
  //   const res = await deleteFileUsingPost({ id })
  // }
}

const previewVisible = ref<boolean>(false)
const previewImage = ref<string | undefined>('')

function getBase64(file: File) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = (error) => reject(error)
  })
}

// 上传文件-预览图片
const handlePreview = async (file: FileItem) => {
  if (file.url) {
    previewImage.value = file.url
    previewVisible.value = true
  } else if (file.originFileObj) {
    try {
      file.preview = (await getBase64(file.originFileObj)) as string
      previewImage.value = file.preview
      previewVisible.value = true
    } catch (error) {
      message.error('预览图片失败')
    }
  } else {
    message.error('文件对象不存在，无法预览')
  }
}

const onUploadImg = async (files: File[], callback: (urls: string[]) => void) => {
  try {
    const uploadPromises = files.map(async (file) => {
      const form = new FormData()
      form.append('file', file)
      const response = await myAxios.post(uploadUrl, form, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      return response.data.data.fileUrl
    })
    const urls = await Promise.all(uploadPromises)
    callback(urls)
  } catch (error) {
    message.error('图片上传失败')
    callback([])
  }
}

const customUpload = async (options: any) => {
  const { file, onProgress, onSuccess, onError } = options
  try {
    const formData = new FormData()
    formData.append('file', file)
    const response = await myAxios.post(actionUrl, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress: (progressEvent) => {
        const percent =
          progressEvent.total && progressEvent.total > 0
            ? (progressEvent.loaded / progressEvent.total) * 100
            : 0
        onProgress({ percent })
      },
    })
    if (response.data.code === 0) {
      formState.cover = response.data.data.fileUrl
      onSuccess(response.data.data)
      message.success('封面图片上传成功')
    } else {
      throw new Error(response.data.msg)
    }
  } catch (error) {
    onError(error)
  }
}

</script>

<style lang="less">
.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }

  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh);
  }

  .ant-modal-body {
    flex: 1;
  }
}

//上传
.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }

  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh);
  }

  .ant-modal-body {
    flex: 1;
  }
}
</style>
