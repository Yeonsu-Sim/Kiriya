<template>
  <v-row justify="center" align="center" class="pa-4">
    <v-card color="surface" class="w-100 pa-4" style="max-width: 1000px">
      <h2 class="text-primary text-center ma-4">질문 작성</h2>

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
              등록
            </v-btn>
            <v-btn variant="tonal" color="secondary" @click.prevent.stop="showList" :disabled="loading">
              목록으로
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </v-row>

  <!-- 스낵 바 -->
  <v-snackbar v-model="success" :timeout="3000" color="success" location="top">
    {{ successMessage }}
  </v-snackbar>
  <v-snackbar v-model="error" :timeout="3000" color="error" location="top">
    {{ errorMessage }}
  </v-snackbar>
  <v-snackbar v-model="loading" :timeout="3000" color="info" location="top">
    {{ loadingMessage }}
  </v-snackbar>
</template>

<script setup>
  import { ref, reactive } from 'vue'
  import { useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'
  import axios from 'axios'

  const router = useRouter()

  const userStore = useUserStore()

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

  const submit = async () => {
    const { valid } = await form.value.validate()
    console.log('폼 유효성 검사 결과:', valid)
    if (valid) {
      loading.value = true
      loadingMessage.value = '등록 중입니다...'

      const data = {
        title: title.value,
        content: content.value,
        mno: userStore.profile.mno
      }
      console.log('등록할 데이터:', data)
      try {
        const response = await axios.post('http://localhost:8080/api/qnas/questions', data)
        console.log('등록 성공:', response.data)

        successMessage.value = '질문이 등록되었습니다.'
        success.value = true

        // 폼 초기화
        title.value = ''
        content.value = ''
        await form.value.resetValidation()
      } catch (e) {
        console.error('등록 실패:', e)
        errorMessage.value = '질문 등록에 실패했습니다.'
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