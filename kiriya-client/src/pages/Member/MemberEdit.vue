<template>
    <v-container
      fluid
      class="auth-container fill-height d-flex align-center justify-center"
    >
      <v-card elevation="2" class="pa-6 h-80" min-width="500">
        <v-card-title class="justify-center">회원 정보 수정</v-card-title>
        <v-card-text>
          <v-form ref="formRef" @submit.prevent="onSubmit">
            <v-text-field
              v-model="formData.email"
              label="이메일"
              required
              class="mt-4"
            />
            <v-text-field
              v-model="formData.name"
              label="이름"
              required
              class="mt-4"
            />
            <v-text-field
              v-model="formData.password"
              label="새 비밀번호"
              type="password"
              hint="변경하지 않으려면 비워두세요"
              class="mt-4"
            />
            <v-text-field
              v-model="formData.passwordConfirm"
              label="새 비밀번호 확인"
              type="password"
              class="mt-4"
            />
            <v-radio-group
              v-model.number="formData.role"
              required
              
            >
            <label for="formData.role">회원 등급</label>
              <v-radio label="일반회원" :value="2"/>
              <v-radio label="관리자" :value="1" />
            </v-radio-group>

            <div class="d-flex align-center ga-6">
              <v-btn text 
                class="flex-grow-1"
                @click="goBack"
              >
                취소
              </v-btn>
              <v-btn
                type="submit"
                color="primary"
                :loading="loading"
                class="flex-grow-1"
              >
                저장
              </v-btn>
            </div>

          </v-form>
        </v-card-text>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'

  
  const router = useRouter()
  const userStore = useUserStore()
  const loading = ref(false)


  
  const formData = ref({
    email: '',
    name: '',
    password: '',
    passwordConfirm: '',
    role: 2,
  })
  
  // 초기 데이터 로드
  onMounted(async () => {
    if (!userStore.profile) {
      await userStore.fetchProfile()
    }
    formData.value.email = userStore.profile.email
    formData.value.name = userStore.profile.name
    formData.value.role = userStore.profile.role.no
  })
  
  async function onSubmit() {
    if (
      formData.value.password &&
      formData.value.password !== formData.value.passwordConfirm
    ) {
      return alert('비밀번호가 일치하지 않습니다.')
    }
    loading.value = true
    try {
      await userStore.updateProfile({
        email: formData.value.email,
        name: formData.value.name,
        password: formData.value.password,
        role: formData.value.role,
      })
      alert('회원 정보가 성공적으로 업데이트되었습니다.')
      router.push('/myPage')
    } catch (err) {
      console.error(err)
      alert('정보 저장에 실패했습니다.')
    } finally {
      loading.value = false
    }
  }
  
  function goBack() {
    router.back()
  }
  </script>
  
  <style scoped>
  .auth-container {
    background: url('@/assets/auth-bg.png') center/cover no-repeat;
  }
  </style>
  