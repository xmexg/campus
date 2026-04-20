import { reactive, computed } from 'vue'
import { userApi } from '../api'

const state = reactive({
  user: null,
  token: localStorage.getItem('token') || '',
})

export function useSession() {
  const isLogin = computed(() => !!state.token)
  const role = computed(() => state.user?.role || '')
  const identityOk = computed(() => state.user?.identityAuditStatus === 'APPROVED')

  async function refreshUser() {
    if (!state.token) {
      state.user = null
      return
    }
    try {
      state.user = await userApi.me()
    } catch {
      state.user = null
    }
  }

  function setAuth(token, user) {
    state.token = token
    state.user = user
    if (token) localStorage.setItem('token', token)
    else localStorage.removeItem('token')
  }

  function logout() {
    setAuth('', null)
  }

  return { state, isLogin, role, identityOk, refreshUser, setAuth, logout }
}
