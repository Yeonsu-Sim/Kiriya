<template>
  <v-row justify="center" align="center" class="pa-4">
    <v-card color="surface" class="w-100 pa-4" style="max-width: 1000px">
      <h2 class="text-primary text-center ma-4">공지 상세 보기</h2>

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
            <v-divider></v-divider>
          </v-col>
          <v-col cols="12">
            <v-sheet color="grey-lighten-4" rounded>
              <div class="d-flex justify-space-between align-center flex-wrap">
                <span>작성자: {{ author }}</span>
                <span>작성일: {{ createdAt }}</span>
                <span>수정일: {{ updatedAt }}</span>
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
</template>

<script setup>
  import { ref, reactive } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/user'
  import axios from 'axios'


  const router = useRouter()
  const route = useRoute()

  const userStore = useUserStore()

  const title = ref('')
  const content = ref('')
  const author = ref('')
  const createdAt = ref('')
  const updatedAt = ref('')

  onMounted(async () => {
    const id = route.params.id
    try {
      const res = await axios.get(`http://localhost:8080/api/notices/${id}`)
      const data = res.data.data
      title.value = data.title
      content.value = data.content
      createdAt.value = new Date(data.createdAt).toLocaleString('ko-KR', { timeZone: 'Asia/Seoul', hour12: true, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
      updatedAt.value = new Date(data.updatedAt).toLocaleString('ko-KR', { timeZone: 'Asia/Seoul', hour12: true, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
      author.value = data.author
    } catch (e) {
      console.error('공지사항 불러오기 실패:', e)
    }
  })

  const edit = () => {
    const id = route.params.id
    router.push({ name: 'NoticeEdit'})
  }

  const showList = () => {
    router.push({ name: 'Support', query: { tab: 'notice' } })
  }

</script>

<style scoped lang="scss">
</style>