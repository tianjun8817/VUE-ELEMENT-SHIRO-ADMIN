import request from '@/utils/request'
import Qs from 'qs'

export function fetchOperList(data) {
  return request({
    url: '/api/log/oper-infos',
    method: 'post',
    data: Qs.stringify(data)
  })
}
