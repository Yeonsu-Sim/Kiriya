<template>
  <KakaoMapMarkerPolyline
    :markerList="markerList"
    :showMarkerOrder="true"
    strokeColor="#EF5350"
    :strokeWeight="5"
    :strokeOpacity="1"
    :endArrow="true"
    strokeStyle="shortdot"
    :zIndex="5"
    @mouseOverKakaoMapMarker="showInfoWindow($event)"
    @mouseOutKakaoMapMarker="hideInfoWindow($event)"
    @onClickKakaoMapMarker="select($event)"
    class="custom-marker"
    />
    <KakaoMapCustomOverlay
    v-if="selectedMarker !== null"
    :lat="selectedMarker?.lat"
    :lng="selectedMarker?.lng"
    :clickable="true"
    :yAnchor="1.4"
    :zIndex="infoWindowShow  || attractionStore.detail?.contentId == selectedMarker?.contentId ? 2 : 1"
    >
    <div 
      class="d-flex flex-column justify-center ga-1"
    >
      <v-sheet 
        class="custom-info-window justify-center bg-surface pa-1" 
        elevation="4"
        v-if="infoWindowShow || attractionStore.detail?.contentId == selectedMarker?.contentId"
      >
        <span class="text-center">{{ selectedMarker?.label }}</span>
      </v-sheet>
    </div>
  </KakaoMapCustomOverlay>
</template>

<script setup>
import { KakaoMapMarkerPolyline, KakaoMapCustomOverlay } from 'vue3-kakao-maps';
import { ref, defineEmits } from 'vue'
import { useAttractionStore } from '@/stores/attraction';

const attractionStore = useAttractionStore()

defineProps({
  markerList: Array,
})

const infoWindowShow = ref(false);

const emit = defineEmits(['select'])
const select = (marker) => {
  const contentId = marker.contentId
  if (contentId > -1) {
    emit('select', contentId)
  }
}

const selectedMarker = ref(null)

const showInfoWindow = (marker) => {
  selectedMarker.value = marker
  infoWindowShow.value = true
}

const hideInfoWindow = (marker) => {
  selectedMarker.value = null
  infoWindowShow.value = false
}

</script>

<style scoped>

.custom-marker {
  border-radius: 50%;
  cursor: pointer;
  pointer-events: auto;
}

.custom-info-window {
  max-width: 140px;
  white-space: nowrap;         /* 줄바꿈 안 함 */
  overflow: hidden;            /* 넘치는 내용 숨김 */
  text-overflow: ellipsis;     /* 말줄임표 처리 */
  position: absolute;
  bottom:20px;
  align-self:center;
  font-size: 14px;
  border-radius: 6px;
  pointer-events: auto;
}

</style>