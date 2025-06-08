<template>
  <v-container style="max-height: 1000px;" class="pa-0 ma-10 align-self-center">
    <v-card class="bg-surface pa-10" style="max-width: 1000px; min-height: 600px; margin: auto;">
      <h2 class="text-primary text-center ma-4">나의 그룹</h2>
      <!-- AppTable로 그룹 리스트 렌더링 -->
      <AppTable
        :headers="headers"
        :items="items"
        domain="groups"
      >
      <!-- 그룹 이름 클릭 시 상세로 이동 -->
      <template #name="{ item }">
          <RouterLink class="router-link" :to="{ name: 'GroupDetail' , params: {gno: item.gno }}" >{{ item.name }}</RouterLink>
      </template>
      <!-- 관리(수정/삭제) 버튼 -->
      <template #manage="{ item }">
          <v-btn size="small" color="primary" variant="tonal" class="ma-1"
            v-if="userStore.profile?.mno === item.mno" @click.stop="goEdit(item.gno)">수정</v-btn>
          <v-btn size="small" color="error" variant="tonal"
            v-if="userStore.profile?.mno === item.mno" @click.stop.prevent="onDelete(item.gno)">삭제</v-btn>
      </template>
      <!-- 그룹 생성 버튼 및 폼 -->
      <template #write>
      <v-btn color="primary" @click="goCreate">
          그룹 만들기
      </v-btn>
      </template>
      </AppTable>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from 'axios'
import AppTable from '@/components/common/AppTable.vue'

const router = useRouter()
const items = ref([])

const headers = [
  { label: '번호', key: 'gno' },
  { label: '이름', key: 'name' },
  { label: '설명', key: 'description' },
  { label: '관리', key: 'manage', sortable: false }
]

const userStore = useUserStore()
const isOwner = computed(() => userStore.profile?.mno === group.value?.mno)

// 초기 로드 및 검색
async function loadGruops() {
  try {
    const res = await axios.get(
      `http://localhost:8080/api/groups`
    )
    const data = res.data.data || res.data
    // API 리턴 형태에 따라 gno, emails 등 키 조정
    items.value = data.map(g => ({ ...g, manage: true }))

  } catch (e) {
    console.error('그룹 목록 조회 실패:', e)
  }
}

function goCreate() {
  router.push({ name: 'GroupCreate' })
}

function goEdit(gno) {
  router.push({ name: 'GroupEdit', params: { gno } })
}

async function onDelete(gno) {
  if (!confirm('정말 삭제하시겠습니까?')) return
  try {
    await axios.delete(`http://localhost:8080/api/groups/${gno}`)
    // 삭제 후 다시 조회
    loadGruops()
  } catch (e) {
    console.error('그룹 삭제 실패:', e)
  }
}

// 컴포넌트 최초 마운트 시 전체 목록 로드
loadGruops()
</script>

<style scoped>
/* 추가 스타일링 필요 시 작성 */
</style>
