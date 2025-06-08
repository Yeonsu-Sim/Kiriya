<template>
  <div class="pane-container pa-4 h-100 w-100">
    <div class="position-relative d-flex flex-column h-100 w-100 pb-8">
      <div class="d-flex flex-row ga-4">
        <FilterPane class="interactive flex-shrink-1" style="z-index:100;" @search="searchAttractions($event)"/>
      </div>
      <div class="flex-grow-1 d-flex w-100">

        <AttractionPane
          class="h-100 interactive flex-shrink-1"
          v-show="attractionPaneShow"
          :width="300"
          @select="selectAttraction($event)"
          @like="like($event)"
          @unlike="unlike($event)"
        />
        
        <div class="mx-2" v-show="attractionPaneShow && attractionDetailPaneShow"></div>
        
        <AttractionDetailPane 
          v-show="attractionDetailPaneShow && attractionStore.detail"
          class="h-100 interactive flex-shrink-1"
          :width="300"
          :color="'surface'"
          @closeAttractionDetailPane="onCloseAttractionDetailPane"
          @like="like($event)"
          @unlike="unlike($event)"
        />
        <v-btn 
          class="align-self-center pa-2 h-auto interactive bg-blur"
          style="min-width: 0"
          color="primary"
          variant="tonal"
          v-show="attractionPaneShow"
          @click="attractionPaneShow = false; attractionDetailPaneShow = false"
        >
          <
        </v-btn>
        <v-btn 
          class="align-self-center h-auto pa-2 interactive bg-blur"
          style="min-width: 0"
          color="primary"
          variant="tonal"
          v-show="!attractionPaneShow"
          @click="attractionPaneShow = true; attractionDetailPaneShow = true"
        >
          >
        </v-btn>
        
        <div class="ms-auto d-flex">
          <v-btn 
            class="align-self-center h-auto pa-2 interactive bg-blur" 
            style="min-width: 0"
            color="primary"
            variant="tonal"
            v-show="routePaneShow"
            @click="routePaneShow = false"
          >
            >
          </v-btn>
          <v-btn 
            class="align-self-center h-auto pa-2 interactive bg-blur" 
            style="min-width: 0"
            color="primary"
            variant="tonal"
            v-show="!routePaneShow"
            @click="routePaneShow = true"
          >
            <
          </v-btn>
          <RoutePane 
            class="interactive flex-shrink-1"
            :width="300"
            v-show="routePaneShow"
            @select="selectAttraction($event)"
          />
        </div>
      </div>
    </div>
  </div>
  <div class="d-flex justify-center"></div>

  <MapView 
    @select="selectAttraction($event)"
  />

  <RouteSaveDialog />
  <RouteImportDialog />
  <RouteViewImportDialog />
</template>

<script setup>
import MapView from '@/components/layout/map/MapView.vue'
import FilterPane from '@/components/layout/map/FilterPane.vue'
import AttractionPane from '@/components/layout/map/AttractionPane.vue'
import AttractionDetailPane from '@/components/layout/map/attraction/AttractionDetailPane.vue'
import RoutePane from '@/components/layout/map/RoutePane.vue'
import RouteSaveDialog from '@/components/layout/map/route/RouteSaveDialog.vue'
import RouteImportDialog from '@/components/layout/map/route/RouteImportDialog.vue'
import RouteViewImportDialog from '@/components/layout/map/route/RouteViewImportDialog.vue'

import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useFilterStore } from '@/stores/Map/filter'
import { useAttractionStore } from '@/stores/attraction'
import { useMarkerStore } from '@/stores/Map/marker'
import { useMapStore } from '@/stores/Map/map'
import { useBucketlistStore } from '@/stores/bucketlist'

const router = useRouter()
const route = useRoute()
const filterStore = useFilterStore()
const attractionStore = useAttractionStore()
const markerStore = useMarkerStore()
const mapStore = useMapStore()
const bucketlistStore = useBucketlistStore()

const attractionPaneShow = ref(true)
const attractionDetailPaneShow = ref(false)
const routePaneShow = ref(false)

// ✅ 최초 1회 실행
onMounted(async () => {
  if (Object.keys(route.query).length > 0) {
    await searchAttractions(route.query)
  }
})

onUnmounted( () => {
  markerStore.clearAllMarkers()
  attractionStore.clearAttractions()
  filterStore.resetFilter()
})

const onCloseAttractionDetailPane = () => {
  attractionDetailPaneShow.value = false
  attractionStore.clearDetail()
}

// ✅ route.query 변경 감지
watch(
  () => route.fullPath,
  async (newPath, oldPath) => {
    console.log('경로 변경:', oldPath, '→', newPath)
    const newQuery = route.query
    // if (Object.keys(newQuery).length > 0) {
    if (newQuery.sidoCode || newQuery.gunguCode) {
      await searchAttractions()
    } else {
      filterStore.resetFilter()
      attractionStore.clearAttractions()

      markerStore.clearAllMarkers()
      markerStore.addAttraction({
        ...mapStore.coord,
        color: 'error',
        icon: 'mdi-crosshairs-gps',
        label: '현위치',
        contentId: -1
      })
      mapStore.fitbounds(markerStore.attractions)
    }
  },
  { immediate: false, deep: true }
)

watch(
  () => filterStore.selectedContentTypes,
  async () => {
    const sidoCode = route.query.sidoCode
    const gunguCode = route.query.gunguCode

    if (sidoCode || gunguCode ) {
      // await searchAttractions(true)
      await searchAttractions()
    }
  },
  { immediate: false, deep: true }
)

const searchAttractions = async (noFitBounds = false) => {
  console.log(route.query)
  const queryObj = {
    ...route.query,
    contentTypeList: filterStore.selectedContentTypes,
  }
  await attractionStore.fetchAttractions(queryObj)
  console.log("attractions: ",attractionStore.attractions)

  updateAttractionMarkers(attractionStore.attractions, noFitBounds)
}

const selectAttraction = async (contentId) => {
  console.log('디테일 살피기:', contentId)
  await attractionStore.fetchAttractionDetail(contentId)
  attractionDetailPaneShow.value = true

  let selectedMarker = markerStore.attractions.find(e => e.contentId == contentId)
  if (selectedMarker === undefined) {
      selectedMarker = markerStore.bucketlists.find(e => e.contentId === contentId)
  }
  if (selectedMarker === undefined) {
      selectedMarker = markerStore.route.find(e => e.contentId === contentId)
  }
  if (selectedMarker === undefined) {
      selectedMarker = markerStore.routeView.find(e => e.contentId === contentId)
  }
  
  mapStore.panToCenter(selectedMarker)
  console.log("detail:", attractionStore.detail)
}

// 관광지 마커 갱신
const updateAttractionMarkers = (items, noFitBounds) => {
  markerStore.clearAttractions()
  items.forEach(item => {
    const marker = {
      lat: item.latitude,
      lng: item.longitude,
      icon: markerStore.icons[item.contentTypeId],
      color: markerStore.colors[item.contentTypeId],
      label: item.title,
      contentId: item.contentId,
    }
    markerStore.addAttraction(marker)
  })

  console.log("attraction markers:", markerStore.attractions)

  if (!noFitBounds) {
    // 지도 중심 이동
    mapStore.fitbounds(markerStore.attractions)
  }
}

const like = async (contentId) => {
  await bucketlistStore.like(contentId)
}

const unlike = async (contentId) => {
  await bucketlistStore.unlike(contentId)
}

</script>

<style lang="scss" scoped>
.pane-container {
  position: fixed;
  z-index: 10;
  // padding-bottom: 64px !important;
  max-height: 93% !important;
  pointer-events: none; // 전체적으로 이벤트 비활성화
}

.interactive {
  pointer-events: auto; // 필요한 컴포넌트만 이벤트 가능
}

</style>
