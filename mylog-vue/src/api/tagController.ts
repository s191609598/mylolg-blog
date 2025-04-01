// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addTag POST /api/tag/addtag */
export async function addTagUsingPost(body: API.EditTagDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/tag/addtag', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteTagById POST /api/tag/deletetagbyid */
export async function deleteTagByIdUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/tag/deletetagbyid', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** queryTagAll GET /api/tag/querytagall */
export async function queryTagAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/tag/querytagall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryTagPageList POST /api/tag/querytagpagelist */
export async function queryTagPageListUsingPost(
  body: API.TagPageListDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/tag/querytagpagelist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateTag POST /api/tag/updatetag */
export async function updateTagUsingPost(body: API.EditTagDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/tag/updatetag', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
