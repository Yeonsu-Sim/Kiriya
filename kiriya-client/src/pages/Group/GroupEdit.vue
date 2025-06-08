<template>
  <v-container class="pa-4">
    <v-card class="pa-10" max-width="1000" style="margin: auto;">
      <h2 class="text-center text-primary">그룹 수정</h2>
      <v-card-text>
        <v-form ref="form" @submit.prevent="onSubmit">
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

          <!-- 멤버 편집 섹션 -->
          <div class="mt-6">
            <div class="d-flex align-center mb-2">
              <v-text-field
                v-model="newEmail"
                label="새 멤버 이메일"
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
                  v-if="email !== ownerEmail"
                  small
                  class="ms-1"
                  @click="removeMember(idx)"
                >mdi-close</v-icon>
              </template>
              </v-chip>
              <div v-if="!membersEmails.length" class="grey--text">
                등록된 멤버가 없습니다.
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
              저장
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import qs from 'qs'

const route  = useRoute()
const router = useRouter()
const gno     = route.params.gno

// 폼 상태
const form = ref({
  name: '',
  description: ''
})
const newEmail       = ref('')
const membersEmails  = ref([])  // String[]
const ownerEmail = ref('')

// 로딩 상태
const loading = ref(false)

// 1) 기존 그룹 정보 + 멤버 리스트 불러오기
const fetchGroup = async () => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/groups/${gno}`)
    const { group, members } = data.data
    // 기본 정보 세팅
    form.value.name       = group.name
    form.value.description = group.description
    // 방장 이메일만 뽑아서 저장
    ownerEmail.value = members.find(m => m.mno === group.mno)?.memberEmail || ''
    // 기존 멤버 이메일만 추출
    membersEmails.value    = members.map(m => m.memberEmail).filter(email => email !== ownerEmail.value)
  } catch (err) {
    console.error('그룹 로드 실패', err)
  }
}

// 2) 멤버 추가
const addMember = () => {
  const email = newEmail.value.trim()
  if (email && !membersEmails.value.includes(email)) {
    membersEmails.value.push(email)
  }
  newEmail.value = ''
}

// 3) 멤버 삭제
const removeMember = (idx) => {
  membersEmails.value.splice(idx, 1)
}

// 4) 폼 제출 (PUT /api/groups/{gno}?emails=...)
const onSubmit = async () => {
  loading.value = true
  try {
    const payload = {
      name: form.value.name,
      description: form.value.description
    }
    await axios.put(
      `http://localhost:8080/api/groups/${gno}`,
      payload,
      { params: { emails: membersEmails.value },
        paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
     }
    )
    // 수정 완료 후 상세 페이지로 돌아가기
    router.push(`/groups/${gno}`)
  } catch (err) {
    console.error('그룹 수정 실패', err)
  } finally {
    loading.value = false
  }
}

onMounted(fetchGroup)
</script>
