// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addCategory POST /api/category/addCategory */
export async function addCategoryUsingPost(
  body: API.EditCategoryDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/category/addCategory', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteCategoryById POST /api/category/deletecategorybyid */
export async function deleteCategoryByIdUsingPost(
  body: API.IdDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/category/deletecategorybyid', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getCategoryAll GET /api/category/querycategoryall */
export async function getCategoryAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/category/querycategoryall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryCategoryPageList POST /api/category/queryCategorypagelist */
export async function queryCategoryPageListUsingPost(
  body: API.CategoryPageListDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/category/queryCategorypagelist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateCategory POST /api/category/updateCategory */
export async function updateCategoryUsingPost(
  body: API.EditCategoryDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/category/updateCategory', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
