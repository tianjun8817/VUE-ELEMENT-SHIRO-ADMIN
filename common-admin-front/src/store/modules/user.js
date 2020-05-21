import { login, logout, getInfo } from '@/api/sys/user'
import { setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const state = {
  name: '',
  token: '',
  count: 0
}

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, {
      name: '',
      token: ''
    })
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_COUNT: (state) => {
    state.count++
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(data => {
        const { userId } = data
        commit('SET_TOKEN', userId)
        setToken(userId)
        commit('SET_COUNT')
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(data => {
        const { username } = data.sysUser
        commit('SET_NAME', username)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  // user logout
  logout({ commit, dispatch }, req = true) {
    return new Promise((resolve, reject) => {
      if (req) {
        logout().then(() => {
        // must remove  token  first
          removeToken()
          resetRouter()
          commit('RESET_STATE')
          dispatch('tagsView/delAllViews', null, { root: true })
          dispatch('auth/clearAuth', null, { root: true })
          resolve()
        }).catch(error => {
          removeToken()
          resetRouter()
          commit('RESET_STATE')
          dispatch('tagsView/delAllViews', null, { root: true })
          dispatch('auth/clearAuth', null, { root: true })
          reject(error)
        })
      // eslint-disable-next-line brace-style
      }
      // 不需要请求后台
      else {
        removeToken()
        resetRouter()
        commit('RESET_STATE')
        dispatch('tagsView/delAllViews', null, { root: true })
        dispatch('auth/clearAuth')
        resolve()
      }
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

