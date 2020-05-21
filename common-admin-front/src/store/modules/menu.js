import { constantRoutes } from '@/router'

/**
 * Filter asynchronous routing tables by recursion
 * @param maps 后台返回的菜单
 */
export function filterAsyncRoutes(menus) {
  return filterChildrenRoutes(constantRoutes, menus)
}

export function filterChildrenRoutes(routes, menus) {
  const roleRoutes = []
  if (menus) {
    routes.forEach(route => {
      const temp = { ...route }
      if (temp.code && menus.findIndex(menu => menu.code === temp.code) > -1) {
      // 存在子菜单
        if (temp.children && temp.children.length > 0) {
          temp.children = filterChildrenRoutes(temp.children, menus)
        }
        roleRoutes.push(temp)
      }
    })
  }
  return roleRoutes
}

const state = {
  menus: []
}

const mutations = {
  SET_ROUTES: (state, menus) => {
    state.menus = menus
  }
}

const actions = {
  generateRoutes({ commit }, menus) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(menus)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
