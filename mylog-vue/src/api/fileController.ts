// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** deleteFile POST /api/file/deletefile */
export async function deleteFileUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/file/deletefile', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getFile GET /api/file/getfile */
export async function getFileUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFileUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/file/getfile', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** upload POST /api/file/upload */
export async function uploadUsingPost(body: {}, file?: File, options?: { [key: string]: any }) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, JSON.stringify(item))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })


}

/** uploadCover POST /api/file/uploadcover */
export async function uploadCoverUsingPost(
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, JSON.stringify(item))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })


}
