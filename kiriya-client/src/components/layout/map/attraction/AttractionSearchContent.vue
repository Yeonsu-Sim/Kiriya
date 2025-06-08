<template>
  <div class="d-flex flex-column" style="max-height: 80vh;">
    <AppSearchBar @search="search($event)" class="pa-3"/>

    <v-sheet 
      class="attraction-list scroll-pane px-3"
      rounded
    >
      <v-sheet elevation="3"
        class="attraction-block pa-2 my-2"
        v-for="item in attractionStore.attractions"
        :style="{ 
          backgroundColor: attractionStore.detail?.contentId == item.contentId ? 'whitesmoke' : ''}"
        :key="item.id"
        rounded
        @click="$emit('select', item.contentId)"
      >
        <div class="info">
          <div class="d-flex ga-1">
            <v-icon 
              :icon="markerStore.icons[item.contentTypeId+'']"
              size="small"
              :color="markerStore.colors[item.contentTypeId]"
            />
            <h4 class="text-primary">{{ item.title }}</h4>
          </div>
          <p style="color: gray; font-size:14px;">{{ item.address }}</p>
          <div class="d-flex flex-row ga-1" style="color:#EFACCF">
            <v-icon v-show="item.liked" icon="mdi-heart" @click.stop="$emit('unlike', item.contentId)"></v-icon>
            <v-icon v-show="!item.liked" icon="mdi-heart-outline" @click.stop="$emit('like', item.contentId)"></v-icon>
            {{ item.likes }}
          </div>
        </div>
      </v-sheet>
    </v-sheet>
  </div>
</template>

<script setup>
import AppSearchBar from '@/components/common/AppSearchBar.vue'

import { defineEmits } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAttractionStore } from '@/stores/attraction'
import { useMarkerStore } from '@/stores/Map/marker'
import { useBucketlistStore } from '@/stores/bucketlist'

const route = useRoute()
const router = useRouter()
const emit = defineEmits(['select', 'like', 'unlike'])
const attractionStore = useAttractionStore()
const markerStore = useMarkerStore()
const bucketlistStore = useBucketlistStore()

const search = async (searchQuery) => {
  console.log("검색어:", searchQuery)
  // URL 파라미터에 반영
  await router.push({
    query: {
      ...route.query,
      query: searchQuery,
    }
  })
}

</script>

<style scoped>
.attraction-block {
  transition: background-color 0.2s ease, box-shadow 0.2s ease;
}
.attraction-block:hover {
  background-color: whitesmoke;
}
</style>