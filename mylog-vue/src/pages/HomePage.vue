<template>
  <div id="homePage">
    <a-row :gutter="[16, { xs: 8, sm: 16 }]">
      <!-- 左侧空白列（移动端隐藏） -->
      <a-col :xs="0" :sm="4" :md="4" />

      <!-- 主内容列 -->
      <a-col :xs="24" :sm="10" :md="10" :style="{ maxWidth: '100%' }">
        <a-layout class="main-content">
          <!-- 轮播图仅在非移动端显示 -->
          <a-layout-header v-if="!xsMode" class="header">
            <CarouselModal />
          </a-layout-header>

          <!-- 文章列表始终显示 -->
          <a-layout-content class="content">
            <ListModal />
          </a-layout-content>
        </a-layout>
      </a-col>

      <!-- 右侧卡片（移动端隐藏） -->
      <a-col v-if="!xsMode" :xs="0" :md="4" :xl="6" :style="{ maxWidth: '100%' }">
        <div id="rightCard">
          <CardModal />
        </div>
      </a-col>

      <!-- 右侧空白列（移动端隐藏） -->
      <a-col :xs="0" :sm="4" :md="4" />
    </a-row>

    <!-- 移动端专属布局 -->
    <div v-if="xsMode" class="mobile-wrapper">
      <ListModal class="mobile-list" />
    </div>

    <!-- 底部信息 -->
    <div class="footer">
      <a href="https://beian.miit.gov.cn" target="_blank" rel="noopener noreferrer"
        >{{ beianhao }}</a
      >
      <div id="bottomTime">
        <h1>
          本站居然运行了
          {{ runningTime.days }} 天 {{ runningTime.hours }} 时 {{ runningTime.minutes }} 分
          {{ runningTime.seconds }} 秒
        </h1>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import CarouselModal from '@/pages/home/CarouselModal.vue'
import ListModal from '@/pages/home/ListModal.vue'
import CardModal from '@/pages/home/CardModal.vue'
import { onMounted, onUnmounted, ref } from 'vue'
import { getInitUsingGet } from '@/api/homeController.ts'

const xsMode = ref(window.innerWidth <= 576)

const handleResize = () => {
  xsMode.value = window.innerWidth <= 576
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

// 网站运行时间
const startTime = ref(new Date('2025-01-01T00:00:00'))
const runningTime = ref({
  days: 0,
  hours: 0,
  minutes: 0,
  seconds: 0,
})
let timer: number

const calculateRuntime = () => {
  const now = new Date()
  const diff = now.getTime() - startTime.value.getTime()

  runningTime.value.seconds = Math.floor(diff / 1000) % 60
  runningTime.value.minutes = Math.floor(diff / (1000 * 60)) % 60
  runningTime.value.hours = Math.floor(diff / (1000 * 60 * 60)) % 24
  runningTime.value.days = Math.floor(diff / (1000 * 60 * 60 * 24))
}

const beianhao = ref('')

const getInit = async () => {
  try {
    const res = await getInitUsingGet()
    if (res.data.code === 0 && res.data.data) {
      console.log('res.data.data', res.data.data)
      beianhao.value = res.data.data.ba
    }
  } catch (error) {
    console.error('初始化失败:', error)
  }
}

onMounted(() => {
  calculateRuntime()
  getInit()
  timer = setInterval(calculateRuntime, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
#homePage {
  text-align: center;
  padding: 0 16px;
}

.main-content {
  background: white;
  margin-bottom: 24px;
}

.header {
  height: 400px;
  width: 1000px;
  margin-left: 28px;
  padding-inline: 0;
}

@media (max-width: 1920px) {
  .header {
    height: 340px;
    width: 756px;
    margin-left: 28px;
    padding-inline: 0;
  }
}

.content {
  padding: 24px;
}

#rightCard {
  padding: 16px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.footer {
  background: white;
  padding: 20px 0;
  text-align: center;
  margin-top: 24px;
}

#bottomTime h1 {
  font-size: 18px;
  color: #666;
  animation: pulse 1s infinite alternate;
}

@keyframes pulse {
  from {
    opacity: 0.8;
  }
  to {
    opacity: 1;
  }
}

/* 移动端样式 */
@media (max-width: 576px) {
  .main-content,
  #rightCard {
    display: none;
  }

  .mobile-wrapper {
    padding: 0 16px;
  }

  .mobile-list {
    padding: 24px 0;
  }

  #bottomTime h1 {
    font-size: 14px;
    padding: 8px;
    text-align: center;
  }
}
</style>
