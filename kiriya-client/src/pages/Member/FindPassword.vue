<template>
  <v-container
    fluid
    class="auth-container fill-height d-flex align-center justify-center"
  >
    <v-card elevation="2" class="pa-6 rounded-lg" min-width="450">
      <v-card-title class="justify-center">비밀번호 찾기</v-card-title>
      <v-card-text>
        <v-form ref="form" @submit.prevent="onFindPassword">
          <v-text-field
            v-model="formData.email"
            label="이메일"
            prepend-icon="mdi-email"
            required
          />
          <v-text-field
            v-model="formData.name"
            label="이름"
            prepend-icon="mdi-account"
            required
            class="mt-4"
          />
          <v-btn
            type="submit"
            color="primary"
            :loading="loading"
            class="mt-6 gradient-btn w-100"
            block
          >
            비밀번호 찾기
          </v-btn>
        </v-form>
      </v-card-text>
      <v-card-actions class="justify-center">
        <RouterLink class="router-link" to="/login">돌아가기</RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const form = ref(null)
const formData = ref({ email: '', name: '' })
const loading = ref(false)

async function onFindPassword() {
  if (!formData.value.email || !formData.value.name) {
    alert('이메일과 이름을 모두 입력해 주세요.')
    return
  }
  loading.value = true
  try {
    // 비밀번호 찾기 요청
    await userStore.findPassword({
      email: formData.value.email,
      name: formData.value.name
    })
    alert('입력하신 이메일로 비밀번호 재설정 링크를 전송했습니다.')
    router.replace({ path: '/login' })
  } catch (err) {
    console.error(err)
    alert('비밀번호 찾기에 실패했습니다. 정보를 다시 확인해 주세요.')
  } finally {
    loading.value = false
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
  