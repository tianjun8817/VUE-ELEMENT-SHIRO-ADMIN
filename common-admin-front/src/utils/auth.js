import Cookies from 'js-cookie'
import store from '@/store'

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function hasRole(role) {
  return store.getters.roles.indexOf(role) > -1 || store.getters.roles.indexOf('SUPER_ADMIN') > -1
}

export function hasPerm(perm) {
  return store.getters.perms.indexOf(perm) > -1 || store.getters.roles.indexOf('SUPER_ADMIN') > -1
}
