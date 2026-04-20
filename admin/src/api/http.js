import axios from 'axios'
import { ElMessage } from 'element-plus'

/** 与门户 `token` 分离，避免在 5173 以学生登录后访问 5174 被误判为已登录并跳回门户 */
export const TOKEN_KEY = 'legal_platform_admin_token'

const http = axios.create({
  baseURL: '/api',
  timeout: 60000,
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body && typeof body.code === 'number') {
      if (body.code !== 0) {
        ElMessage.error(body.message || '请求失败')
        return Promise.reject(new Error(body.message || '请求失败'))
      }
      return body.data
    }
    return body
  },
  (err) => {
    const msg = err.response?.data?.message || err.message || '网络错误'
    ElMessage.error(msg)
    return Promise.reject(err)
  }
)

export default http
