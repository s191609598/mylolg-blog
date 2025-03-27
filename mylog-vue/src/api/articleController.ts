// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addArticle POST /mylog/article/addarticle */
export async function addArticleUsingPost(
  body: API.EditArticleDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/article/addarticle', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteArticle POST /mylog/article/deletearticle */
export async function deleteArticleUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/article/deletearticle', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getArticleById GET /mylog/article/getarticlebyid */
export async function getArticleByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getArticleByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/article/getarticlebyid', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** queryArticleList POST /mylog/article/queryarticlelist */
export async function queryArticleListUsingPost(
  body: API.QueryArticleDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/article/queryarticlelist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateArticle POST /mylog/article/updatearticle */
export async function updateArticleUsingPost(
  body: API.EditArticleDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/article/updatearticle', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateArticleStatus POST /mylog/article/updatearticlestatus */
export async function updateArticleStatusUsingPost(
  body: API.UpdateArticleStatusDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/article/updatearticlestatus', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
