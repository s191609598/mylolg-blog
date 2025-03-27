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
              <h1>本站居然运行了 1269 天 5 时 15 分 54 秒</h1>
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
</style>
