<template>
  <div v-if="!plans.length" class="empty-plan-center">
      <div class="mb-4">등록된 일정이 없습니다.</div>
      <v-btn
        color="primary"
        size="large"
        class="gradient-btn rounded-pill font-bold"
        @click="goAddPlan"
      >
        <v-icon start size="20">mdi-plus</v-icon> 일정 만들러 가기
      </v-btn>
    </div>
  <v-timeline v-else align="start" side="end" class="mb-4 mt-8" line-color="#bb9fff">
    <v-timeline-item
      v-for="plan in plans"
      :key="plan.pno"
      dot-color="primary"
      size="x-large"
      fill-dot
    >
      <!-- 플랜(일차) 제목을 카드 위에 크게 -->
      <template #opposite>
        <div class="font-extrabold text-2xl text-primary mb-2">
          {{ plan.title }}
        </div>
        <div class="text-grey-darken-1 text-sm mb-1">
          {{ formatDate(plan.startDate) }}
        </div>
      </template>

      <div class="flex flex-col">
        <div
          v-for="(route, idx) in plan.route || []"
          :key="route.no"
          class="mb-0"
        >
          <!-- 경로 제목을 카드 밖에 더 크게 -->
          <div class="font-bold text-lg mb-1 px-1">
            <v-icon color="primary" size="30" class="mr-1"
              >mdi-map-marker</v-icon
            >
            {{ route.title }}
          </div>
          <v-card
            class="pa-0 mb-3"
            style="
              border-radius: 20px;
              box-shadow: 0 4px 20px 0 rgba(70, 60, 180, 0.07);
              width: 400px;
            "
          >
            <v-img
              v-if="route.firstImage1"
              :src="route.firstImage1"
              height="180"
              class="rounded-t-xl"
              cover
            />
            <div class="px-5 py-4">
              <div
                class="text-xs text-grey mb-2"
                style="font-size: 13px; color: #888"
              >
                {{ route.addr1 }}
              </div>
              <div class="text-body-1">{{ route.description }}</div>
            </div>
          </v-card>

          <!-- 경로 보기 버튼 (다음 장소가 있을 때만) -->
          <div
            v-if="idx < (plan.route?.length || 0) - 1"
            class="text-center my-3"
          >
            <v-btn
              :href="getNaverMapLink(route, plan.route[idx + 1])"
              target="_blank"
              variant="tonal"
              color="primary"
              class="gradient-btn"
              size="small"
              style="min-width: 100px"
            >
              <v-icon start size="18">mdi-route</v-icon>
              경로 보기
              <v-icon end size="18">mdi-chevron-right</v-icon>
            </v-btn>
          </div>
        </div>
        <div
          v-if="!(plan.route && plan.route.length)"
          class="text-grey text-center py-2"
        >
          등록된 장소가 없습니다.
        </div>
      </div>
    </v-timeline-item>
    
  </v-timeline>
</template>
  
<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goAddPlan = () => {
  router.push('/map')
  // 또는 실제 라우트에 맞게 수정 (ex. /plans/new?tno=)
}
const props = defineProps({
  plans: Array,
});
const formatDate = (d) => d?.split(" ")[0] || "";
const getNaverMapLink = (src, dst) =>
  `http://map.naver.com/index.nhn` +
  `?slng=${src.longitude}&slat=${src.latitude}&stext=${encodeURIComponent(
    src.title
  )}` +
  `&elng=${dst.longitude}&elat=${dst.latitude}&etext=${encodeURIComponent(
    dst.title
  )}` +
  `&pathType=0&showMap=true&menu=route`;
</script>
  
  <style scoped>
.text-2xl {
  font-size: 1.6rem;
}
.font-extrabold {
  font-weight: 900;
}
.text-lg {
  font-size: 1.2rem;
}
.rounded-pill {
  border-radius: 100px !important;
}
.empty-plan-center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;     /* 가로축 중앙 */
  min-height: 280px;       /* 버튼+문구가 어느 정도 아래쪽에도 보이게 */
  width: 100%;
  text-align: center;
}
</style>
  