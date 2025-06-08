<template>
  <div class="d-flex flex-column" style="max-height: 80vh;">
    <div class="pa-2 px-3 d-flex justify-space-between ga-2">
      <v-btn class="text-surface overflow-hidden flex-shrink-1" color="info"
        size="small"
        @click="showImportRouteView"
        variant="tonal"
      >불러오기</v-btn>
      <v-btn class="text-surface overflow-hidden flex-shrink-1" color="warning"
        size="small"
        @click="moveCenter"
        variant="tonal"
      >지도 이동</v-btn>
    </div>

    <div v-if="routeViewStore.importRouteViewShow" 
      class="d-flex justify-center align-center flex-wrap pa-2 pt-0 ga-1"
      style="white-space: nowrap;"
    >
      <h3 class="text-center text-primary overflow-hidden text-truncate" style="min-width: 0;">
          {{ routeViewStore.routeViewTrip?.title }}
      </h3>
      <v-divider class="mx-2" vertical></v-divider>
      <h3 class="text-center text-primary overflow-hidden text-truncate" style="min-width: 0;">
          {{ routeViewStore.routeViewPlan?.title }}
      </h3>
      <span class="text-center text-body-2 font-weight-medium" style="color:gray;">
          {{ '('+(routeViewStore.routeViewPlan?.startDate || '')+' ~ '+(routeViewStore.routeViewPlan?.endDate || '')+')' }}
      </span>
    </div>

    <v-sheet class="scroll-pane px-3" rounded>
      <div v-for="(item, idx) in routeViewStore.routeView" :key="item.id">
        <v-sheet
          elevation="3"
          class="route-block pa-2 my-2"
          rounded
          :style="{ backgroundColor: attractionStore.detail?.contentId === item.contentId ? 'whitesmoke' : '' }"
          @click="$emit('select', item.contentId)"
        >
          <div class="d-flex ga-2">
            <div class="d-flex flex-column ga-1" style="max-width:40%;">
              <AppImage
                :src="item.firstImage1 || item.firstImage2 || elephantImg"
                :lazy-src="elephantImg"
                alt="장소 이미지"
                :width="90"
                :height="70"
              />
            </div>

            <div class="d-flex flex-column">
              <div class="d-flex ga-1">
                <v-icon
                  :icon="markerStore.icons[item.contentTypeId + '']"
                  size="small"
                  :color="markerStore.colors[item.contentTypeId]"
                />
                <h4 class="text-primary">{{ item.title }}</h4>
              </div>
              <p style="color: gray; font-size:13px;">{{ item.addr1 }}</p>
            </div>
          </div>
        </v-sheet>

        <div class="d-flex ga-3 justify-center align-center" v-if="routeViewStore.routeViewPath.length > idx">
          <span class="text-info" style="font-size: 14px;">🚘{{ routeViewStore.routeViewDuration[idx] }}</span>
          <v-btn
            class="pa-0"
            color="info"
            size="small"
            variant="tonal"
            target="_blank"
            :href="routeViewStore.routeViewPath[idx]"
          >
            길찾기
          </v-btn>
        </div>
      </div>
    </v-sheet>

    <!-- 하단 저장 버튼 -->
    <div class="pa-2 px-3 d-flex justify-space-between align-end">
      <v-btn
        color="error"
        size="small"
        variant="tonal"
        block
        :disabled="!routeViewStore.routeViewPlan"
        @click="routeViewStore.clearRouteView"
      >
        비우기
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import elephantImg from '@/assets/elephant-illustration.png'

import { useAttractionStore } from '@/stores/attraction'
import { useRouteViewStore } from '@/stores/Map/routeView'
import { useMarkerStore } from '@/stores/Map/marker'
import { useMapStore } from '@/stores/Map/map'

import { defineEmits } from 'vue'
const emit = defineEmits(['select'])

const attractionStore = useAttractionStore()
const routeViewStore = useRouteViewStore()
const markerStore = useMarkerStore()
const mapStore = useMapStore()

const showNewRoute = () => {
  if (!confirm('정말 새로 만드시겠습니까?')) {
    return
  }
  routeViewStore.clearRouteView()
  routeViewStore.importRouteView = false
}

const showImportRouteView = () => {
  routeViewStore.routeViewImportDialogShow = true
}

const moveCenter = () => {
  mapStore.fitbounds(markerStore.routeView)
}

watch (() => routeViewStore.importRouteView, () => {
  mapStore.fitbounds(markerStore.routeView)
})

</script>

<style scoped>
.route-block {
  transition: background-color 0.2s ease, box-shadow 0.2s ease;
}
.route-block:hover {
  background-color: whitesmoke;
}

/* 버튼 애니메이션 */
.transition-button {
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}
.transition-button:active {
  transform: scale(0.9);
}
</style>