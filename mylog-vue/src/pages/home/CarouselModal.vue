<!--首页-轮播图-->
<template>
  <div id="carouselModal">
    <el-carousel :interval="5000" arrow="always" style="height: 100%">
      <el-carousel-item
        v-for="item in listData"
        :key="item"
        @click="() => item.id !== undefined && toArticle(item.id)"
        style="cursor: pointer"
      >
        <img id="imgbox" :src="item.cover" />
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
/deep/ .el-carousel__container {
  height: 100%;
}

#carouselModal {
  background: black;
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
</style>
