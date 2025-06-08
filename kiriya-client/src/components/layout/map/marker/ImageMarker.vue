<template>
  <KakaoMapMarker 
  :lat="lat"
  :lng="lng"
  :clickable="clickable"
  :zIndex="infoWindowShow  || attractionStore.detail?.contentId == contentId ? 0 : -1"
  :image="{
    imageSrc: imageSrc,
    imageWidth: size,
    imageHeight: size,
    imageOption: {offset: offset},
  }"
  :order="order"
  :order-bottom-margin="orderBottomMargin"
  :yAnchor="1.5"
  class="custom-marker"
  @mouseOverKakaoMapMarker="infoWindowShow = true"
  @mouseOutKakaoMapMarker="infoWindowShow = false"
  @onClickKakaoMapMarker="select(contentId)"
  />
  <KakaoMapCustomOverlay
    :lat="lat"
    :lng="lng"
    :clickable="true"
    :yAnchor="1.4"
    :zIndex="infoWindowShow  || attractionStore.detail?.contentId == contentId ? 1 : 0"
  >
    <div 
      class="d-flex flex-column justify-center ga-1"
    >
      <v-sheet 
        class="custom-info-window justify-center bg-surface pa-1" 
        elevation="4"
        v-if="infoWindowShow || attractionStore.detail?.contentId == contentId"
      >
        <span class="text-center">{{ label }}</span>
      </v-sheet>
    </div>
  </KakaoMapCustomOverlay>
</template>

<script setup>
import { KakaoMapMarker, KakaoMapCustomOverlay } from 'vue3-kakao-maps'
import { ref, defineExpose, defineEmits } from 'vue'
import { useAttractionStore } from '@/stores/attraction';

const attractionStore = useAttractionStore()

defineProps({
  lat: [String, Number],
  lng: [String, Number],
  clickable: [String, Boolean],
  size: Number,
  label: String,
  contentId: [String, Number],
  imageSrc: String,
  order: [String, Number],
  orderBottomMargin: String,
})

// const markerEl = ref(null)
// defineExpose({ markerEl })

const infoWindowShow = ref(false);
const offset = new kakao.maps.Point(30, 29)

const emit = defineEmits(['select'])
const select = (contentId) => {
  if (contentId > -1) {
    emit('select', contentId)
    console.log('마커 클릭:',contentId)
  }
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