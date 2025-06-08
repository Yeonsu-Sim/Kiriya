<template>
  <v-container class="py-12" style="max-width: 700px; position: relative;">
    
    <!-- 왼쪽 상단 < 버튼 -->
    <div style="position:absolute; top:55px; left:22px; z-index:3; 
      background:rgba(255,255,255,0.88); border-radius: 9999px;">
      <v-btn icon size="large" variant="text" @click="goList">
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
    </div>
    <!-- 오른쪽 상단 ... 버튼 -->
      <div style="position: absolute; top: 55px; right: 22px; z-index: 3; 
        background: rgba(255,255,255,0.88); border-radius: 9999px;">
        <v-menu transition="slide-y-transition" location="end">
          <template #activator="{ props }">
            <v-btn v-bind="props" icon size="large" variant="text">
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>
          <v-list min-width="120" class="py-2">
            <v-list-item @click="goEdit">
              <v-icon start size="small">mdi-pencil</v-icon>
              <span>수정</span>
            </v-list-item>
            <v-list-item @click="deleteTrip">
              <v-icon start size="small" color="error">mdi-delete</v-icon>
              <span class="text-error">삭제</span>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
      <v-img
        :src="trip.thumbnailUrl || defaultTripImage"
        height="270"
        class="w-100"
        cover
      />
      <div class="flex items-center justify-between mb-2">
        <h1 class="text-xl font-bold mt-3">{{ trip.title }}</h1>
        <v-btn
          v-if="!trip.gno"
          color="primary"
          class="rounded-pill font-bold ml-8 mt-4"
          variant="tonal"
          size="small"
          @click="showGroupModal=true">
          <v-icon start size="18">mdi-account-group</v-icon> 그룹 여행으로 추가
        </v-btn>
        <v-chip
          v-else
          color="accent"
          class="ml-8 mt-4"
          label
          varaint="flat"
        >
          <v-icon start size="18">mdi-account-group</v-icon>
          그룹여행
        </v-chip>
      </div>
      <AddToGroupModal
        v-model="showGroupModal"
        :trip="trip"
        @success="onAddToGroupSuccess"
      />
        <p class="text-grey-darken-1 text-base mb-2">
          {{ formatDate(trip.startDate) }} ~ {{ formatDate(trip.endDate) }}
        </p>
        <div class="mb-6" v-if="trip.description">
          <p class="text-body-1" style="white-space: pre-line">
            {{ trip.description }}
          </p>
        </div>
        <v-divider class="my-4" />

        <!-- 일정 리스트 -->
        <PlanTimeline :plans="plans" />
  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import PlanTimeline from '@/components/layout/trip/PlanTimeline.vue'
import defaultTripImage from '@/assets/default-trip.jpg'
import AddToGroupModal from '@/components/layout/trip/AddToGroupModal.vue'

const showGroupModal = ref(false)

const onAddToGroupSuccess = (gno) => {
}
const trip = ref({});
const plans = ref([]);
const loadingPlans = ref(false);
const route = useRoute();
const router = useRouter();
const tno = route.params.tno;



const fetchTrip = async () => {
  try {
    const res = await axios.get(`/api/trips/${tno}`);
    trip.value = res.data.data?.trip || {};
  } catch (err) {
    alert("여행 정보를 불러오지 못했습니다!");
    console.error(err);
  }
};

const fetchPlans = async () => {
  loadingPlans.value = true;
  try {
    const res = await axios.get(`/api/trips/${tno}/plans`);
    plans.value = res.data.data?.planList || [];
    console.log(plans.value);
  } catch (err) {
    alert("일정 정보를 불러오지 못했습니다!");
    console.error(err);
  } finally {
    loadingPlans.value = false;
  }
};

onMounted(() => {
  fetchTrip();
  fetchPlans();
});

const goEdit = () => {
  router.push(`/trips/edit/${tno}`);
};

const deleteTrip = async () => {
  if (!confirm("정말 삭제하시겠습니까?")) return;
  try {
    await axios.delete(`/api/trips/${tno}`);
    alert("삭제되었습니다!");
    router.push("/trips");
  } catch (err) {
    alert("삭제에 실패했습니다!");
    console.error(err);
  }
};

const goList = () => {
  router.push("/trips");
};

const formatDate = (dateStr) => dateStr?.split(" ")[0] || "";
const formatPlanDate = (start, end) =>
  [start, end]
    .filter(Boolean)
    .map((d) => d.split(" ")[0])
    .join(" ~ ");
</script>

<style scoped>
/* 심플&깔끔 스타일을 위한 추가 */
.v-card {

min-height: 800px;
  background: #fff;
  box-shadow: 0 6px 36px 0 rgba(55, 42, 128, 0.07);
}
.text-xl {
  font-size: 1.5rem;
}
.text-lg {
  font-size: 1.25rem;
}
.flex {
  display: flex;
}
.gap-2 {
  gap: 0.5rem;
}

</style>