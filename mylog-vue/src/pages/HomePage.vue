<template>
  <div id="homePage">
    <a-row>
      <a-col :span="4"></a-col>
      <a-col :span="10">
        <a-layout style="background: white">
          <a-layout-header class="header">
            <CarouselModal />
          </a-layout-header>
          <a-layout-content class="content">
            <ListModal />
          </a-layout-content>
          <a-layout-footer class="footer">
            <div id="bottomTime">
              <div id="bottomTime">
                <h1>本站居然运行了 {{ runningTime.days }} 天 {{ runningTime.hours }} 时 {{ runningTime.minutes }} 分 {{ runningTime.seconds }} 秒</h1>
              </div>
            </div>
          </a-layout-footer>
        </a-layout>
      </a-col>
      <a-col :span="6">
        <div id="rightCard">
          <CardModal />
        </div>
      </a-col>
      <a-col :span="4"></a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import CarouselModal from '@/pages/home/CarouselModal.vue'
import ListModal from '@/pages/home/ListModal.vue'
import CardModal from '@/pages/home/CardModal.vue'
import { onMounted, onUnmounted, ref } from 'vue'


// 设置网站开始运行的时间（根据实际情况修改）
const startTime = ref(new Date('2025-01-01T00:00:00')) // 替换为你的实际上线时间

const runningTime = ref({
  days: 0,
  hours: 0,
  minutes: 0,
  seconds: 0
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

onMounted(() => {
  calculateRuntime() // 立即执行一次
  timer = setInterval(calculateRuntime, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})

</script>
<style scoped>
#homePage {
  text-align: center;
}

#homePage .footer {
  background: white;
  padding-top: 20px;
  text-align: center;

}

#homePage .content {
  background: linear-gradient(to bottom, #fefefe, #fff);
  margin-left: 10px;
  margin-right: 10px;

}

#homePage .header {
  background: white;
  color: unset;
  margin-bottom: 16px;
  padding-right: 16px;
  padding-left: 16px;
  padding-bottom: 16px;
  width: 100%; /* 使头部占满整个宽度 */
  height: 400px; /* 设置一个固定的正方形高度 */
}

#rightCard {
  height: 100%;
  width: 100%;
}

#bottomTime h1 {
  font-size: 18px;
  color: #666;
  animation: pulse 1s infinite alternate;
}

@keyframes pulse {
  from { opacity: 0.8; }
  to { opacity: 1; }
}
</style>
