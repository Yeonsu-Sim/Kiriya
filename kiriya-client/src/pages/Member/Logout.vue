<template>
    <v-container
      fluid
      class="logout-container fill-height d-flex align-center justify-center"
    >
      <v-card elevation="2" class="pa-6" max-width="400">
        <v-card-text class="text-center">
          <v-progress-circular indeterminate color="primary" size="48" />
          <div class="mt-4">로그아웃 중입니다...</div>
        </v-card-text>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'
  import axios from 'axios'

  const router = useRouter()
  const userStore = useUserStore()
  
  onMounted(async () => {
    try {
        await axios.post('http://localhost:8080/api/members/logout')
    } catch (err) {
        console.error('서버 로그아웃 실패', err)
    }
    // 토큰 및 프로필 제거
    await userStore.logout()
    // 로그아웃 처리 후 로그인 페이지로 이동
    router.replace({ path: '/login' })
    })
  </script>
  
  <style scoped>
  .logout-container {
    background: var(--v-theme-background);
  }
  </style>
  