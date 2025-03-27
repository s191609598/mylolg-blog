// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** getHomeArticleById GET /mylog/home/gethomearticlebyid */
export async function getHomeArticleByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getHomeArticleByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/home/gethomearticlebyid', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** getHomeCategoryAll GET /mylog/home/gethomecategoryall */
export async function getHomeCategoryAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/home/gethomecategoryall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryArticleCarouselAll GET /mylog/home/queryarticlecarouselall */
export async function queryArticleCarouselAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/home/queryarticlecarouselall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryCommentByArticleId POST /mylog/home/querycommentbyarticleid */
export async function queryCommentByArticleIdUsingPost(
  body: API.queryCommentDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/home/querycommentbyarticleid', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** queryHomeTagAll GET /mylog/home/queryhometagall */
export async function queryHomeTagAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/home/queryhometagall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryRecommendArticle GET /mylog/home/queryrecommendarticle */
export async function queryRecommendArticleUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/home/queryrecommendarticle', {
    method: 'GET',
    ...(options || {}),
  })
}

/** searchFromEs POST /mylog/home/searchfromes */
export async function searchFromEsUsingPost(
  body: API.SearchArticleByKeywordDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/home/searchfromes', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** submitComment POST /mylog/home/submitcomment */
export async function submitCommentUsingPost(
  body: API.CommentHomeDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/home/submitcomment', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
