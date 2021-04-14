import axios from 'axios'

let base = 'http://localhost:8081'
export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      // Do whatever you want to transform the data
      return JSON.stringify(data)
    }],
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export const getRequest = (url, params) => {
  return axios({
    method: 'get',
    params: params,
    headers: {
      'Content-Type': 'application/json'
    },
    url: `${base}${url}`
  })
}
