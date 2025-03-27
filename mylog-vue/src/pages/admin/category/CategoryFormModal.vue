<template>
  <a-modal
    :visible="visible"
    :title="isEdit ? '编辑' : '新增'"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="提交"
    cancelText="取消"
  >
    <a-form :model="formState" :label-col="{ span: 6 }" :wrapper-col="{ span: 14 }">
      <a-form-item label="名称" name="name">
        <a-input v-model:value="formState.name" />
      </a-form-item>
      <a-form-item label="排序" name="sort">
        <a-input-number v-model:value="formState.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, defineEmits, defineProps, reactive, watch } from 'vue'

const props = defineProps<{
  visible: boolean
  category: API.EditCategoryDTO | null
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'submit', value: API.EditCategoryDTO): void
}>()

const formState = reactive<API.EditCategoryDTO>({
  id: undefined,
  name: '',
  sort: undefined,
})

const isEdit = computed(() => !!props.category?.id)

watch(
  () => props.category,
  (newVal) => {
    if (newVal) {
      Object.assign(formState, newVal)
      // 将数字转换为对应的中文文本
    } else {
      Object.assign(formState, {
        id: undefined,
        name: '',
        sort: undefined,
      })
    }
  },
  { immediate: true },
)

const handleOk = () => {
  // 将中文文本转换回数字
  const submitData = { ...formState }
  emit('submit', submitData)
  emit('update:visible', false)
}

const handleCancel = () => {
  emit('update:visible', false)
}
</script>
