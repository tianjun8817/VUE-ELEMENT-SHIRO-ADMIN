import request from '@/utils/request'
import Qs from 'qs'

export function fetchRolePermList(data) {
  return request({
    url: '/api/sys/role/perm-infos',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function fetchRoleUserList(data) {
  return request({
    url: '/api/sys/role/user-infos',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function fetchList(data) {
  return request({
    url: '/api/sys/role/infos',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function save(params) {
  return request({
    url: '/api/sys/role/save',
    method: 'post',
    data: params
  })
}

export function view(params) {
  return request({
    url: '/api/sys/role/view',
    method: 'post',
    data: Qs.stringify(params)
  })
}

export function savePerms(params) {
  return request({
    url: '/api/sys/role/save-perms',
    method: 'post',
    data: Qs.stringify(params, { indices: false })
  })
}

export function roleMenus(params) {
  return request({
    url: '/api/sys/role/role-menus',
    method: 'post',
    data: Qs.stringify(params)
  })
}

export function saveMenus(params) {
  return request({
    url: '/api/sys/role/save-menus',
    method: 'post',
    data: Qs.stringify(params, { indices: false })
  })
}
