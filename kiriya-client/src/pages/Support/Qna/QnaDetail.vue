<template>
  <v-row justify="center" align="center" class="pa-4">
    <v-card color="surface" class="w-100 pa-4" style="max-width: 1000px">
      <h2 class="text-primary text-center ma-4">질문 상세 보기</h2>

      <v-card-text>
        <v-row>
          <v-col cols="12">
            <v-sheet color="grey-lighten-4" rounded>
              <div class="d-flex justify-space-between align-center">
                <h3>{{ title }}</h3>
              </div>
            </v-sheet>
          </v-col>
          <v-col cols="12">
            <v-divider />
          </v-col>
          <v-col cols="12">
            <v-sheet color="grey-lighten-4" rounded>
              <div class="d-flex justify-space-between align-center flex-wrap">
                <div class="d-flex align-center justify-center" style="gap: 4px">
                <v-avatar size="30" style="border: 1px solid #f5f5f5">
                  <v-img
                    :src="authorProfileUrl || defaultProfileImage"
                    cover
                  />
                </v-avatar> 
                <span>작성자: {{ author }}</span>
              </div>
              <div class="d-flex justify-space-between align-center" style="gap: 12px">
                <span>작성일: {{ createdAt }}</span>
                <span>수정일: {{ updatedAt }}</span>
              </div>
              </div>
            </v-sheet>
          </v-col>
          <v-col cols="12">
            <v-sheet color="grey-lighten-5" rounded>
              <div style="white-space: pre-line;">{{ content }}</div>
            </v-sheet>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions class="justify-end">
        <v-btn 
          v-if="userStore.profile?.mno === author.mno"
          variant="tonal" color="primary" @click="edit">수정</v-btn>
        <v-btn variant="outlined" color="secondary" @click="showList">목록으로</v-btn>
      </v-card-actions>
    </v-card>
  </v-row>

  <!-- 답변 작성 폼 -->
  <v-row v-if="showanswerWriteForm" justify="center" align="center" class="pa-4 mt-0">
    <div class="d-flex flex-row justify-space-between align-center w-100" style="max-width: 1000px">
      <v-form class="w-100" @submit.prevent="submitAnswer" ref="answerWriteFormRef" lazy-validation>
        <div class="d-flex w-100" style="gap: 16px;">
          <v-textarea
            v-model="answerWriteForm.content"
            :rules="[rules.required]"
            label="답변 내용"
            rows="3"
            outlined
            clearable
            validate-on-blur="false"
            validate-on="submit"
            class="flex-grow-1"
            style="width: 100%;"
          ></v-textarea>
          <v-btn
            color="primary"
            type="submit"
            :disabled="loading"
            class="flex-shrink-0"
            style="height: 100px;"
          >
            답변 등록
          </v-btn>
        </div>
      </v-form>
    </div>
  </v-row>

  <!-- 답변 -->
  <div class="d-flex flex-column justify-center align-center w-100 pb-2 mt-2">
    <v-row v-if="answers.length > 0" class="w-100" style="max-width: 1000px;">
      <v-col v-for="(answer, index) in answers" :key="index" cols="12" class="pa-0 py-2">
        <v-sheet color="surface" elevation="1" rounded class="pa-4 d-flex flex-column">
          <v-form
            :ref="el => { if (el) answerEditFormRefs[answer.id] = el }"
            class="d-flex w-100 justify-end"
            lazy-validation
            @submit.prevent="editAnswer(answer.id)"
          >
            <div v-show="editAnswerMode != answer.id" class="w-100">
              <div class="overflow-wrap" style="word-break: break-word; white-space: pre-wrap;">
                {{ answer.content }}
              </div>
              <div class="answer-detail d-flex justify-start align-center flex-wrap">
                <v-avatar size="30" style="border: 1px solid #f5f5f5">
                  <v-img
                    :src="answer.profileUrl || defaultProfileImage"
                    cover
                  />
                </v-avatar> 
                <span>{{ answer.author }}</span>
                <span> | {{ answer.createdAt }}</span>
                <span>{{ answer.modified ? '(수정됨)' : '' }}</span>
              </div>
            </div>
            <v-textarea
              v-show="editAnswerMode === answer.id"
              v-model="answerEditForm.content"
              clearable
              label="답변 내용"
              outlined
              rows="2"
              :rules="[rules.required]"
              style="width: 100%;"
              validate-on="submit"
              validate-on-blur="false"
            />
            <div class="d-flex flex-column align-end ms-2">
              <v-btn
                v-if="userStore.profile?.mno === answer.mno"
                v-show="editAnswerMode != answer.id"
                color="primary"
                variant="tonal"
                @click="editAnswerModeOn(answer.id)"
              >
                수정
              </v-btn>
              <v-btn
                v-if="userStore.profile?.mno === answer.mno"
                v-show="editAnswerMode != answer.id"
                class="mt-2"
                color="error"
                variant="tonal"
                @click="deleteAnswer(answer.id)"
              >
                삭제
              </v-btn>
              <v-btn
                v-show="editAnswerMode === answer.id"
                color="success"
                :disabled="sameAnswerContent"
                type="submit"
                variant="tonal"
              >
                완료
              </v-btn>
              <v-btn
                v-show="editAnswerMode === answer.id"
                class="mt-2"
                color="secondary"
                variant="tonal"
                @click="editAnswerModeOff"
              >
                취소
              </v-btn>
            </div>
          </v-form>
        </v-sheet>
      </v-col>
    </v-row>
    <v-row v-else>
      <v-col cols="12">
        <div class="d-flex justify-center align-center">
          <h3 class="text-secondary mb-4 mb-0">답변이 없습니다.</h3>
        </div>
      </v-col>
    </v-row>
  </div>

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
  import { ref, getCurrentInstance, reactive, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'
  import axios from 'axios'
  import defaultProfileImage from '@/assets/default-profile.png'

  const router = useRouter()
  const route = useRoute()
  const id = route.params.id

  const userStore = useUserStore()

  const title = ref('')
  const content = ref('')
  const author = ref('')
  const authorProfileUrl = ref('')
  const createdAt = ref('')
  const updatedAt = ref('')
  const answers = ref([])
  
  onMounted(async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/qnas/questions/${id}`)
      const data = res.data.data
      title.value = data.title
      content.value = data.content
      createdAt.value = new Date(data.createdAt).toLocaleString('ko-KR', { timeZone: 'Asia/Seoul', hour12: true, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
      updatedAt.value = new Date(data.updatedAt).toLocaleString('ko-KR', { timeZone: 'Asia/Seoul', hour12: true, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
      author.value = data.author
      authorProfileUrl.value = data.authorProfileUrl
    } catch (e) {
      console.error('질문 불러오기 실패:', e)
    }

    await getAnswers()
    console.log('답변 목록:', answers.value)
  })
  
  const edit = () => {
    router.push({ name: 'QnaEdit'})
  }
  
  const showList = () => {
    router.push({ name: 'Support', query: { tab: 'QnA' } })
  }

  const success = ref(false)
  const successMessage = ref('')
  const error = ref(false)
  const errorMessage = ref('')
  const loading = ref(false)
  const loadingMessage = ref('')

  /** 답변 **/
  const showanswerWriteForm = ref(true)
  const answerWriteFormRef = ref(null)
  const answerWriteForm = reactive({
    content: '',
  })
  const rules = {
    required: value => !!value || '필수 입력 항목입니다.',
  }

  const getAnswers = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/qnas/questions/${id}/answers`)
      const data = res.data.data
      if (data) {
        answers.value = data.map(answer => ({
          id: answer.answerNo,
          mno: answer.mno,
          content: answer.content,
          createdAt: answer.createdAt ? new Date(answer.createdAt).toLocaleString('ko-KR', { timeZone: 'Asia/Seoul', hour12: true, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }) : '', 
          updatedAt: answer.updatedAt ? new Date(answer.updatedAt).toLocaleString('ko-KR', { timeZone: 'Asia/Seoul', hour12: true, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }) : '',
          modified: answer.updatedAt != answer.createdAt,
          author: answer.author,
          profileUrl: answer.authorProfileUrl
        }))
      }
    } catch (e) {
      console.error('답변 불러오기 실패:', e)
    }
  }

  const submitAnswer = async () => {
    const { valid } = await answerWriteFormRef.value.validate()
    console.log('폼 유효성 검사 결과:', valid)
    if (valid) {
      loading.value = true
      loadingMessage.value = '답변 등록 중입니다...'

      const data = {
        content: answerWriteForm.content,
        mno: userStore.profile.mno
      }
      try {
        const response = await axios.post(`http://localhost:8080/api/qnas/questions/${id}/answers`, data)
        console.log('답변 등록 성공:', response.data)

        successMessage.value = '답변이 등록되었습니다.'
        success.value = true

        // 폼 초기화
        answerWriteForm.content = ''
        await answerWriteFormRef.value.resetValidation()

        getAnswers() // 답변 목록 갱신
      } catch (e) {
        console.error('답변 등록 실패:', e)
        errorMessage.value = '답변 등록에 실패했습니다.'
        error.value = true
      } finally {
        loading.value = false
      }

    } else {
      error.value = true
      errorMessage.value = '모든 필수 항목을 입력해주세요.'
    }
  }

  const answerEditForm = reactive({
    content: '',
  })
  const answerEditFormRefs = reactive({})

  const editAnswerMode = ref(-1)

  const editAnswerModeOn = (answerId) => {
    editAnswerMode.value = answerId
    const answer = answers.value.find(answer => answer.id === answerId)
    if (answer) {
      answerEditForm.content = answer.content
    }
  }
  const editAnswerModeOff = () => {
    editAnswerMode.value = -1
    answerEditForm.content = ''
  }

  const sameAnswerContent = computed(() => {
    if (editAnswerMode.value !== -1) {
      const answer = answers.value.find(a => a.id === editAnswerMode.value)
      if (answer && answerEditForm.content === answer.content) {
        return true // 내용이 같음(수정 전과 동일)
      }
    }
    return false // 내용이 다름(수정 가능)
  })

  const editAnswerValidate = async (answerId) => {
    const formRef = answerEditFormRefs[answerId]
    if (!formRef || typeof formRef.validate !== 'function') {
      console.error('유효하지 않은 form ref:', answerId, formRef)
      return
    }
    const { valid } = await formRef.validate()
    console.log('폼 유효성 검사 결과:', valid)
    return valid
  }

  const editAnswer = async answerId => {
    if (await editAnswerValidate(answerId)) {
      loading.value = true
      loadingMessage.value = '답변 수정 중입니다...'
      const data = {
        content: answerEditForm.content,
      }
      try {
        const res = await axios.put(`http://localhost:8080/api/qnas/answers/${answerId}`, data)
        if (res.data.data.createdAt !== res.data.data.updatedAt) {
          console.log('답변 수정 성공:', res.data)
          successMessage.value = '답변이 수정되었습니다.'
          success.value = true
          getAnswers() // 답변 목록 갱신
        }
      } catch (e) {
        console.error('답변 수정 실패:', e)
        errorMessage.value = '답변 수정에 실패했습니다.'
        error.value = true
      } finally {
        loading.value = false
        editAnswerMode.value = -1
      }
    } else {
      error.value = true
      errorMessage.value = '모든 필수 항목을 입력해주세요.'
      return
    }
  }

  const deleteAnswer = async (answerId) => {
    try {
      const res = await axios.delete(`http://localhost:8080/api/qnas/answers/${answerId}`)
      console.log('답변 삭제 성공:', res.data)
      successMessage.value = '답변이 삭제되었습니다.'
      success.value = true
      getAnswers() // 답변 목록 갱신
    } catch (e) {
      console.error('답변 삭제 실패:', e)
      errorMessage.value = '답변 삭제에 실패했습니다.'
      error.value = true
    }
  }

</script>

<style scoped lang="scss">
  .answer-detail {
    color: gray;
    font-size: 0.8rem;
    margin-top: 8px;
    gap: 8px;
  }
</style>
