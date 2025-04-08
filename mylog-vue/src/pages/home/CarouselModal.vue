<!--首页-轮播图-->
<template>
    <div id="carouselModal">
      <el-carousel :interval="5000" arrow="always" style="height: 100%">
        <el-carousel-item
          v-for="item in listData"
          :key="item.id"
          @click="() => item.id !== undefined && toArticle(item.id)"
          style="cursor: pointer"
        >
          <img id="imgbox" :src="item.cover" alt="" />
          <div id="textbox">
            <span id="titlebox" style="color: white"
              ><h1 id="titletext">{{ item.title }}</h1></span
            >
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
</template>

<script setup lang="ts">
//获取文章评论
import { queryArticleCarouselAllUsingGet } from '@/api/homeController.ts'
import { onMounted, ref } from 'vue'
import router from '@/router'

const listData = ref<API.ArticleCarouselVO[]>([])

const fetchArticleCarouse = async () => {
  const res = await queryArticleCarouselAllUsingGet()
  if (res.data.code === 0 && res.data.data) {
    listData.value = res.data.data.map((record: API.ArticleCarouselVO) => ({
      ...record,
    }))
  }
}

const toArticle = (id: number) => {
  // //console.log(id)
  router.push({ name: '文章详情', params: { id } })
}

// 生命周期钩子
onMounted(() => {
  fetchArticleCarouse()
})
</script>

<style scoped>

#carouselModal {
  background: black;
  height: 100%;
  width: 100%;

}

:deep .el-carousel__container {
  height: 100%;
}
#titlebox {
  position: absolute;
  top: 11px;
  right: 0px;
  height: 100px;
  width: 100%;
}

#imgbox {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;


  img {
    width: 100%;
    height: 100%;
    width: auto;
    height: auto;
    min-width: 100%;
    min-height: 100%;
    object-fit: cover;
    transition: transform 0.5s ease;
    transform: scale(1);
  }

  #imgbox:hover img {
    transform: scale(1.1);
  }
}

#textbox {
  position: absolute;
  top: 250px;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, #0003 10%, #000c);
}

#titletext {
  font-size: 40px;
  color: white;
  font-weight: bold;
}

/* 修改图片容器样式 */
#imgbox {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  /* 添加以下新样式 */
  display: flex;
  justify-content: center;
  align-items: center;
}
/* 修改图片样式 */
#imgbox img {
  width: auto;
  height: auto;
  min-width: 100%;
  min-height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease; /* 添加过渡动画 */
  transform: scale(1); /* 初始缩放值 */
}

/* 如果需要悬停放大效果 */
#imgbox:hover img {
  transform: scale(1.1); /* 等比例放大10% */
}

/* 消除可能的内边距 */
:deep .el-carousel__item {
  padding: 0 !important;
  margin: 0 !important;
}

:deep .el-carousel__container {
  height: 100%;
  perspective: 1000px; /* 增强3D渲染效果 */
}
/* 修改图片展示方式 */
#imgbox img {
  width: 100%;  /* 改为强制宽度填充 */
  height: 100%; /* 改为强制高度填充 */
  min-width: unset; /* 移除最小限制 */
  min-height: unset;
  object-fit: cover; /* 保持原有效果 */

}

:where(.css-dev-only-do-not-override-1p3hq3p).ant-layout .ant-layout-header {
  height: 64px;
  /* padding-inline: 50px; */
  color: rgba(0, 0, 0, 0.88);
  line-height: 64px;
  background: #001529;
}
</style>
