<template>
  <v-dialog v-model="model" max-width="500">
    <v-card>
      <v-card-title>내 여행 중에서 추가</v-card-title>
      <v-card-text>
        <v-list style="max-height:350px;overflow:auto;">
          <v-list-item
            v-for="trip in myTrips"
            :key="trip.tno"
            @click="registerToGroup(trip.tno)"
            class="hover:bg-primary-lighten-4 cursor-pointer"
          >
            <v-list-item-title>{{ trip.title }}</v-list-item-title>
            <v-list-item-subtitle>
              {{ trip.startDate }} ~ {{ trip.endDate }}
            </v-list-item-subtitle>
          </v-list-item>
        </v-list>
        <div v-if="myTrips.length === 0" class="text-grey text-center py-6">
          등록 가능한 내 여행이 없습니다.
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn text @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import axios from 'axios'
import { useUserStore } from '@/stores/user'
const props = defineProps({
  modelValue: Boolean,
  groupGno: Number,
})
const emit = defineEmits(['update:modelValue', 'success'])

const model = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const userStore = useUserStore()
const myTrips = ref([])

const fetchMyTrips = async () => {
  try {
    // 내 여행 조회 API: /api/members/{mno}/trips
    const res = await axios.get(`/api/members/${userStore.profile.mno}/trips`)
    myTrips.value = res.data.data || []
  } catch (e) {
    myTrips.value = []
  }
}

watch(model, (val) => {
  if (val) fetchMyTrips()
})

const registerToGroup = async (tno) => {
  try {
    // 그룹에 여행 복제(공유) API 호출
    await axios.post(`/api/groups/${props.groupGno}/trips/${tno}`)
    emit('update:modelValue', false)
    emit('success')
    alert('그룹에 여행이 등록되었습니다!')
  } catch (e) {
    alert('등록 실패!')
  }
}
const close = () => emit('update:modelValue', false)
</script>
