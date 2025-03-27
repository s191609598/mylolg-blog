<template>
  <a-modal
    :visible="visible"
    :title="isEdit ? '编辑用户' : '新增用户'"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="提交"
    cancelText="取消"
  >
    <a-form :model="formState" :label-col="{ span: 6 }" :wrapper-col="{ span: 14 }">
      <!--      <a-form-item v-if="isEdit" label="用户ID" name="id">-->
      <!--        <a-input v-model:value="formState.id" :disabled="isEdit" />-->
      <!--      </a-form-item>-->
      <a-form-item label="用户名称" name="userName">
        <a-input v-model:value="formState.userName" />
      </a-form-item>
      <a-form-item label="用户账号" name="userAccount">
        <a-input v-model:value="formState.userAccount" />
      </a-form-item>
      <a-form-item v-if="!isEdit" label="用户密码" name="userPassword">
        <a-input v-model:value="formState.userPassword" />
      </a-form-item>
      <a-form-item label="手机号码" name="phonenumber">
        <a-input v-model:value="formState.phonenumber" />
      </a-form-item>
      <a-form-item label="用户邮箱" name="email">
        <a-input v-model:value="formState.email" />
      </a-form-item>
      <a-form-item label="用户简介" name="userProfile">
        <a-input v-model:value="formState.userProfile" />
      </a-form-item>
      <a-form-item label="用户备注" name="remark">
        <a-input v-model:value="formState.remark" />
      </a-form-item>
      <a-form-item label="用户性别" name="sex">
        <a-select v-model:value="formState.sex">
          <a-select-option :value="0">男</a-select-option>
          <a-select-option :value="1">女</a-select-option>
          <a-select-option :value="2">未知</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="用户角色" name="userRole">
        <a-select v-model:value="formState.userRole">
          <a-select-option value="user">普通用户</a-select-option>
          <a-select-option value="admin">管理员</a-select-option>
          <a-select-option value="ban">非法用户</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, defineEmits, defineProps, reactive, watch } from 'vue'

const props = defineProps<{
  visible: boolean
  user: API.EditUserDTO | null
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'submit', value: API.EditUserDTO): void
}>()

const formState = reactive<API.EditUserDTO>({
  id: undefined,
  userAccount: '',
  userName: '',
  phonenumber: '',
  email: '',
  userProfile: '',
  remark: '',
  userRole: '',
  userPassword: '',
  sex: undefined,
})

const isEdit = computed(() => !!props.user?.id)

watch(
  () => props.user,
  (newVal) => {
    if (newVal) {
      Object.assign(formState, newVal)
      // 将数字转换为对应的中文文本
      // formState.sex = sexMap[(formState.sex ?? 2) as 0 | 1 | 2]
      // formState.sex = sexMap[formState.sex as keyof typeof sexMap]
    } else {
      Object.assign(formState, {
        id: undefined,
        userAccount: '',
        userName: '',
        phonenumber: '',
        email: '',
        userProfile: '',
        remark: '',
        userRole: '',
        userPassword: '',
        sex: undefined,
      })
    }
  },
  { immediate: true },
)

const handleOk = () => {
  // 将中文文本转换回数字
  // const submitData = { ...formState, sex: sexMapReverse[formState.sex] }
  const submitData = { ...formState}
  emit('submit', submitData)
  emit('update:visible', false)
}

const handleCancel = () => {
  emit('update:visible', false)
}

const sexMap = {
  0: '男',
  1: '女',
  2: '未知',
}

const sexMapReverse = {
  男: 0,
  女: 1,
  未知: 2,
}
</script>
