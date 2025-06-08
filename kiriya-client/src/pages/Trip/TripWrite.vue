<template>
  <v-container class="py-8" style="max-width: 540px;">
    <v-card elevation="3" class="pa-8 rounded-2xl mx-auto">
      <v-card-title class="font-weight-bold mb-4 text-center">
        새 여행 등록
      </v-card-title>
      <v-form ref="formRef" @submit.prevent="onSubmit" v-model="valid">
        <v-text-field
          v-model="form.title"
          label="여행 제목"
          prepend-icon="mdi-airplane"
          :rules="[v => !!v || '필수 입력!']"
          required
          class="mb-4"
        />
        <v-textarea
          v-model="form.description"
          label="여행 설명"
          prepend-icon="mdi-note-text"
          rows="2"
          auto-grow
          class="mb-4"
        />
        <v-row class="mb-4">
          <v-col>
            <v-text-field
              v-model="form.startDate"
              label="시작 날짜"
              type="date"
              :rules="[v => !!v || '필수 입력!']"
              required
            />
          </v-col>
          <v-col>
            <v-text-field
              v-model="form.endDate"
              label="종료 날짜"
              type="date"
              :rules="[v => !!v || '필수 입력!']"
              required
            />
          </v-col>
        </v-row>
        <v-file-input
          v-model="form.thumbnailFile"
          label="썸네일 이미지"
          prepend-icon="mdi-image"
          accept="image/*"
          show-size
          class="mb-4"
        />
        <v-btn type="submit" color="primary" block :loading="loading" size="large">
          <v-icon left>mdi-plus</v-icon> 등록하기
        </v-btn>
      </v-form>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import defaultTripImage from '@/assets/default-trip.jpg'
import axios from 'axios'

const router = useRouter()
const form = ref({
    title: '',
    description: '',
    startDate: '',
    endDate: '',
    thumbnailFile: null, // 파일 객체
})
const valid = ref(true)
const loading = ref(false)
const formRef = ref(null)

const userStore = useUserStore()
const mno = userStore.profile.mno

const onSubmit = async () => {
  if (!formRef.value.validate()) return
  loading.value = true
  try {
    // 이미지 파일이 있으면 먼저 업로드 (예: 별도의 이미지 업로드 API 필요)
    let thumbnailUrl = ''
    if (form.value.thumbnailFile) {
      const imgForm = new FormData()
      imgForm.append('image', form.value.thumbnailFile)
      const res = await axios.post('http://localhost:8080/s3/upload', imgForm, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      thumbnailUrl = res.data.data // 서버에서 반환한 이미지 url
    } else {
      thumbnailUrl = defaultTripImage // 파일 없으면 기본 이미지 경로
    }

    // 여행 정보 등록
    await axios.post('http://localhost:8080/api/trips', {
      mno,
      title: form.value.title,
      description: form.value.description,
      startDate: form.value.startDate,
      endDate: form.value.endDate,
      thumbnailUrl, // 이미지가 있으면 url, 없으면 빈 값
    })
    alert('여행이 등록되었습니다!')
    router.push({name: 'TripList'})
  } catch (err) {
    alert('등록에 실패했습니다!')
    console.error(err)
  } finally {
    loading.value = false
  }
}
</script>
