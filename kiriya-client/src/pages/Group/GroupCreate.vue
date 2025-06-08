<template>
    <v-container class="ma-5 align-self-center" style="margin: auto;">
      <v-card class="pa-10" style="max-width: 1000px; min-height: 600px; margin: auto;">
      <h2 class="text-center text-primary">그룹 만들기</h2>
        <v-card-text>
          <v-form @submit.prevent="onSubmit">
            <!-- 그룹 기본 정보 -->
            <v-text-field
              v-model="form.name"
              label="그룹 이름"
              required
            />
            <v-textarea
              v-model="form.description"
              label="그룹 설명"
              rows="3"
            />
  
            <!-- 멤버 추가 섹션 -->
            <div class="mt-6">
              <div class="d-flex align-center mb-2">
                <v-text-field
                  v-model="newEmail"
                  label="회원 이메일 추가"
                  dense
                  hide-details
                  class="flex-grow-1"
                />
                <v-btn
                  icon size="small" color="primary" class="ma-2"
                  @click="addMember"
                  :disabled="!newEmail || membersEmails.includes(newEmail)"
                >
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </div>
              <div>
                <v-chip
                  v-for="(email, idx) in membersEmails"
                  :key="email"
                  class="ma-1"
                >
                  {{ email }}
                  <template #append>
                    <v-icon
                      small
                      class="ms-1"
                      @click="removeMember(idx)"
                    >mdi-close</v-icon>
                  </template>
                </v-chip>
                <div v-if="!membersEmails.length" class="grey--text">
                  아직 초대된 멤버가 없습니다.
                </div>
              </div>
            </div>
  
            <!-- 저장·취소 버튼 -->
            <v-card-actions class="mt-6">
              <v-btn
                color="primary"
                variant="tonal"
                :loading="loading"
                type="submit"
              >
                생성
              </v-btn>
              <v-btn text variant="tonal" @click="$router.back()">
                취소
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-card-text>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from 'axios'
  import qs from 'qs'
  
  const router = useRouter()
  
  // 폼 상태
  const form = ref({
    name: '',
    description: ''
  })
  const newEmail = ref('')
  const membersEmails = ref([]) // String[]
  
  // 로딩 상태
  const loading = ref(false)
  
  // 멤버 추가
  const addMember = () => {
    const email = newEmail.value.trim()
    if (email && !membersEmails.value.includes(email)) {
      membersEmails.value.push(email)
    }
    newEmail.value = ''
  }
  
  // 멤버 삭제
  const removeMember = (idx) => {
    membersEmails.value.splice(idx, 1)
  }
  
  // 그룹 생성
  const onSubmit = async () => {
    loading.value = true
    try {
      const payload = {
        name: form.value.name,
        description: form.value.description
      }
      await axios.post(
        `/api/groups`,
        payload,
        {
          params: { emails: membersEmails.value },
          paramsSerializer: params =>
            qs.stringify(params, { arrayFormat: 'repeat' })
        }
      )
      // 생성 후 상세 페이지로 이동
      // 백엔드가 생성된 그룹 반환시 gno 추출 가능하다면 사용
      router.push({ name: 'GroupList' })
    } catch (err) {
      console.error('그룹 생성 실패:', err.response?.data || err.message)
    } finally {
      loading.value = false
    }
  }
  </script>
  
  <style scoped>
  /* 필요에 따라 커스텀 스타일 추가 */
  </style>
  