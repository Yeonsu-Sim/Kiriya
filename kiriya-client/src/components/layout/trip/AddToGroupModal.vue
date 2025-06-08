<template>
  <v-dialog v-model="model" max-width="400">
    <v-card>
      <v-card-title><span>여행을 공유할 그룹 선택</span>
        <v-btn
            v-if="groups.length === 0"
            color="primary"
            size="small"
            @click="goCreateGroup"
        ><v-icon start size="18">mdi-plus</v-icon> 그룹 만들기</v-btn>
    </v-card-title>
      <v-card-text>
        <v-list>
          <v-list-item
            v-for="group in groups"
            :key="group.gno"
            @click="addToGroup(group.gno)"
            class="hover:bg-primary-lighten-4 cursor-pointer"
          >
            <v-list-item-title>{{ group.name }}</v-list-item-title>
          </v-list-item>
        </v-list>
        <div v-if="groups.length === 0" class="text-grey text-center">가입한 그룹이 없습니다.</div>
      </v-card-text>
      <v-card-actions>
        <v-btn text @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const goCreateGroup = () => {
  router.push('/groups/create') // 실제 그룹 생성 페이지 경로에 맞게 수정!
}

const props = defineProps({
  modelValue: Boolean,
  trip: Object
})
const emit = defineEmits(['update:modelValue', 'success'])

// v-model 동기화 (Vue3 표준 패턴)
const model = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const groups = ref([])
const userStore = useUserStore()
const userMno = userStore.profile?.mno

// 내가 속한 그룹 리스트 불러오기
const fetchGroups = async () => {
  try {
    // 예시 API: /api/members/{mno}/groups
    const res = await axios.get(`/api/groups`)
    groups.value = res.data.data || []
  } catch (e) {
    groups.value = []
    alert('그룹 조회 실패')
  }
}

// 모달 열릴 때마다 그룹리스트 fetch
watch(model, (val) => {
  if (val) fetchGroups()
})

// 그룹 클릭 → 여행 그룹에 복제/공유
const addToGroup = async (gno) => {
  try {
    // 예시 API: /api/groups/{gno}/trips/{tno}/copy (POST)
    await axios.post(`/api/groups/${gno}/trips/${props.trip.tno}`)
    emit('update:modelValue', false)
    emit('success', gno)
    alert('그룹에 여행이 공유되었습니다!')
  } catch (e) {
    alert('공유 실패!')
  }
}

const close = () => emit('update:modelValue', false)
</script>
