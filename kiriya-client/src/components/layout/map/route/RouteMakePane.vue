<template>
  <div class="d-flex flex-column" style="max-height: 80vh;">
    <!-- 상단 버튼 -->
    <div class="pa-2 px-3 d-flex justify-space-between ga-2">
      <v-btn color="success flex-shrink-1" size="small" variant="tonal" @click="showNewRoute">새 경로</v-btn>
      <v-btn color="info flex-shrink-1" size="small" variant="tonal" @click="showImportRoute">불러오기</v-btn>
      <v-btn color="warning flex-shrink-1" size="small" variant="tonal" @click="moveCenter">지도 이동</v-btn>
    </div>

    <!-- 제목 -->
    <div v-if="routeStore.importRouteShow" 
      class="d-flex justify-center align-center flex-wrap pa-2 pt-0 ga-1"
      style="white-space: nowrap;">
      <h3 class="text-center text-primary overflow-hidden text-truncate" style="min-width: 0;">
          {{ routeStore.routeTrip?.title }}
      </h3>
      <v-divider class="mx-2" vertical></v-divider>
      <h3 class="text-center text-primary overflow-hidden text-truncate" style="min-width: 0;">
        {{ routeStore.routePlan?.title }}
      </h3>
      <span class="text-center text-body-2 font-weight-medium" style="color:gray;">
        {{ '('+(routeStore.routePlan?.startDate || '')+' ~ '+(routeStore.routePlan?.endDate || '')+')' }}
      </span>
    </div>

    <!-- 최단 경로 추천 -->
    <div class="pb-2 px-3 d-flex justify-space-between">
      <v-btn size="small" color="secondary" block @click="getShortestRoute">최단경로 추천받기</v-btn>
    </div>

    <!-- 드래그 가능한 리스트 -->
    <v-sheet class="scroll-pane px-3" rounded>
      <draggable
        :list="routeStore.route"
        item-key="contentId"
        handle=".drag-handle"
      >
        <template #item="{ element: item, index: idx }">
          <div>
            <v-sheet class="route-block pa-2 my-2" elevation="3" rounded
              :style="{ backgroundColor: (attractionStore.detail?.contentId === item.contentId) ? 'whitesmoke' : '' }"
              @click="$emit('select', item.contentId)">
              <div class="d-flex justify-space-between ga-2">
                <!-- 좌측 정보 -->
                <div class="d-flex ga-2">
                  <div class="d-flex flex-column ga-1" style="max-width:40%;">
                    <AppImage :src="item.firstImage1 || item.firstImage2 || elephantImg" :lazy-src="elephantImg" alt="장소 이미지" :width="90" :height="70" />
                    <div class="d-flex justify-space-around align-center">
                      <v-tooltip location="top" :content-class="'custom-tooltip'">
                        <template #activator="{ props }">
                          <v-btn
                            v-bind="props"
                            icon
                            size="x-small"
                            variant="tonal"
                            class="transition-button"
                            :class="{ active: routeStore.startContentId === item.contentId }"
                            :color="routeStore.startContentId === item.contentId ? 'error' : 'lodging'"
                            @click.stop="routeStore.startContentId = item.contentId"
                          >
                            <v-icon>mdi-flag</v-icon>
                          </v-btn>
                        </template>
                        출발지
                      </v-tooltip>

                      <v-tooltip location="top" :content-class="'custom-tooltip'">
                        <template #activator="{ props }">
                          <v-btn
                            v-bind="props"
                            icon
                            size="x-small"
                            variant="tonal"
                            class="transition-button"
                            :class="{ active: routeStore.endContentId === item.contentId }"
                            :color="routeStore.endContentId === item.contentId ? 'info' : 'lodging'"
                            @click.stop="routeStore.endContentId = item.contentId"
                          >
                            <v-icon>mdi-flag-checkered</v-icon>
                          </v-btn>
                        </template>
                        도착지
                      </v-tooltip>
                    </div>
                  </div>

                  <div class="d-flex flex-column" style="min-width: 0; flex: 1;">
                    <div class="d-flex align-center ga-1 overflow-hidden" style="min-width: 0;">
                      <v-icon
                        :icon="markerStore.icons[item.contentTypeId + '']"
                        size="small"
                        :color="markerStore.colors[item.contentTypeId]"
                      />
                      <h4 class="text-primary" style="text-overflow:ellipsis">
                        {{ item.title }}
                      </h4>
                    </div>
                    <p style="color: gray; font-size:13px; text-overflow:ellipsis">{{ item.addr1 }}</p>
                  </div>
                </div>

                <!-- 우측 컨트롤 -->
                <div class="route-controls" @click.stop>
                  <v-btn icon size="small" variant="text" color="error" @click="routeStore.removeRoute(item)">
                    <v-icon>mdi-minus-circle</v-icon>
                  </v-btn>
                  <v-btn icon size="x-small" variant="text" @click="moveUp(idx)">
                    <v-icon>mdi-chevron-up</v-icon>
                  </v-btn>
                  <div class="drag-handle">
                    <v-icon>mdi-drag</v-icon>
                  </div>
                  <v-btn icon size="x-small" variant="text" @click="moveDown(idx)">
                    <v-icon>mdi-chevron-down</v-icon>
                  </v-btn>
                  <div class="pa-1"></div>
                </div>
              </div>
            </v-sheet>

            <div class="d-flex ga-3 justify-center align-center" v-if="routeStore.routePath.length > idx">
              <span class="text-info" style="font-size: 14px;">🚘{{ routeStore.routeDuration[idx] }}</span>
              <v-btn class="pa-0" color="info" size="small" variant="tonal" target="_blank" :href="routeStore.routePath[idx]">
                길찾기
              </v-btn>
            </div>
          </div>
        </template>
      </draggable>
    </v-sheet>

    <!-- 저장 버튼 -->
    <div class="pa-2 px-3 d-flex justify-space-between align-end" v-if="routeStore.newRouteShow">
      <v-btn color="primary" size="small" variant="tonal" :disabled="routeStore.route.length <= 0" @click="routeStore.routeSaveDialogShow = true">
        저장하기
      </v-btn>
    </div>
    <div class="pa-2 px-3 d-flex justify-space-between align-end" v-if="routeStore.importRouteShow">
      <v-btn color="primary" size="small" variant="tonal" :disabled="routeStore.route.length <= 0" @click="planStore.updatePlan">저장하기</v-btn>
      <v-btn class="text-surface" size="small" color="error" variant="tonal" :disabled="routeStore.route.length <= 0" @click="routeStore.routeSaveDialogShow = true">다른이름으로 저장</v-btn>
    </div>
  </div>
</template>

<script setup>
import draggable from 'vuedraggable'
import elephantImg from '@/assets/elephant-illustration.png'
import { useAttractionStore } from '@/stores/attraction'
import { useRouteStore } from '@/stores/Map/route'
import { useMarkerStore } from '@/stores/Map/marker'
import { useMapStore } from '@/stores/Map/map'
import { usePlanStore } from '@/stores/plan'

const attractionStore = useAttractionStore()
const routeStore = useRouteStore()
const markerStore = useMarkerStore()
const mapStore = useMapStore()
const planStore = usePlanStore()

const getShortestRoute = async () => {
  await routeStore.getShortestRoute()
  mapStore.fitbounds(markerStore.route)
}

const showNewRoute = () => {
  if (routeStore.route.length > 0 && !confirm('작업중인 경로가 사라집니다. 정말 새로 만드시겠습니까?')) return
  routeStore.clearRoute()
  routeStore.newRouteShow = true
  routeStore.importRouteShow = false
}

const showImportRoute = () => {
  routeStore.routeImportDialogShow = true
}

const moveCenter = () => {
  mapStore.fitbounds(markerStore.route)
}

const moveUp = (idx) => {
  if (idx <= 0) return
  const temp = routeStore.route[idx]
  routeStore.route.splice(idx, 1)
  routeStore.route.splice(idx - 1, 0, temp)
}

const moveDown = (idx) => {
  if (idx >= routeStore.route.length - 1) return
  const temp = routeStore.route[idx]
  routeStore.route.splice(idx, 1)
  routeStore.route.splice(idx + 1, 0, temp)
}

</script>

<style scoped>
.route-block {
  position: relative;
  transition: background-color 0.2s ease, box-shadow 0.2s ease;
}
.route-block:hover {
  background-color: whitesmoke;
}
.route-controls {
  position: absolute;
  display: flex;
  right: 0px;
  top: -8px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s ease;
  z-index: 2;
  color:gray;
  height:110%;
}
.route-block:hover .route-controls {
  opacity: 1;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 6px;                         /* 약간 둥글게 */
  backdrop-filter: blur(3px);                /* 배경 흐림 효과 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);   /* 부드러운 그림자 */
}

.drag-handle {
  cursor: grab;
  padding-top: 2px;
}
</style>
