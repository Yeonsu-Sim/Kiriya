// src/stores/user.js
import { defineStore } from 'pinia'
import axios from 'axios'
import FindPassword from '@/pages/Member/FindPassword.vue'

export const useUserStore = defineStore('user', {
  state: () => {
    const token= localStorage.getItem('token') || null
    let profile = null
    try {
      const raw = localStorage.getItem('profile')
      if (raw && raw !== 'undefined') {
        profile = JSON.parse(raw)
      }
    } catch {
      profile = null
    }

    return { token, profile, }
  },
  getters: {
    isLoggedIn: state => !!state.profile,
    userName: state => state.profile?.name || '',
    loginMno: state => state.profile?.mno || 0,
  },
  actions: {
    setProfile(data) {
      this.profile = data
    },
    // 로그인
    async login({ email, password, rememberMe }) {
      const res = await axios.post('http://localhost:8080/api/members/login', 
        {}, { 
          params: {email, password},
          withCredentials: true
        })
      
      const { status, accessToken, refreshToken, member } = res.data
      if (status !== 'SUCCESS') {
        throw new Error('로그인 실패')
      }
      this.token   = accessToken
      this.profile = member

      // 브라우저 스토리지에 보관
      localStorage.setItem('token', accessToken)
      localStorage.setItem('refreshToken', refreshToken)
      localStorage.setItem('profile', JSON.stringify(member))

      // 이후 모든 요청에 자동으로 포함
      axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`

      // 아이디 기억하기
      if (rememberMe) {
        localStorage.setItem('rememberEmail', email)
        localStorage.setItem('rememberMe', 'true')
      } else {
        localStorage.removeItem('rememberEmail')
        localStorage.removeItem('rememberMe')
      }
    },

    // 회원가입
    async signUp(payload) {
      // payload = { email, password, ... }
      await axios.post('http://localhost:8080/api/members/signup', {
        name: payload.name,
        email: payload.email,
        password: payload.password,
        role: { no: payload.role }
      },{
        params: {
          passwordCheck: payload.passwordConfirm,
          withCredentials: true
        }
      })
      // 가입 후 자동 로그인 원하면 여기서 login() 호출해도 되고,
      // 보통은 로그인 화면으로 리디렉션만 처리합니다.
    },

    // 프로필 조회 (페이지 새로고침 시 유저정보 복원용)
    async fetchProfile() {
      if (!this.token) return
      const res = await axios.get('/member/info')
      this.profile = res.data
      localStorage.setItem('profile', JSON.stringify(this.profile))
    },

    // 프로필 수정
    async updateProfile({ name, email, password, role }) {
      const mno = this.profile.mno
      const body = { name, email, role: {no: role} }
      if(password) {
        body.password = password
      }
      await axios.put(`http://localhost:8080/api/members/${mno}`, 
        body,
        { withCredentials: true }
      )
      this.profile = { 
        ...this.profile, 
        name, 
        email, 
        ...(password && { password }), 
        role: { ...this.profile.role, no: role }
      }
      localStorage.setItem('profile', JSON.stringify(this.profile))
    },

    // 로그아웃
    logout() {
      this.refreshToken = null
      this.accessToken   = null
      this.profile = null
      localStorage.removeItem('token')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('profile')
    },

    async findPassword({email, name}){
      await axios.post('http://localhost:8080/api/members/password/find', {}, { 
          params: {email, name},
          withCredentials: true
    })
    }

  }
})
