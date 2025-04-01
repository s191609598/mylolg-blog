// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addArticle POST /api/article/addarticle */
export async function addArticleUsingPost(
  body: API.EditArticleDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/article/addarticle', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteArticle POST /api/article/deletearticle */
export async function deleteArticleUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/article/deletearticle', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getArticleById GET /api/article/getarticlebyid */
export async function getArticleByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getArticleByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/article/getarticlebyid', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** queryArticleList POST /api/article/queryarticlelist */
export async function queryArticleListUsingPost(
  body: API.QueryArticleDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/article/queryarticlelist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateArticle POST /api/article/updatearticle */
export async function updateArticleUsingPost(
  body: API.EditArticleDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/article/updatearticle', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateArticleStatus POST /api/article/updatearticlestatus */
export async function updateArticleStatusUsingPost(
  body: API.UpdateArticleStatusDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/article/updatearticlestatus', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
