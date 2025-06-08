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
import RouteMakePane from './route/RouteMakePane.vue'
import RouteViewPane from './route/RouteViewPane.vue'

import { ref, defineEmits } from 'vue'
import { useMarkerStore } from '@/stores/Map/marker'
import { useMapStore } from '@/stores/Map/map'

const markerStore = useMarkerStore()
const mapStore = useMapStore()
const emit = defineEmits(['select'])

const items = [
  { label: '경로 만들기', value: 'route-make', content: RouteMakePane},
  { label: '경로 보기', value: 'route-view', content: RouteViewPane},
]
const tab = ref(items[0].value)

const tabChange = (value) => {
  if (value === 'route-make') {
    markerStore.showRouteMarker()
    markerStore.hideRouteViewMarker()
    // mapStore.fitbounds(markerStore.route)
  } else {
    markerStore.showRouteViewMarker()
    markerStore.hideRouteMarker()
    // mapStore.fitbounds(markerStore.routeView)
  }
}
</script>

<style scoped>
</style>

