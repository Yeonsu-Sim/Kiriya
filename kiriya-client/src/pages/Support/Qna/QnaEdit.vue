<template>
  <v-row justify="center" align="center" class="pa-4">
    <v-card color="surface" class="w-100 pa-4" style="max-width: 1000px">
      <h2 class="text-primary text-center ma-4">질문 수정</h2>

      <v-card-text>
        <v-form ref="form" lazy-validation>
            <v-text-field
            v-model="title"
            :rules="[rules.required]"
            label="제목"
            outlined
            clearable
            validate-on-blur="false"
            validate-on="submit"
            ></v-text-field>

          <v-textarea
            v-model="content"
            :rules="[rules.required]"
            label="내용"
            outlined
            clearable
            validate-on-blur="false"
            validate-on="submit"
          ></v-textarea>
          <div class="d-flex ga-4">
            <v-btn color="primary" @click="submit" :disabled="loading">
              완료
            </v-btn>
            <v-btn variant="tonal" color="secondary" @click="cancel" :disabled="loading">
              취소
            </v-btn>
            <v-btn variant="outlined" color="secondary" @click.prevent.stop="showList" :disabled="loading">
              목록으로
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </v-row>

  <!-- 스낵 바 -->
  <v-snackbar v-model="error" :timeout="3000" color="error" location="top">
    {{ errorMessage }}
  </v-snackbar>
</template>

<script setup>
  import { ref, reactive } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import axios from 'axios'

  const router = useRouter()
  const route = useRoute()

  const title = ref('')
  const content = ref('')

  const success = ref(false)
  const successMessage = ref('')
  const error = ref(false)
  const errorMessage = ref('')
  const loading = ref(false)
  const loadingMessage = ref('')
  
  const rules = {
    required: value => !!value || '필수 입력 항목입니다.',
  }

  const form = ref(null)

  onMounted(async () => {
    const id = route.params.id
    try {
      const res = await axios.get(`http://localhost:8080/api/qnas/questions/${id}`)
      title.value = res.data.data.title
      content.value = res.data.data.content
    } catch (e) {
      console.error('질문 불러오기 실패:', e)
    }
  })

  const cancel = () => {
    title.value = ''
    content.value = ''
    router.go(-1)
  }

  const submit = async () => {
    const { valid } = await form.value.validate()
    console.log('폼 유효성 검사 결과:', valid)
    if (valid) {
      loading.value = true
      loadingMessage.value = '수정 중입니다...'

      const data = {
        title: title.value,
        content: content.value,
        mno: 1, // 임시로 작성자 ID를 1로 설정
      }
      console.log('수정할 데이터:', data)
      try {
        const id = route.params.id
        const response = await axios.put(`http://localhost:8080/api/qnas/questions/${id}`, data)
        console.log('수정 성공:', response.data)

        successMessage.value = '질문이 수정되었습니다.'
        success.value = true

        // 폼 초기화
        await form.value.resetValidation()
        router.push({ name: 'QnaDetail', params: { id } })
      } catch (e) {
        console.error('수정 실패:', e)
        errorMessage.value = '질문 수정에 실패했습니다.'
        error.value = true
      } finally {
        loading.value = false
      }

    } else {
      error.value = true
      errorMessage.value = '모든 필수 항목을 입력해주세요.'
    }
  }

  const showList = () => {
    router.push({ name: 'Support', query: { tab: 'QnA' } })
  }

</script>

<style scoped lang="scss">
</style>