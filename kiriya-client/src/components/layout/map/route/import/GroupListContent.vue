<template>
  <v-window v-model="viewIndex" class="scroll-pane w-100 h-100" touch transition="slide-slow">
    <!-- 그룹 목록 화면 -->
    <v-window-item :value="0" class="full-height">
      <div class="pa-10 pt-6 w-100 h-100 window-content">
        <div class="d-flex justify-center align-center mb-4">
          <h3 class="text-center text-primary ma-2">👥 그룹 목록</h3>
        </div>
        <div v-if="groupStore.isLoading" class="text-center pa-4">
          그룹 목록을 불러오는 중...
        </div>
        <div v-else-if="!groupStore.groups || groupStore.groups.length === 0" class="pa-4 text-center" style="color:gray;">
          참여중인 그룹이 없습니다.
        </div>
        <!-- Applied unified styles via classes -->
        <div v-for="group in groupStore.groups" :key="group.gno"
          class="my-2 rounded-lg pa-3 elevation-3 group-block list-item-block"
          @click="selectGroup(group)"
        >
          <div class="font-weight-bold text-primary text-truncate">
            {{ group.name }}
          </div>
          <div class="text-body-2 text-grey-darken-1 text-truncate">
            {{ group.description || '설명 없음' }}
          </div>
        </div>
      </div>
    </v-window-item>

    <!-- 여행 목록 화면 (선택된 그룹의) -->
    <v-window-item :value="1" class="full-height">
      <div class="pa-10 pt-6 w-100 h-100 window-content">
        <div class="d-flex align-center mb-4">
          <v-btn icon variant="text" color="primary" class="position-absolute" style="left:10px; top: 16px;"
            @click="goBackToGroupList">
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
          <div class="d-flex justify-center align-center w-100 ga-1 flex-wrap text-truncate px-12">
            <h3 class="text-primary ma-2 text-truncate" :title="selectedGroup?.name">
              🧳 '{{ selectedGroup?.name || '그룹' }}'의 여행
            </h3>
          </div>
        </div>

        <div v-if="isLoadingGroupTrips" class="text-center pa-4">
          여행 목록을 불러오는 중...
        </div>
        <div v-else-if="!groupTrips || groupTrips.length === 0" class="pa-4 text-center" style="color:gray;">
          이 그룹에는 여행이 없습니다.
        </div>
        <!-- Applied unified styles via classes -->
        <div v-for="trip in groupTrips" :key="trip.tno"
          class="my-2 rounded-lg pa-3 elevation-3 trip-block list-item-block"
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
    <v-window-item :value="2" class="full-height">
      <div class="pa-10 pt-6 w-100 h-100 window-content">
        <div class="d-flex align-center mb-4">
          <v-btn icon variant="text" color="primary" class="position-absolute" style="left:10px; top: 16px;"
            @click="goBackToTripList">
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
          <div class="d-flex justify-center align-center w-100 ga-1 flex-wrap text-truncate px-12">
            <h3 class="text-primary ma-2 text-truncate" :title="selectedTrip?.title">{{ selectedTrip?.title }}</h3>
            <span class="text-body-1 font-weight-bold" style="color:gray;">
              ({{ (selectedTrip?.startDate || '') + ' ~ '+ (selectedTrip?.endDate || '')}})
            </span>
          </div>
        </div>
        
        <div v-if="isLoadingPlans" class="text-center pa-4">
            일정 목록을 불러오는 중...
        </div>
        <div v-else-if="!planList || planList.length === 0" class="pa-4 text-center" style="color:gray;">
          일정이 없습니다.
        </div>
        <!-- Applied unified styles via classes -->
        <div v-for="plan in planList" :key="plan.pno"
          class="d-flex justify-space-between align-center my-2 pa-3 rounded elevation-3 plan-block list-item-block"
          @click="selectPlan(plan)"
        >
          <div class="overflow-hidden me-2" style="min-width: 0; flex-grow: 1;">
            <div class="font-weight-bold text-primary text-truncate">{{ plan.title }}</div>
            <div class="text-body-2 text-grey text-truncate">{{ plan.description }}</div>
            <div class="text-caption mt-1 text-grey">{{ plan.startDate }} ~ {{ plan.endDate }}</div>
          </div>
          <div class="d-flex ga-2 flex-shrink-0 ms-2">
            <v-btn size="small" variant="tonal" @click.stop="editPlanDialog(plan)">수정</v-btn>
            <v-btn size="small" variant="tonal" color="error" @click.stop="confirmDeletePlan(plan)">삭제</v-btn>
          </div>
        </div>
      </div>
    </v-window-item>
  </v-window>

  <PlanEditSlide v-model:show="planEditSlideShow" :plan="selectedPlan"
    @edit="handlePlanEdited" @create="handlePlanCreated" />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useGroupStore } from '@/stores/group'
import { useTripStore } from '@/stores/trip'
import { usePlanStore } from '@/stores/plan'
import { useRouteStore } from '@/stores/Map/route'
import PlanEditSlide from '../PlanEditSlide.vue' // Adjusted path

import axios from 'axios' // Assuming axios is configured globally or this is intended

const groupStore = useGroupStore()
const tripStore = useTripStore()
const planStore = usePlanStore()
const routeStore = useRouteStore()

const viewIndex = ref(0) // 0: Groups, 1: Trips, 2: Plans

const selectedGroup = ref(null)
const groupTrips = ref([])
const isLoadingGroupTrips = ref(false)

const selectedTrip = ref(null)
const planList = ref([])
const isLoadingPlans = ref(false)

const selectedPlan = ref(null) // This is used for both edit and selection for import
const planEditSlideShow = ref(false)

// Fetch initial groups
onMounted(async () => {
  // groupStore.isLoading = true; // Manage loading state within store or use local isLoadingGroups
  await loadGroups() // Changed from groupStore.fetchGroups to local loadGroups
  // groupStore.isLoading = false;
})

// 초기 로드 및 검색
async function loadGroups() {
  isLoadingGroupTrips.value = true; // Actually, this should be isLoadingGroups
  try {
    const res = await axios.get(`http://localhost:8080/api/groups`)
    const data = res.data.data || res.data || []
    groupStore._groups = data.map(g => ({ ...g, manage: true })) // Directly setting store state
    console.log('그룹 목록 조회 성공:', groupStore.groups)
  } catch (e) {
    console.error('그룹 목록 조회 실패:', e)
    groupStore._groups = [] // Ensure it's an empty array on failure
  } finally {
    isLoadingGroupTrips.value = false; // isLoadingGroups
  }
}

// Handler for selecting a group
async function selectGroup(group) {
  selectedGroup.value = group
  selectedTrip.value = null 
  planList.value = []       
  isLoadingGroupTrips.value = true
  const tripsData = await fetchTripsByGroup(group.gno)
  groupTrips.value = Array.isArray(tripsData) ? tripsData : []
  isLoadingGroupTrips.value = false
  viewIndex.value = 1 
}

/** 그룹 여행 불러오기 */
async function fetchTripsByGroup(gno) {
  console.log("Fetching trips for gno:",gno)
  try {
    const response = await axios.get(`http://localhost:8080/api/groups/${gno}/trips`)
    console.log("Group trips response:",response)
    return response.data.data?.trips || response.data.data || [] // Ensure array return
  } catch (e) {
    console.error('그룹 여행 조회 실패:', e)
    return [] // Return empty array on failure
  }
}

// Handler for selecting a trip from the group's list
async function selectTrip(trip) {
  selectedTrip.value = trip
  // selectedPlan.value = null; // Not strictly needed here as fetchPlansForSelectedTrip will clear planList
  await fetchPlansForSelectedTrip()
  viewIndex.value = 2 
}

// Fetch plans for the currently selected trip
async function fetchPlansForSelectedTrip() {
  if (!selectedTrip.value?.tno) {
    planList.value = []
    return
  }
  isLoadingPlans.value = true
  const plansData = await planStore.fetchPlans(selectedTrip.value.tno) 
  planList.value = Array.isArray(plansData) ? plansData : []
  isLoadingPlans.value = false
}

// Handler for selecting a plan (for import)
async function selectPlan(plan) {
  if (routeStore.routePlan && routeStore.route?.length > 0 && !confirm('작업중인 경로가 사라집니다. 정말 새로 불러오시겠습니까?')) {
    return
  }
  selectedPlan.value = { ...plan} 
  
  // Assuming fetchRoute fetches the full plan details including its route geometry
  routeStore.fetchRoute(plan.pno) 
  
    routeStore.importRoute(plan) // Import the full plan data
    routeStore.routeImportDialogShow = false
    routeStore.importRouteShow = true // Show the imported route view/controls
    routeStore.newRouteShow = false   // Hide new route creation view/controls
    routeStore.routeTrip = selectedTrip.value ? { ...selectedTrip.value } : null // Associate the trip context
}

// Open plan edit dialog
function editPlanDialog(plan) {
  selectedPlan.value = { ...plan } 
  planEditSlideShow.value = true
}

// After plan is edited in PlanEditSlide
async function handlePlanEdited() {
  planEditSlideShow.value = false 
  if (selectedTrip.value?.tno) {
    await fetchPlansForSelectedTrip() 
  }
  // selectedPlan.value = null; // Clear selection only if it's not needed for other purposes
}

// After plan is created
async function handlePlanCreated() {
    planEditSlideShow.value = false;
    if (selectedTrip.value?.tno) {
        await fetchPlansForSelectedTrip(); 
    }
}

// Handler for deleting a plan
async function confirmDeletePlan(planToDelete) {
  if (confirm(`'${planToDelete.title}' 일정을 삭제하시겠습니까?`)) {
    try {
      await planStore.deletePlan(planToDelete.pno)
      await fetchPlansForSelectedTrip()
      alert('삭제되었습니다.')
    } catch (error) {
      console.error('일정 삭제 실패:', error)
      alert('일정 삭제에 실패했습니다.')
    }
  }
}

// Navigation functions
function goBackToGroupList() {
  viewIndex.value = 0
  selectedGroup.value = null
  groupTrips.value = []
  selectedTrip.value = null
  planList.value = []
}

function goBackToTripList() {
  viewIndex.value = 1
  selectedTrip.value = null
  planList.value = []
}

watch(viewIndex, (newIndex, oldIndex) => {
  if (newIndex < oldIndex) { 
    if (newIndex === 1) { 
      selectedTrip.value = null
      planList.value = []
    } else if (newIndex === 0) { 
      selectedGroup.value = null
      groupTrips.value = []
      selectedTrip.value = null
      planList.value = []
    }
  }
})

</script>

<style scoped>
.full-height {
  height: 40vh; /* Or specific vh like 40vh if that's the design for this component */
}
.window-content {
  overflow-y: auto; /* Ensures content within each view is scrollable if it overflows */
  height: 40vh; /* Make sure window-content can fill full-height */
  display: flex; /* Added to allow pa-10 to work with height 100% */
  flex-direction: column; /* Added to allow pa-10 to work with height 100% */
}
.scroll-pane {
  /* Custom scrollbar styling can go here */
}

/* Generic transition button class (from original second file) */
/* This can be a fallback or used by other buttons if any */
.transition-button {
  cursor: pointer;
  /* Transitions defined by specific list-item-block will override this for those items */
  transition: background-color 0.2s ease-in-out, transform 0.1s ease-in-out;
}
.transition-button:hover {
  /* This background-color will be overridden by .list-item-block:hover */
  /* background-color: rgba(0, 0, 0, 0.05); */
}
.transition-button:active {
  /* This transform will be overridden by .list-item-block:active */
  /* transform: scale(0.98); */
}

/* Unified styles for list items (group, trip, plan blocks) */
/* Added a common class .list-item-block for easier targeting, alongside specific block classes */
.list-item-block {
  /* Base style from the first file */
  background-color: #f5f1ff;
  transition: background-color 0.2s ease, box-shadow 0.2s ease, transform 0.2s ease;
  cursor: pointer;
  /* overflow: hidden; -- Vuetify's pa-3 and rounded-lg should handle clipping. text-truncate on children is better. */
  /* white-space: nowrap; -- text-truncate on children is better for text elements. */
}

.list-item-block:hover {
  /* Hover style from the first file */
  background-color: white;
  box-shadow: 0 8px 16px rgba(140, 100, 240, 0.12);
  transform: translateY(-2px);
}

.list-item-block:active {
  /* Active state combining hover's Y shift and a consistent active scale */
  transform: translateY(-2px) scale(0.95); /* scale(0.95) is from the first file's general active style */
}

/* Specific block classes are still useful for semantic reasons or if any minor variations were ever needed */
.group-block { /* Can add specific group-block tweaks here if needed */ }
.trip-block { /* Can add specific trip-block tweaks here if needed */ }
.plan-block { /* Can add specific plan-block tweaks here if needed */ }

.position-absolute {
  position: absolute !important; 
  z-index: 1; /* Ensures back buttons are on top */
}
.text-truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 느린 슬라이드 전환 (from first file, ensure v-window has transition="slide-slow") */
/* Note: Vuetify's v-window already has built-in transitions. Custom ones might conflict
   unless you are not using Vuetify's transition prop or are augmenting it.
   If `transition="slide-slow"` on v-window is a custom name for these CSS transitions, it's fine.
*/
.slide-slow-enter-active,
.slide-slow-leave-active {
  transition: transform 0.5s cubic-bezier(0.25, 0.8, 0.5, 1), opacity 0.5s cubic-bezier(0.25, 0.8, 0.5, 1);
}
.slide-slow-enter-from {
  opacity: 0;
  transform: translateX(30%); /* Adjust for desired slide distance/direction */
}
.slide-slow-leave-to {
  opacity: 0;
  transform: translateX(-30%); /* Adjust for desired slide distance/direction */
}
/* .slide-slow-enter-to, .slide-slow-leave-from are implicitly opacity: 1, transform: translateX(0) */

</style>