import request from '@/utils/request'
import Qs from 'qs'

export function login(data) {
  return request({
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    url: '/api/sys/login',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function getInfo() {
  return request({
    url: '/api/sys/user/info',
    method: 'post'
  })
}

export function logout() {
  return request({
    url: '/api/sys/logout',
    method: 'post'
  })
}

export function fetchList(data) {
  return request({
    url: '/api/sys/user/infos',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function create(params) {
  return request({
    url: '/api/sys/user/cread',
    method: 'post',
    data: params
  })
}

export function view(params) {
  return request({
    url: '/api/sys/user/view',
    method: 'post',
    data: Qs.stringify(params)
  })
}

export function fetchUserRoleList(params) {
  return request({
    url: '/api/sys/user/role-infos',
    method: 'post',
    data: Qs.stringify(params)
  })
}

export function fetchUserMenuTree(params) {
  return request({
    url: '/api/sys/user/menu-tree',
    method: 'post',
    data: Qs.stringify(params)
  })
}

export function saveRoles(params) {
  return request({
    url: '/api/sys/user/save-roles',
    method: 'post',
    data: Qs.stringify(params, { indices: false })
  })
}

export function save(params) {
  return request({
    url: '/api/sys/user/save',
    method: 'post',
    data: params
  })
}

export function resetPassword(params) {
  return request({
    url: '/api/sys/user/reset-password',
    method: 'post',
    data: Qs.stringify(params)
  })
}
