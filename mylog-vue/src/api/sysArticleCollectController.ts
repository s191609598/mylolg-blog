// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** collectArticle GET /api/articlecollect/collectarticle */
export async function collectArticleUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.collectArticleUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/articlecollect/collectarticle', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** noArticle GET /api/articlecollect/no */
export async function noArticleUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.noArticleUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/articlecollect/no', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
