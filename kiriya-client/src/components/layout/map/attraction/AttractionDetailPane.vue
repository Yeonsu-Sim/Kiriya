<template>
  <v-sheet
    :elevation="4"
    :height="height"
    :width="width"
    :color="color"
    rounded
  >
    <div class="d-flex flex-column w-auto justify-start align-end">
      <v-icon
        icon="mdi-close"
        class="position-absolute ma-2 text-primary"
        size="small"
        @click="emit('closeAttractionDetailPane')"
      />
      <div 
        v-if="attractionStore.detail || false" 
        class="pa-4 w-100 h-100 d-flex flex-column ga-4"
        style="max-height: 80vh;"
      >

        <div class="text-center">
          <h3 class="text-primary my-2">
              <v-icon 
                :icon="markerStore.icons[attractionStore.detail.contentTypeId]"
                size="small"
                :color="markerStore.colors[attractionStore.detail.contentTypeId]"
              />
              {{ attractionStore.detail.title }}
            </h3>
          </div>

        <div class="scroll-pane overflow-auto d-flex flex-column ga-4">
          <AppImage 
            :src="attractionStore.detail.firstImage1 || attractionStore.detail.firstImage2 || elephantImg"
            :lazy-src="elephantImg"
            alt="장소 이미지"
            :height="170"
          />
  
          <div class="d-flex justify-space-between">
            <div style="color:#EFACCF;">
              <v-icon
                icon="mdi-heart-outline"
                v-show="!attractionStore.detail.liked"
                @click="$emit('like', attractionStore.detail.contentId)"
              />
              <v-icon 
                icon="mdi-heart"
                v-show="attractionStore.detail.liked"
                @click="$emit('unlike', attractionStore.detail.contentId)"
              />
              {{ attractionStore.detail.likes }}
            </div>
  
            <v-icon
              icon="mdi-map-plus"
              class="text-info"
              v-show="!isInRoute"
              @click.stop="addRoute"
            />
            <v-icon
              icon="mdi-map-check"
              class="text-success"
              v-show="isInRoute"
              @click.stop="removeRoute"
            />

          </div>
  
          <p style="color:gray; font-size:14px;">
            {{ attractionStore.detail.addr1 }}
          </p>
  
          <div class="d-flex overflow-auto">
            <v-icon icon="mdi-link" style="color:gray"></v-icon>
            <div class="me-1"></div>
            <div
              style="font-size:14px; color:gray;"
              v-html="attractionStore.detail.homepage || '홈페이지가 없습니다.'"
            ></div>
          </div>
          <v-sheet
            class="pa-3 scroll-pane"
            elevation="1"
            style="max-height: 140px; font-size:14px; overflow-y: auto; white-space: pre-wrap;"
          >
            {{ attractionStore.detail.overview || '상세 설명이 없습니다.' }}
          </v-sheet>
        </div>
      </div>
    </div>
  </v-sheet>
</template>

<script setup>
import AppImage from '@/components/common/AppImage.vue'
import elephantImg from '@/assets/elephant-illustration.png'

import { useAttractionStore } from '@/stores/attraction';
import { useMarkerStore } from '@/stores/Map/marker';
import { useBucketlistStore } from '@/stores/bucketlist';
import { useRouteStore } from '@/stores/Map/route';

import { defineEmits } from 'vue'
const emit = defineEmits(['closeAttractionDetailPane', 'like', 'unlike'])

const attractionStore = useAttractionStore()
const markerStore = useMarkerStore()
const bucketlistStore = useBucketlistStore()
const routeStore = useRouteStore()

const props = defineProps({
  height: Number,
  width: Number,
  color: String,
  items: Array
})

const addRoute = () => {
  routeStore.addRoute(attractionStore.detail)
  console.log('경로에 추가')
  console.log('route:', routeStore.route)
}

const removeRoute = () => {
  routeStore.removeRoute(attractionStore.detail)
  console.log('경로에서 제외')
  console.log('route:', routeStore.route)
}

const isInRoute = computed(() => {
  const detail = attractionStore.detail
  console.log(detail)
  if (!detail) return false
  return routeStore.isIn(detail.contentId)
})

</script>

<style scoped>
</style>