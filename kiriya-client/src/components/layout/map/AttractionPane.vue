<template>
  <AppTabPane 
    :width="250"
    :color="'surface'"
    :items="items"
    :tab="tab"
    @select="$emit('select', $event)"
    @tabChange="tabChange($event)"
  >
  </AppTabPane>
</template>

<script setup>
import AppTabPane from '@/components/common/AppTabPane.vue'
import AttractionSearchContent from './attraction/AttractionSearchContent.vue'
import AttractionBucketlistContent from './attraction/AttractionBucketlistContent.vue'

import { ref } from 'vue'
import { defineEmits } from 'vue'
import { useMarkerStore } from '@/stores/Map/marker'
import { useMapStore } from '@/stores/Map/map'

const markerStore = useMarkerStore()
const mapStore = useMapStore()
const emit = defineEmits(['select'])

const items = [
  { label: '검색', value: 'find', content: AttractionSearchContent},
  { label: '버킷리스트', value: 'bucketlist', content: AttractionBucketlistContent},
]
const tab = ref(items[0].value)

const tabChange = (value) => {
  if (value === 'bucketlist') {
    markerStore.showBucketlistMarker()
    mapStore.fitbounds(markerStore.bucketlists)
  } else {
    markerStore.hideBucketlistMarker()
  }
}
</script>

<style scoped>
</style>