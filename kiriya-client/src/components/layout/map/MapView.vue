// ✅ MapView.vue
<template>
  <div class="map-wrapper">
    <KakaoMap
      :lat="mapStore.coord.lat"
      :lng="mapStore.coord.lng"
      class="w-100 h-100"
      @on-load-kakao-map="onLoadKakaoMap"
    >
      <!-- 관광지 마커 -->
      <IconMarker
        v-for="item in markerStore.attractions"
        :key="item.contentId"
        :lat="item.lat"
        :lng="item.lng"
        :clickable="true"
        :visible="true"
        :icon="item.icon"
        :color="item.color"
        :size="20"
        :label="item.label"
        :contentId="item.contentId"
        @select="$emit('select', $event)"
      />

      <!-- 버킷리스트 마커 -->
      <ImageMarker
        v-if="markerStore.bucketlistMarkerShow"
        v-for="item in markerStore.bucketlists"
        :key="item.contentId"
        :lat="item.lat"
        :lng="item.lng"
        :clickable="true"
        :size="60"
        :label="item.label"
        :contentId="item.contentId"
        :imageSrc="item.imageSrc"
        @select="$emit('select', $event)"
        />
        
        <!-- 경로 마커 -->
        <PolylineMarker
        v-if="markerStore.routeMarkerShow"
        :markerList="routeMarkerList"
        @select="$emit('select', $event)"
        />
      <!-- <ImageMarker
        v-for="item in markerStore.route"
        :key="item.contentId"
        :lat="item.lat"
        :lng="item.lng"
        :clickable="true"
        :size="60"
        :label="item.label"
        :contentId="item.contentId"
        :imageSrc="item.image.imageSrc"
        :order="item.order"
        orderBottomMargin="-20px"
        @select="$emit('select', $event)"
      /> -->

      <!-- 경로 보기 마커 -->
      <PolylineMarker
        v-if="markerStore.routeViewMarkerShow"
        :markerList="routeViewMarkerList"
        @select="$emit('select', $event)"
      />

    </KakaoMap>
  </div>
</template>

<script setup>
import IconMarker from './marker/IconMarker.vue'
import ImageMarker from './marker/ImageMarker.vue'
import PolylineMarker from './marker/PolylineMarker.vue'

import { onMounted, defineEmits, computed } from 'vue'
import { KakaoMap } from 'vue3-kakao-maps'
import { useMapStore } from '@/stores/Map/map'
import { useMarkerStore } from '@/stores/Map/marker'

const emit = defineEmits(['select'])
const mapStore = useMapStore()
const markerStore = useMarkerStore()

const getCurrCoord = (callback) => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        callback({
          lat: position.coords.latitude,
          lng: position.coords.longitude
        })
      },
      (err) => {
        console.error('위치 정보를 가져올 수 없습니다.', err)
      }
    )
  }
}

const onLoadKakaoMap = (mapRef) => {
  mapStore.setMap(mapRef)
}

onMounted(() => {
  getCurrCoord((currCoord) => {
    mapStore.setCoord(currCoord.lat, currCoord.lng, '현위치')
    mapStore.setHasLocation(true)

    markerStore.clearAllMarkers()
    markerStore.addAttraction({
      ...currCoord,
      color: 'error',
      icon: 'mdi-crosshairs-gps',
      label: '현위치',
      contentId: -1
    })
  })
})

const routeMarkerList = computed(() =>
  markerStore.route.map(item => ({
    lat: item.lat,
    lng: item.lng,
    contentId: item.contentId,
    label: item.label,
    image: item.image,
    orderBottomMargin: '13px',
    order: item.order,
    zIndex: item.zIndex,
  }))
)

const routeViewMarkerList = computed(() =>
markerStore.routeView.map(item => ({
    lat: item.lat,
    lng: item.lng,
    contentId: item.contentId,
    label: item.label,
    image: item.image,
    orderBottomMargin: '13px',
    order: item.order,
    zIndex: item.zIndex,
  }))
)

</script>

<style scoped>
.map-wrapper {
  width: 100%;
  height: 100%;
}
</style>
