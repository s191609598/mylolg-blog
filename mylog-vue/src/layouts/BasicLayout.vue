<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <!--      头部-->
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <!--      内容-->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <!--      底部-->
      <a-layout-footer class="footer">
        <a href="https://gitee.com/pss733/mylog-blog" target="_blank">踏上取经路，比抵达灵山更重要</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script lang="ts" setup>
import GlobalHeader from '@/components/GlobalHeader.vue'
</script>

<style scoped>
#basicLayout .footer {
  background: #efefef;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}

#basicLayout .content {
  padding: 20px;
  background: linear-gradient(to bottom, #fefefe, #fff);
  margin-bottom: 28px;
}

#basicLayout .header {
  background: white;
  color: unset;
  margin-bottom: 16px;
  padding-inline: 20px;
}
</style>
