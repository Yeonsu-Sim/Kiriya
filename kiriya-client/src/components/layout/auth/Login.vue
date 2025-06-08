<template>
  <v-container class="auth-container d-flex align-center justify-center">
    <v-card elevation="2" class="pa-6" min-width="450" max-height="500">
      <v-card-title class="justify-center">로그인</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="login">
          <v-text-field
            v-model="email"
            label="이메일"
            prepend-icon="mdi-email"
            required
          />
          <v-text-field
            v-model="password"
            label="비밀번호"
            prepend-icon="mdi-lock"
            type="password"
            required
          />
          <v-checkbox
            v-model="rememberMe"
            label="아이디 기억하기"
          />
          <v-btn
            type="submit"
            color="primary"
            block
            :loading="loading"
            class="mt-4"
          >
            로그인
          </v-btn>
        </v-form>
      </v-card-text>
      <v-card-actions class="justify-center">
        <RouterLink class="router-link" to="/signup">회원가입</RouterLink>
        <span class="mx-2">|</span>
        <RouterLink class="router-link" to="/member/find-password">비밀번호 찾기</RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useUserStore } from '@/stores/user'

const email = ref('')
const password = ref('')
const rememberMe = ref(false)

const router = useRouter()
const userStore = useUserStore()

const login = async () => {
  try {
    await axios.post('/api/members/login', null, {
      params: {
        email: email.value,
        password: password.value,
        rememberMe: rememberMe.value ? 'on' : null,
      },
      withCredentials: true
    })
    router.push('/')
  } catch (err) {
      alert(err.response?.data?.message || '로그인 실패')
  }
}
</script>

<style scoped>
.auth-container {
  background: url('@/assets/auth-bg.png') center/cover no-repeat;
}

.router-link:hover {
  color: #BDBDBD;
}
</style>
