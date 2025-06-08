<template>
  <KakaoMapCustomOverlay 
  :lat="lat" 
  :lng="lng" 
  :clickable="clickable" 
  :visible="visible"
  :zIndex="infoWindowShow  || attractionStore.detail?.contentId == contentId ? 0 : -1"
  
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

      <v-sheet
        ref="markerEl"
        :class="`custom-marker bg-${color} align-self-center`"
        elevation="4"
        @mouseenter.stop="infoWindowShow = true"
        @mouseleave.stop="infoWindowShow = false"
        @click.stop="select(contentId)"
      >
        <div>
          <v-icon
            class="ma-1"
            :icon="icon" 
            color="surface"
            :size="size"
          />
        </div>
      </v-sheet>
    </div>
  </KakaoMapCustomOverlay>
</template>

<script setup>
import { KakaoMapCustomOverlay } from 'vue3-kakao-maps'

import { ref, defineExpose, defineEmits } from 'vue'
import { useAttractionStore } from '@/stores/attraction';

const attractionStore = useAttractionStore()

defineProps({
  lat: [String, Number],
  lng: [String, Number],
  visible: [String, Boolean],
  clickable: [String, Boolean],
  icon: String,
  color: String,
  size: Number,
  label: String,
  contentId: [String, Number],
})

const markerEl = ref(null)
defineExpose({ markerEl })

const infoWindowShow = ref(false);

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
  bottom:35px;
  align-self:center;
  font-size: 14px;
  border-radius: 6px;
}

</style>