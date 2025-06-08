<template>
    <v-container class="py-10" style="max-width: 600px;">
      <v-card class="pa-8" elevation="1">
        <v-card-title class="text-xl font-bold mb-2">게시글 작성</v-card-title>
        <v-form @submit.prevent="onSubmit">
          <!-- 제목 -->
          <v-text-field v-model="form.title" label="제목" required />
          <!-- 내용 -->
          <v-textarea v-model="form.content" label="내용" rows="5" required />
  
          <!-- 이미지 업로드 -->
          <div class="mb-4">
            <label class="font-semibold">이미지 첨부</label>
            <input
              type="file"
              multiple
              accept="image/*"
              @change="onFileChange"
              style="display: block; margin-top: 8px;"
            />
            <div class="flex flex-wrap mt-3">
              <div v-for="(img, idx) in imagePreviews" :key="idx" style="margin-right: 12px;">
                <img :src="img" alt="미리보기" width="90" style="border-radius: 12px; border: 1px solid #eee;" />
              </div>
            </div>
          </div>
  
          <!-- 태그 입력 (콤마, 띄어쓰기 구분 가능) -->
          <v-combobox
            v-model="form.tags"
            label="태그 (여러 개, Enter로 추가)"
            multiple
            chips
            clearable
            hint="#태그를 직접 입력하세요"
            persistent-hint
          />
  
          <!-- 등록 버튼 -->
          <v-btn type="submit" color="primary" class="mt-7" block>등록</v-btn>
        </v-form>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from 'axios'
  
  const router = useRouter()
  const form = ref({
    title: '',
    content: '',
    imageFiles: [],
    tags: []
  })
  
  const imageFiles = ref([])
const imageUrls = ref([])

// 이미지 여러 장 선택
function onFileChange(e) {
  const files = Array.from(e.target.files)
  imageFiles.value = files
  form.value.imageFiles = files
}

// 이미지 여러장 S3에 업로드 (프론트→Spring API)
async function uploadImagesToS3() {
  imageUrls.value = []
  for (const file of imageFiles.value) {
    const formData = new FormData()
    formData.append('image', file)
    try {
        // Spring의 /api/upload는 1장 업로드 & S3 URL 리턴!
        const { data } = await axios.post('http://localhost:8080/s3/upload', formData)
        console.log('S3 응답:', data);
        imageUrls.value.push(data.data) // S3 이미지 URL
        console.log('업로드된 S3 URL:', data.data)
    } catch(e) {
        console.error('이미지 업로드 실패: ', e)
    }
  }
}

// 게시글 등록
async function onSubmit() {
  await uploadImagesToS3()
  
  
    // Board 등록
    const boardDto = {
      title: form.value.title,
      content: form.value.content,
      imageUrls: imageUrls.value,
      tags: form.value.tags
    }

    console.log('boardDto', JSON.stringify(boardDto))

    console.log('imageUrls', imageUrls.value)

  
    await axios.post('http://localhost:8080/api/boards', boardDto)
    alert('게시글이 등록되었습니다!')
    // 성공시 페이지 이동 등 추가
    router.push('/feed')
  }
  </script>
  