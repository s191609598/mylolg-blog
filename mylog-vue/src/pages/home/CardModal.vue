<!--首页-右边卡片-->
<template>
  <div id="cardModal">
    <a-space direction="vertical" :style="{ width: '100%', height: '100%' }">
      <a-layout class="cardLayout" :style="{ background: '#fff' }">
        <a-layout-header class="cardHeader">
          <a-card id="cardHeaderValue" title="" :bordered="true" :hoverable="false">
            <template #cover></template>
            <a-card-meta title="联系作者" description="">
              <template #avatar>
                <a-avatar :src="imgSrc" />
              </template>
            </a-card-meta>
            <a-divider />
            <a-card title="">
              <a-card-grid style="width: 25%; text-align: center; font-size: 30px">
                <a href="https://gitee.com/pss733/mylog-blog" target="_blank" class="icon-link">
                <GithubOutlined />
                </a>
              </a-card-grid>
              <a-card-grid style="width: 25%; text-align: center">
                <a href="https://gitee.com/pss733/mylog-blog" target="_blank" class="icon-link">
                <svg
                  :style="{
                    verticalAlign: '-1.80em',
                  }"
                  t="1742370791231"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="901"
                  width="31"
                  height="31"
                >
                  <path
                    d="M512 1024q-104 0-199-40-92-39-163-110T40 711Q0 616 0 512t40-199Q79 221 150 150T313 40q95-40 199-40t199 40q92 39 163 110t110 163q40 95 40 199t-40 199q-39 92-110 163T711 984q-95 40-199 40z m259-569H480q-10 0-17.5 7.5T455 480v64q0 10 7.5 17.5T480 569h177q11 0 18.5 7.5T683 594v13q0 31-22.5 53.5T607 683H367q-11 0-18.5-7.5T341 657V417q0-31 22.5-53.5T417 341h354q11 0 18-7t7-18v-63q0-11-7-18t-18-7H417q-38 0-72.5 14T283 283q-27 27-41 61.5T228 417v354q0 11 7 18t18 7h373q46 0 85.5-22.5t62-62Q796 672 796 626V480q0-10-7-17.5t-18-7.5z"
                    p-id="902"
                  ></path>
                </svg>
                </a>
              </a-card-grid>
              <a-card-grid style="width: 25%; text-align: center; font-size: 30px">
                <QqOutlined />
              </a-card-grid>
              <a-card-grid style="width: 25%; text-align: center; font-size: 30px">
                <MailOutlined />
              </a-card-grid>
            </a-card>
          </a-card>
        </a-layout-header>
        <a-layout-content class="cardContent">
          <a-card id="cardContentValue" title="公告" :bordered="true" :hoverable="true">
            <p>本站基于 Vue3 + Ant Design Vue + Spring Boot 开发，源码已开源。</p>
            <p>有问题欢迎通过邮箱或其他社交方式联系我。</p>
          </a-card>
        </a-layout-content>
        <a-layout-footer class="cardFooter">
          <a-card id="cardFooterValue" title="推荐文章" :bordered="true" :hoverable="false">
            <a-list item-layout="horizontal" :data-source="list">
              <template #renderItem="{ item }">
                <a-card
                  hoverable
                  style="width: 100%; height: 180px; margin-bottom: 20px"
                  @click="handleCardClick(item.id)"
                >
                  <a-row>
                    <a-col id="cardaImagecol" :span="7">
                      <a-image id="cardaImage" :preview="false" :src="item.cover" :width="150" :onerror="setDefaultImage"/>
                    </a-col>
                    <a-col :span="17">
                      <a-card-meta :title="item.title">
                        <template #description> 发布时间：{{ item.createTime }}</template>
                      </a-card-meta>
                    </a-col>
                  </a-row>
                </a-card>
              </template>
            </a-list>
          </a-card>
        </a-layout-footer>
      </a-layout>
    </a-space>
  </div>
</template>

<script setup lang="ts">
import { GithubOutlined, MailOutlined, QqOutlined } from '@ant-design/icons-vue'
import { onMounted, ref } from 'vue'
import { queryRecommendArticleUsingGet } from '@/api/homeController.ts'
import router from '@/router'
import imgSrc from '@/assets/wangcai.jpg';
import imgerror from '@/assets/imgERROR.jpg';

// 文章详情
const list = ref<API.RecommendArticleVO[]>([])
// 处理卡片点击事件
const handleCardClick = (id: number) => {
  router.push({ name: '文章详情', params: { id } })
}
const queryRecommendArticle = async () => {
  const res = await queryRecommendArticleUsingGet()
  if (res.data.code === 0 && res.data.data) {
    list.value = res.data.data
  }
  // //console.log(res)
}
// 生命周期钩子
onMounted(() => {
  queryRecommendArticle()
})

// 设置默认图片
const setDefaultImage = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = imgerror // 替换为你的默认图片路径
  img.onerror = null // 避免无限循环
}



</script>

<style scoped>
#cardModal {
  width: 100%;
  height: 100%;
  background: white;
}

.cardHeader {
  textalign: center;
  width: 100%;
  height: 100%;
  background: white;
  padding-inline: 0px;
}

#cardHeaderValue {
  width: 100%;
}

.cardContent {
  textalign: center;
  padding-top: 20px;
  padding-inline: 0px;
}

.cardFooter {
  textalign: center;
  padding-top: 20px;
  padding-inline: 0px;
  background: #fff;
}

#cardaImagecol {
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
  padding: 8px; /* 可选的内边距 */
  padding-bottom: 80px;
}

#cardaImage {
  object-fit: cover;
  height: 100%;
  width: 100%;
  position: absolute; /* 绝对定位确保完全填充 */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%); /* 精确居中 */
}

a.icon-link {
  color: inherit;
  text-decoration: none;
}
</style>
