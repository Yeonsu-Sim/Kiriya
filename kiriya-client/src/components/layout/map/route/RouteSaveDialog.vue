<template>
  <!-- 여행 목록 팝업 -->
  <v-dialog v-model="routeStore.routeSaveDialogShow"
    class="rounded-lg d-flex justify-center align-center"
    style="width:fit-content;"
  >
    <div class="dialog-card">
      <v-card class="elevation-0">
        <v-card-title class="text-center text-primary ma-4 mb-0">🧳 여행 목록</v-card-title>
    
        <v-card-text class="w-100 h-100 overflow-auto scroll-pane pa-10 pt-2">
          <AppList :items="tripList" domain="여행" 
            @create="createTrip"
            @select="select($event)"
            @edit="editTrip"
            @delete="deleteTrip($event)"
          />
        </v-card-text>
      </v-card>
  
      <div class="d-flex pa-4"></div>
      <TripCreateSlide v-model:show="tripCreateSlideShow" />
      <TripEditSlide v-model:show="tripEditSlideShow" :trip="targetedTrip"/>
      <PlanCreateSlide v-model:show="planCreateSlideShow" :trip="selectedTrip"/>
    </div>
  </v-dialog>
</template>

<script setup>
import AppList from '@/components/common/AppList.vue'
import TripCreateSlide from './TripCreateSlide.vue'
import TripEditSlide from './TripEditSlide.vue'
import PlanCreateSlide from './PlanCreateSlide.vue'

import { useRouteStore } from '@/stores/Map/route'
import { useUserStore } from '@/stores/user'
import { useTripStore } from '@/stores/trip'

import { onMounted } from 'vue'

const userStore = useUserStore()
const routeStore = useRouteStore()
const tripStore = useTripStore()

const tripCreateSlideShow = ref(false)
const planCreateSlideShow = ref(false)
const tripEditSlideShow = ref(false)

const tripList = computed(() =>
  tripStore.trips.map(item => ({
    id: item.tno,
    title: item.title,
    description: item.description,
    date: (item.startDate || '') +' ~ '+ (item.endDate || ''),
  }))
)

const selectedTrip = ref(null)
const targetedTrip = ref(null)

onMounted(() => {
  tripStore.fetchTrips()
})

watch(() => userStore.isLoggedIn, (val) => {
  if (val) {
    tripStore.fetchTrips()
  }
})

const createTrip = () => { tripCreateSlideShow.value = true }

const select = async (tno) => {
  selectedTrip.value = await tripStore.getTrip(tno)
  planCreateSlideShow.value = true
}

const editTrip = async (tno) => {
  targetedTrip.value = await tripStore.getTrip(tno)
  console.log(targetedTrip.value)
  tripEditSlideShow.value = true
}

const deleteTrip = async (tno) => { 
  if (confirm('정말 삭제하시겠습니까?')) {
    console.debug('hihi')
    await tripStore.deleteTrip(tno)
    alert('삭제되었습니다.')
  }
}

</script>

<style scoped>

.dialog-card {
  width: 60vw;
  height: 60vh;
  max-width: 600px;
  max-height: 600px;
  align-self: center;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 40px rgba(80, 70, 120, 0.17);
  position: relative;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

</style>