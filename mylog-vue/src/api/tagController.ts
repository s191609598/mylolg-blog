// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addTag POST /mylog/tag/addtag */
export async function addTagUsingPost(body: API.EditTagDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/tag/addtag', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteTagById POST /mylog/tag/deletetagbyid */
export async function deleteTagByIdUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/tag/deletetagbyid', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** queryTagAll GET /mylog/tag/querytagall */
export async function queryTagAllUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/tag/querytagall', {
    method: 'GET',
    ...(options || {}),
  })
}

/** queryTagPageList POST /mylog/tag/querytagpagelist */
export async function queryTagPageListUsingPost(
  body: API.TagPageListDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/tag/querytagpagelist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateTag POST /mylog/tag/updatetag */
export async function updateTagUsingPost(body: API.EditTagDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/tag/updatetag', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
