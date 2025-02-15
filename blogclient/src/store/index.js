import Vuex from 'vuex'
import Vue from 'vue'
import axios from 'axios'

Vue.use(Vuex)

export const GET_USER = 'getUser'
export const SET_USER = 'setUser'
export const GET_TOKEN = 'getToken'
export const GET_USER_NAME = 'getUserName'

export default new Vuex.Store({
  state: {
    user: {}
  },
  mutations: {
    setUser (state, payload) {
      state.user = payload
      axios.defaults.headers.common['token'] = state.user.token
    },
  },
  getters: {
    getUser: state => {
      return state.user
    },
    getToken: state => {
      return state.user.token
    },
    getUserName: state => {
      return state.user.username
    },
  }
})
