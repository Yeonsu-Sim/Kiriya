<template>
    <v-container fluid class="auth-container fill-height d-flex align-center justify-center">
      <v-card elevation="2" class="pa-6" min-width="600" min-height="450">
        <v-row>
        
        <!-- 회원정보 -->
        <v-col cols="8">
        <v-card-title class="justify-center">회원 정보</v-card-title>
        <v-card-text>
          <v-list dense>
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title>이메일</v-list-item-title>
                <v-list-item-subtitle>{{ member.email }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title>이름</v-list-item-title>
                <v-list-item-subtitle>{{ member.name }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title>회원 등급</v-list-item-title>
                <v-list-item-subtitle>{{ member.role.roleName }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <!-- 필요 시 추가 정보 삽입 -->
          </v-list>
        </v-card-text>
        </v-col>
        <!-- 프로필 이미지 -->
        <v-col cols="4" class="d-flex flex-column align-center pr-15">
          <v-avatar size="200" class="profile-container">
            <img 
              :key="member.profileUrl"
              :src="member.profileUrl || defaultProfileImage" alt="프로필 사진" width="200" height="200"/>
          </v-avatar>

          <input ref="fileInput" type="file" accept="image/*" class="d-none"
            @change="uploadAvatar" />
          <v-btn color="primary" @click="triggerFileSelect" class="profile-btn">프로필 사진 변경</v-btn>
        </v-col>
        </v-row>
        <v-card-actions class="justify-center mt-10">
          <v-btn color="primary" @click="goEdit">정보 수정</v-btn>
          <v-btn text @click="goBack">뒤로가기</v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from 'axios'
  import { useUserStore } from '@/stores/user'
  import defaultProfileImage from '@/assets/default-profile.png'

  const userStore = useUserStore()
  const member = computed(() => userStore.profile || {})
  const router = useRouter()

  const fileInput = ref(null)

  function triggerFileSelect() {
    fileInput.value?.click()
  }

  async function uploadAvatar(event) {
    const file = event.target.files[0]
    if (!file) return
    const formData = new FormData()
    formData.append('avatar', file)

    try {
      const response = await axios.post(`http://localhost:8080/api/members/${member.value.mno}/avatar`,
        formData
      )
    const updatedMember = response.data.data
    if (!updatedMember) {
      console.warn('▶ response.data.data가 없습니다!')
      return
    }
    // 스토어 갱신
    userStore.profile = updatedMember

    await fetchMyProfile()
    } catch(e) {
      console.error('프로필 업로드 실패', e)
    }
  }

async function fetchMyProfile() {
   try {
     const res = await axios.get(
       `http://localhost:8080/api/members/${userStore.profile.mno}`
     )
     // handleSuccess 구조라면 res.data.data, 아니면 res.data
     userStore.profile = res.data.data
   } catch (e) {
     console.error('프로필 재조회 실패', e)
   }
 }

onMounted(async() => {
  await fetchMyProfile()
})

  
  function goEdit() {
    router.push('/member/:mno/edit')
  }
  
  function goBack() {
    router.back()
  }
  </script>
  
  <style scoped>
  .auth-container {
    background: url('@/assets/auth-bg.png') center/cover no-repeat;
  }

  .d-none {
    display: none;
  }
  .profile-container {
    box-sizing: border-box;
    box-shadow: 0 0 0 4px rgba(142, 36, 170, 0.5);
    overflow: hidden;
  }
  .profile-btn {
    margin-top: 20px;
    width: 200px;
    text-transform: none;
  }

  </style>
  