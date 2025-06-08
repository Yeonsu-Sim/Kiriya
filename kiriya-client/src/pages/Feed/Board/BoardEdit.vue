<template>
    <v-container>
      <v-card class="pa-8" elevation="1">
        <v-card-title>게시글 수정</v-card-title>
        <v-form @submit.prevent="onSubmit">
          <!-- 제목/내용 -->
          <v-text-field v-model="form.title" label="제목" />
          <v-textarea v-model="form.content" label="내용" rows="5" />
  
          <!-- 기존 이미지(썸네일/삭제) -->
          <div v-if="form.imageUrls.length">
            <div v-for="(img, idx) in form.imageUrls" :key="img" class="mr-2 inline-block">
              <img :src="img" alt="기존 이미지" width="90" style="border-radius:12px;" />
              <v-btn size="x-small" @click="removeExistingImage(idx)" color="red" class="ml-1">삭제</v-btn>
            </div>
          </div>
          <!-- 새 이미지 업로드 -->
          <input type="file" multiple @change="handleNewImages" accept="image/*" />
  
          <!-- 태그 -->
          <v-combobox
            v-model="form.tags"
            label="태그 (여러 개)"
            multiple chips clearable
          />
  
          <v-btn type="submit" color="primary" class="mt-7" block>수정</v-btn>
          <v-btn class="mt-2" block @click="router.back()">취소</v-btn>
        </v-form>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import axios from 'axios'
  
  const route = useRoute()
  const router = useRouter()
  const bno = route.params.bno
  
  const form = ref({
    title: '',
    content: '',
    imageUrls: [], // 기존 이미지 S3 url
    tags: [],
    newImages: []  // 새로 추가한 File[]
  })
  
  // 기존 글 불러오기
  onMounted(async () => {
    const { data } = await axios.get(`http://localhost:8080/api/boards/${bno}`)
    const board = data.data.board
    form.value.title = board.title
    form.value.content = board.content
    form.value.imageUrls = board.imageUrls || []
    form.value.tags = board.tags || []
  })
  
  // 기존 이미지 삭제
  function removeExistingImage(idx) {
    form.value.imageUrls.splice(idx, 1)
  }
  
  // 새 이미지 업로드 (프리뷰 생략)
  function handleNewImages(e) {
    form.value.newImages = Array.from(e.target.files)
  }
  
  // 수정 요청
  async function onSubmit() {
    // 새로 추가한 이미지만 S3 업로드
    const newUrls = []
    for (const file of form.value.newImages) {
      const formData = new FormData()
      formData.append('image', file)
      const { data } = await axios.post('http://localhost:8080/s3/upload', formData)
      newUrls.push(data.data)
    }
    // 최종 이미지 URL 리스트 = 기존 + 새로 추가
    const allImageUrls = [...form.value.imageUrls, ...newUrls]
    const boardDto = {
      bno: Number(bno),
      title: form.value.title,
      content: form.value.content,
      imageUrls: allImageUrls,
      tags: form.value.tags
    }
    // PUT /api/boards/:bno
    await axios.put(`http://localhost:8080/api/boards/${bno}`, boardDto)
    alert('수정 완료!')
    router.push('/feed') // 혹은 상세로
  }
  </script>
  