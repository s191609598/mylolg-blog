// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addCategory POST /mylog/category/addCategory */
export async function addCategoryUsingPost(
  body: API.EditCategoryDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/category/addCategory', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteCategoryById POST /mylog/category/deletecategorybyid */
export async function deleteCategoryByIdUsingPost(
  body: API.IdDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/category/deletecategorybyid', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getCategoryAll GET /mylog/category/querycategoryall */
export async function getCategoryAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/category/querycategoryall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryCategoryPageList POST /mylog/category/queryCategorypagelist */
export async function queryCategoryPageListUsingPost(
  body: API.CategoryPageListDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/category/queryCategorypagelist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateCategory POST /mylog/category/updateCategory */
export async function updateCategoryUsingPost(
  body: API.EditCategoryDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/category/updateCategory', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
