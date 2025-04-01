// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addUser POST /api/user/adduser */
export async function addUserUsingPost(body: API.EditUserDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/adduser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteUser POST /api/user/deleteuser */
export async function deleteUserUsingPost(body: API.IdDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/deleteuser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** editUser POST /api/user/edituser */
export async function editUserUsingPost(body: API.EditUserDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/edituser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getUserInfo GET /api/user/getuserinfo */
export async function getUserInfoUsingGet(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/getuserinfo', {
    method: 'GET',
    ...(options || {}),
  })
}

/** login POST /api/user/login */
export async function loginUsingPost(body: API.UserLoginDTO, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** logout POST /api/user/logout */
export async function logoutUsingPost(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  })
}

/** queryUserList POST /api/user/queryuserlist */
export async function queryUserListUsingPost(
  body: API.UserQueryDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/user/queryuserlist', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateUser POST /api/user/updateuser */
export async function updateUserUsingPost(
  body: API.UpdateUserDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/user/updateuser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** userRegister POST /api/user/userregister */
export async function userRegisterUsingPost(
  body: API.UserRegisterDTO,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/user/userregister', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
