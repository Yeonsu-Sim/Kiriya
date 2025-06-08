<template>
  <v-container class="py-8">
    <v-row>
      <v-col cols="12" class="d-flex justify-between align-center mb-4">
        <h2 class="text-h5 font-weight-bold">내 여행</h2>
        <v-btn color="primary" @click="onAddTrip" class="rounded-xl ml-3" elevation="2">
          <v-icon left>mdi-plus</v-icon> 새 여행
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        v-for="trip in trips"
        :key="trip.tno"
        cols="12" sm="6" md="4" lg="3"
        class="mb-4"
      >
      <TripCard
        :trip="trip"
        @detail="goDetail"
        @delete="deleteTrip"
      />
      </v-col>
      <v-col v-if="trips.length === 0" cols="12" class="text-center py-12">
        <v-img
          :src="emptyTripImage"
          width="160"
          class="mx-auto mb-4"
        />
        <p class="text-h6 font-weight-bold mb-2">아직 등록된 여행이 없어요!</p>
        <v-btn color="primary" @click="onAddTrip">새 여행 추가하기</v-btn>
      </v-col>
    </v-row>
    <!-- Floating Action Button (FAB) -->
    <v-btn
      class="fab"
      color="primary"
      icon
      large
      fixed
      bottom
      right
      @click="onAddTrip"
      style="position: fixed; bottom: 36px; right: 36px;"
    >
      <v-icon>mdi-plus</v-icon>
    </v-btn>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import axios from 'axios'
import TripCard from '@/components/layout/trip/TripCard.vue'
import emptyTripImage from '@/assets/empty-trip.png'

const router = useRouter()

const trips = ref([])
const userStore = useUserStore()
const mno = userStore.profile.mno

// 여행 목록 가져오기
const fetchTrips = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/members/${mno}/trips`)
    trips.value = res.data.data || []
  } catch (err) {
    console.error('여행 목록 조회 실패', err)
  }
}

onMounted(fetchTrips)

// 여행 추가 버튼
const onAddTrip = () => {
  // 예시: 라우터 이동
  router.push('/trips/write')
  alert('여행 추가 폼으로 이동!')
}

// 상세보기
const goDetail = (tno) => {
  router.push(`/trips/${tno}`)
}

// 삭제
const deleteTrip = async (tno) => {
  if (!confirm('정말 삭제할까요?')) return
  try {
    await axios.delete(`http://localhost:8080/api/trips/${tno}`)
    trips.value = trips.value.filter(t => t.tno !== tno)
  } catch (err) {
    alert('삭제 실패!')
    console.error(err)
  }
}
</script>
