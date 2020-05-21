import request from '@/utils/request'
import Qs from 'qs'

export function fetchPermRoleList(data) {
  return request({
    url: '/api/sys/perm/role-infos',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function fetchList(data) {
  return request({
    url: '/api/sys/perm/infos',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function save(params) {
  return request({
    url: '/api/sys/perm/save',
    method: 'post',
    data: params
  })
}

export function view(params) {
  return request({
    url: '/api/sys/perm/view',
    method: 'post',
    data: Qs.stringify(params)
  })
}
