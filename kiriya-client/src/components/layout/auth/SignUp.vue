<template>
    <v-container fluid class="auth-container fill-height d-flex align-center justify-center">
      <v-card elevation="2" class="pa-3" min-width="450" max-height="700">
        <v-card-title class="justify-center">회원가입</v-card-title>
        <v-card-text>
          <v-form @submit.prevent="signup">
            <v-text-field
              v-model="name"
              label="이름"
              prepend-icon="mdi-account"
              required
            />
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
            <v-text-field
              v-model="passwordCheck"
              label="비밀번호 확인"
              prepend-icon="mdi-lock-check"
              type="password"
              required
            />
            <v-radio-group
              v-model="role"
              required
              
            >
            <label for="role">회원 등급</label>
              <v-radio label="일반회원" value="USER"/>
              <v-radio label="관리자" value="ADMIN" />
            </v-radio-group>
            <v-checkbox
              v-model="agree"
              label="개인정보정책 동의"
              class="pb-0"
              required
            />
            <v-btn
              type="submit"
              color="primary"
              block
              :loading="loading"
            >
              회원가입
            </v-btn>
          </v-form>
        </v-card-text>
        <v-card-actions class="justify-center">
          <RouterLink to="/login">로그인</RouterLink>
        </v-card-actions>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'
  import axios from 'axios'

  const name = ref('')
  const email = ref('')
  const password = ref('')
  const passwordCheck = ref('')
  const role = ref('USER')
  const agree = ref(false)

  const router = useRouter()
  const userStore = useUserStore()
  
  const signup = async () => {
    if (password.vaue !== passwordCheck.value) {
      return alert('비밀번호가 일치하지 않습니다.')
    }
    if (!agree.value) {
      return alert('개인정보정책에 동의해 주세요.')
    }
    try {
      await axios.post(
        'api/members/signup',
        { name: name.value, email: email.value, password: password.value, role: role.value },
        { params: {passwordCheck: passwordCheck.value }}
      )
      alert('회원가입이 완료되었습니다!')
      router.push('/login')
    } catch(err) {
      console.error(err)
      alert(err.reponse?.data?.message || '회원가입 실패')
    }
  }
  
  </script>
  
  <style scoped>
  .auth-container {
    background: url('@/assets/auth-bg.png') center/cover no-repeat;
  }
  </style>
  