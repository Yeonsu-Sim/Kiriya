<template>
  <v-container class="py-10" style="max-width:600px">
    <v-card>
      <v-card-title>여행 수정</v-card-title>
      <v-card-text>
        <v-form ref="formRef" @submit.prevent="onSubmit">
          <!-- 제목 -->
          <v-text-field v-model="form.title" label="여행 제목" required />
          <!-- 설명 -->
          <v-textarea v-model="form.description" label="여행 설명" rows="2" />
          <!-- 날짜 -->
          <v-row>
            <v-col cols="6">
              <v-text-field v-model="form.startDate" label="시작일" type="date" required />
            </v-col>
            <v-col cols="6">
              <v-text-field v-model="form.endDate" label="종료일" type="date" required />
            </v-col>
          </v-row>
          <!-- 썸네일 이미지 -->
          <div class="mb-4">
            <label>썸네일 이미지</label>
            <v-img :src="previewImage" height="120" class="mb-2" v-if="previewImage"/>
            <input type="file" accept="image/*" @change="onImageChange" />
          </div>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="onSubmit">수정 완료</v-btn>
        <v-btn @click="goBack">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const tno = route.params.tno

const form = ref({
  title: '',
  description: '',
  startDate: '',
  endDate: '',
  thumbnailUrl: '',      // 기존 url 저장
  thumbnailFile: null    // 새로 업로드할 파일
})
const previewImage = ref('')

const formRef = ref(null)
const loading = ref(false)

// 여행 정보 불러오기
const fetchTrip = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/trips/${tno}`)
    const trip = res.data.data.trip
    form.value = {
      title: trip.title,
      description: trip.description,
      startDate: trip.startDate?.split(' ')[0],
      endDate: trip.endDate?.split(' ')[0],
      thumbnailUrl: trip.thumbnailUrl || '',
      thumbnailFile: null
    }
    previewImage.value = trip.thumbnailUrl || ''
  } catch (e) {
    alert('여행 정보를 불러올 수 없습니다!')
  }
}

onMounted(fetchTrip)

// 썸네일 이미지 변경 핸들러
const onImageChange = (e) => {
  const file = e.target.files[0]
  if (!file) return
  form.value.thumbnailFile = file
  previewImage.value = URL.createObjectURL(file)
}

// 여행 수정 제출
const onSubmit = async () => {
     console.log('onSubmit 호출됨!');
  if (!form.value.title || !form.value.startDate || !form.value.endDate) {
    alert('필수 항목을 입력하세요.')
    return
  }
  loading.value = true
  try {
    let thumbnailUrl = form.value.thumbnailUrl
    // 새 파일 있으면 업로드
    if (form.value.thumbnailFile) {
      const imgForm = new FormData()
      imgForm.append('image', form.value.thumbnailFile)
      const res = await axios.post('http://localhost:8080/s3/upload', imgForm)
      thumbnailUrl = res.data.data // 실제 응답에 따라 경로 조정!
      console.log(thumbnailUrl.value)
    }
    console.log('이미지 업로드 완료! ');
    // 여행 정보 수정 API
    await axios.put(`http://localhost:8080/api/trips/${tno}`, {
      tno,
      title: form.value.title,
      description: form.value.description,
      startDate: form.value.startDate,
      endDate: form.value.endDate,
      thumbnailUrl
    })
    alert('수정 완료!')
    router.push(`/trips/${tno}`)
  } catch (e) {
    alert('수정 실패!')
  } finally {
    loading.value = false
  }
}

const goBack = () => router.back()
</script>
