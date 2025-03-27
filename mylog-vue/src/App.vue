<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { provide, ref } from 'vue'
const activeReplyId = ref<number | null>(0);
const isReply = ref<boolean | null>(false);
// 提供全局变量
provide('activeReplyId', activeReplyId);
provide('isReply', isReply);
</script>
<style scoped></style>
