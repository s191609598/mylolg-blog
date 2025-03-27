// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addUser POST /mylog/user/adduser */
export async function addUserUsingPost(body: API.EditUserDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/user/adduser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteUser POST /mylog/user/deleteuser */
export async function deleteUserUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/user/deleteuser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** editUser POST /mylog/user/edituser */
export async function editUserUsingPost(body: API.EditUserDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/user/edituser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getUserInfo GET /mylog/user/getuserinfo */
export async function getUserInfoUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/user/getuserinfo', {
    method: 'GET',
    ...(options || {}),
  })
}

/** login POST /mylog/user/login */
export async function loginUsingPost(body: API.UserLoginDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** logout POST /mylog/user/logout */
export async function logoutUsingPost(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/mylog/user/logout', {
    method: 'POST',
    ...(options || {}),
  })
}

/** queryUserList POST /mylog/user/queryuserlist */
export async function queryUserListUsingPost(
  body: API.UserQueryDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/user/queryuserlist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateUser POST /mylog/user/updateuser */
export async function updateUserUsingPost(
  body: API.UpdateUserDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/user/updateuser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** userRegister POST /mylog/user/userregister */
export async function userRegisterUsingPost(
  body: API.UserRegisterDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/mylog/user/userregister', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
