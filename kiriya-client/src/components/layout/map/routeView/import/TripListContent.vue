<template>
  <v-window v-model="viewIndex" class="scroll-pane" style="overflow-y:auto;" touch transition="slide-slow">
    <!-- 여행 목록 화면 -->
    <v-window-item :value="0" class="full-height">
      <div class="pa-10 pt-6 w-100 h-100 window-content">
        <div class="d-flex justify-center align-center mb-4">
          <h3 class="text-center text-primary ma-2">🧳 여행 목록</h3>
        </div>
        <div v-for="trip in tripStore.trips" :key="trip.tno"
          class="my-2 rounded-lg pa-3 elevation-3 trip-block transition-button"
          @click="selectTrip(trip)"
        >
          <div class="font-weight-bold text-primary text-truncate">
            {{ trip.title }}
          </div>
          <div class="text-body-2 text-grey-darken-1 text-truncate">
            {{ trip.description || '' }}
          </div>
          <div class="text-caption mt-1 text-grey">{{ trip.startDate }} ~ {{ trip.endDate }}</div>
        </div>
      </div>
    </v-window-item>

    <!-- 일정 목록 화면 -->
    <v-window-item :value="1" class="full-height">
      <div class="pa-10 pt-6 w-100 h-100 window-content">
        <div class="d-flex align-center mb-4">
          <v-btn icon variant="text" color="primary" class="position-absolute"
            @click="viewIndex = 0">
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
          <div class="d-flex justify-center align-center w-100 ga-1 flex-wrap text-truncate">
            <h3 class="text-primary ma-2">{{ selectedTrip?.title }}</h3>
            <span class="text-body-1 font-weight-bold" style="color:gray;">
              ({{ (selectedTrip?.startDate || '') + ' ~ '+ (selectedTrip?.endDate || '')}})
            </span>
          </div>
        </div>
        
        <div v-if="planList.length === 0" class="pa-4 text-center" style="color:gray;">
          일정이 없습니다.
        </div>
        <div v-for="plan in planList" :key="plan.pno"
          class="d-flex justify-space-between align-center my-2 pa-3 rounded elevation-3 plan-block transition-button"
          @click="selectPlan(plan)"
        >
          <!-- 왼쪽 텍스트 영역 -->
          <div class="overflow-hidden" style="min-width: 0;">
            <div class="font-weight-bold text-primary text-truncate">{{ plan.title }}</div>
            <div class="text-body-2 text-grey text-truncate">{{ plan.description }}</div>
            <div class="text-caption mt-1">{{ plan.startDate }} ~ {{ plan.endDate }}</div>
          </div>

          <!-- 오른쪽 버튼 영역 -->
          <div class="d-flex ga-2 flex-shrink-0 ms-4">
            <v-btn variant="tonal" @click.stop="editPlan(plan)">수정</v-btn>
            <v-btn variant="tonal" color="error" @click.stop="deletePlan(plan)">삭제</v-btn>
          </div>
        </div>
      </div>
    </v-window-item>
  </v-window>
  <PlanEditSlide v-model:show="planEditSlideShow" :plan="selectedPlan" 
    @edit="fetchPlans(selectedPlan.tno)"/>
</template>

<script setup>
import PlanEditSlide from '../../route/PlanEditSlide.vue'
import { ref } from 'vue'

import { useRouteViewStore } from '@/stores/Map/routeView'
import { useTripStore } from '@/stores/trip'
import { usePlanStore } from '@/stores/plan'

const routeViewStore = useRouteViewStore()
const tripStore = useTripStore()
const planStore = usePlanStore()

const viewIndex = ref(0)

const selectedTrip = ref(null)
const selectedPlan = ref(null)
const planList = ref([])

const planEditSlideShow = ref(false)

const fetchPlans = async (tno) => {
  planList.value = await planStore.fetchPlans(tno)
}

const selectTrip = async (trip) => {
  selectedTrip.value = trip
  fetchPlans(trip.tno)
  viewIndex.value = 1
}

watch (() => routeViewStore.routeViewImportDialogShow, () => {
  if (selectedTrip.value) {
    fetchPlans(selectedTrip.value.tno)
  }
})

const selectPlan = async (plan) => {
  if (routeViewStore.routeViewPlan && !confirm('정말 새로 불러오시겠습니까?')) {
    return
  }
  await routeViewStore.fetchRouteView(plan.pno)
  routeViewStore.importRouteView(plan)
  routeViewStore.routeViewImportDialogShow = false
  routeViewStore.importRouteViewShow = true
  routeViewStore.routeViewTrip = selectedTrip.value
  // viewIndex.value = 0
}

const editPlan = async (plan) => {
  selectedPlan.value = plan
  planEditSlideShow.value = true
}

const deletePlan = async (plan) => {
  if (confirm('정말 삭제하시겠습니까?')) {
    await planStore.deletePlan(plan.pno)
    fetchPlans(plan.tno)
    alert('삭제되었습니다.')
  }
}

</script>

<style scoped>
.trip-block, 
.plan-block {
  transition: background-color 0.2s ease, box-shadow 0.2s ease, transform 0.2s ease;
  cursor: pointer;
  overflow: hidden;
  white-space: nowrap;
  background-color: #f5f1ff;
}
.trip-block:hover,
.plan-block:hover {
  background-color: white;
  box-shadow: 0 8px 16px rgba(140, 100, 240, 0.12);
  transform: translateY(-2px);
}

.full-height {
  height: 40vh !important;
}

.window-content {
  height: 40vh;
  display: flex;
  flex-direction: column;
}

/* 버튼 애니메이션 */
.transition-button {
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}
.transition-button:active {
  transform: scale(0.95);
}

/* 커스텀 효과 */
.hover-elevate:hover {
  box-shadow: 0 6px 20px rgba(160, 120, 240, 0.15);
}
.hover-fade:hover {
  filter: brightness(1.08);
}
.fancy-shadow {
  box-shadow: 0 10px 40px rgba(130, 100, 230, 0.2);
}
.fancy-title {
  font-weight: 700;
  font-size: 20px;
  letter-spacing: 0.5px;
}

/* 느린 슬라이드 전환 */
.slide-slow-enter-active,
.slide-slow-leave-active {
  transition: transform 0.5s ease, opacity 0.5s ease;
}
.slide-slow-enter-from,
.slide-slow-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
.slide-slow-enter-to,
.slide-slow-leave-from {
  transform: translateX(0%);
  opacity: 1;
}
</style>
