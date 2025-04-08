// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** getCaptcha GET /api/home/getcaptcha */
export async function getCaptchaUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/home/getcaptcha', {
    method: 'GET',
    ...(options || {}),
  })
}

/** getHomeArticleById GET /api/home/gethomearticlebyid */
export async function getHomeArticleByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getHomeArticleByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/home/gethomearticlebyid', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** getHomeCategoryAll GET /api/home/gethomecategoryall */
export async function getHomeCategoryAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/home/gethomecategoryall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryArticleCarouselAll GET /api/home/queryarticlecarouselall */
export async function queryArticleCarouselAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/home/queryarticlecarouselall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryCommentByArticleId POST /api/home/querycommentbyarticleid */
export async function queryCommentByArticleIdUsingPost(
  body: API.queryCommentDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/home/querycommentbyarticleid', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** queryHomeTagAll GET /api/home/queryhometagall */
export async function queryHomeTagAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/home/queryhometagall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryMyCollect POST /api/home/querymycollect */
export async function queryMyCollectUsingPost(
  body: API.QueryMyCollectDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/home/querymycollect', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** queryRecommendArticle GET /api/home/queryrecommendarticle */
export async function queryRecommendArticleUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/home/queryrecommendarticle', {
    method: 'GET',
    ...(options || {}),
  })
}

/** searchFromEs POST /api/home/searchfromes */
export async function searchFromEsUsingPost(
  body: API.SearchArticleByKeywordDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/home/searchfromes', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** submitComment POST /api/home/submitcomment */
export async function submitCommentUsingPost(
  body: API.CommentHomeDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/home/submitcomment', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
