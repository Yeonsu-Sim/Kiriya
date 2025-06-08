import axios from 'axios'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

axios.interceptors.request.use(config => {
  const store = useUserStore()
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`
  }
  return config
})

const router = useRouter()

// — 응답 시 401 처리 (추가)
axios.interceptors.response.use(
  response => response,
  error => {
    // 401 Unauthorized 응답을 받으면
    if (error.response?.status === 401) {
      const store = useUserStore()
      // 1) Pinia 스토어에서 프로필·토큰 모두 지우기
      store.logout()
      // 2) axios 기본 헤더에서도 Authorization 제거
      delete axios.defaults.headers.common['Authorization']
      // 3) 로그인 페이지로 이동
      router.push({ name: 'Login' })
    }
    return Promise.reject(error)
  }
)