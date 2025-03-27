// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** deleteFile POST /mylog/file/deletefile */
export async function deleteFileUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/file/deletefile', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getFile GET /mylog/file/getfile */
export async function getFileUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFileUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/file/getfile', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** upload POST /mylog/file/upload */
// export async function uploadUsingPost(body: {}, file?: File, options?: { [key: string]: any }) {
//   const formData = new FormData()
//
//   if (file) {
//     formData.append('file', file)
//   }
//
//   Object.keys(body).forEach((ele) => {
//     const item = (body as any)[ele]
//
//     if (item !== undefined && item !== null) {
//       if (typeof item === 'object' && !(item instanceof File)) {
//         if (item instanceof Array) {
//           item.forEach((f) => formData.append(ele, f || ''))
//         } else {
//           formData.append(ele, JSON.stringify(item))
//         }
//       } else {
//         formData.append(ele, item)
//       }
//     }
//   })
//
//   return request<Record<string, any>>('/mylog/file/upload', {
//     method: 'POST',
//     data: formData,
//     requestType: 'form',
//     ...(options || {}),
//   })
// }
