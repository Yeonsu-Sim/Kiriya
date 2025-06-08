<template>
  <div class="d-flex flex-column" style="max-height: 80vh;">
    <v-sheet 
      class="scroll-pane px-3"
      rounded
    >
      <v-sheet elevation="3"
        class="bucket-block pa-2 my-2"
        v-for="item in bucketlistStore.bucketlist"
        :style="{ 
          backgroundColor: attractionStore.detail?.contentId == item.contentId ? 'whitesmoke' : ''}"
        :key="item.id"
        rounded
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
              <div class="d-flex flex-row ga-1" style="color:#EFACCF">
                <v-icon v-show="item.liked" icon="mdi-heart" @click.stop="$emit('unlike', item.contentId)"></v-icon>
                <v-icon v-show="!item.liked" icon="mdi-heart-outline" @click.stop="$emit('like', item.contentId)"></v-icon>
                {{ item.likes }}
              </div>
          </div>
          <div class="d-flex flex-column">
            <div class="d-flex ga-1">
              <v-icon 
                :icon="markerStore.icons[item.contentTypeId+'']"
                size="small"
                :color="markerStore.colors[item.contentTypeId]"
              />
              <h4 class="text-primary">{{ item.title }}</h4>
            </div>
            <p style="color: gray; font-size:13px;">{{ item.addr1 }}</p>
          </div>
        </div>
      </v-sheet>
    </v-sheet>
  </div>
</template>

<script setup>
import AppImage from '@/components/common/AppImage.vue'
import elephantImg from '@/assets/elephant-illustration.png'

import { onMounted, defineEmits } from 'vue'
import { useUserStore } from '@/stores/user'
import { useBucketlistStore } from '@/stores/bucketlist'
import { useAttractionStore } from '@/stores/attraction'
import { useMarkerStore } from '@/stores/Map/marker'

const userStore = useUserStore()
const bucketlistStore = useBucketlistStore()
const attractionStore = useAttractionStore()
const markerStore = useMarkerStore()

const emit = defineEmits(['select'])

onMounted(async () => {
  // if (userStore.isLoggedIn > 0) {
    await bucketlistStore.fetchBucketlist()
  // }
  console.log("bucketlist:",bucketlistStore.bucketlist)
})

watch(() => userStore.isLoggedIn, (val) => {
  if (val) {
    bucketlistStore.fetchBucketlist()
  }
})

</script>

<style scoped>
.bucket-block {
  transition: background-color 0.2s ease, box-shadow 0.2s ease;
}
.bucket-block:hover {
  background-color: whitesmoke;
}
</style>