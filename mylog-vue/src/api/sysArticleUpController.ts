// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** noArticle GET /api/articleup/no */
export async function noArticleUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.noArticleUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/articleup/no', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** upArticle GET /api/articleup/up */
export async function upArticleUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.upArticleUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/articleup/up', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
