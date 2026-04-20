import { reactive, computed } from 'vue'
import { userApi } from '../api'
import { TOKEN_KEY } from '../api/http'

const state = reactive({
  user: null,
  token: localStorage.getItem(TOKEN_KEY) || '',
})

export function useSession() {
  const isLogin = computed(() => !!state.token)
  const role = computed(() => state.user?.role || '')

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
    if (token) localStorage.setItem(TOKEN_KEY, token)
    else localStorage.removeItem(TOKEN_KEY)
  }

  function logout() {
    setAuth('', null)
  }

  return { state, isLogin, role, refreshUser, setAuth, logout }
}
