const state = {
  roles: [],
  perms: []
}

const mutations = {
  RESET_AUTH: (state) => {
    Object.assign(state, {
      roles: [],
      perms: []
    })
  },
  SET_AUTH: (state, { roles, perms }) => {
    state.roles = roles
    state.perms = perms
  }
}

const actions = {
  userAuth({ commit }, data) {
    return new Promise(resolve => {
      commit('SET_AUTH', data)
      resolve(data)
    })
  },
  clearAuth({ commit }) {
    return new Promise(resolve => {
      commit('RESET_AUTH')
      resolve
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
