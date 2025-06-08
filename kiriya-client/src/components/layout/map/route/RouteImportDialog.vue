<template>
  <v-dialog v-model="routeStore.routeImportDialogShow" 
    class="rounded-lg d-flex justify-center align-center"
    style="width:fit-content;"
  >
    <div class="dialog-card">
        <AppTabPane
          :color="'surface'"
          :items="items"
          :tab="tab"
          class="h-100 w-100"
          @select="$emit('select', $event)"
          @tabChange="tabChange($event)"
        >
        </AppTabPane>
    </div>
  </v-dialog>
</template>

<script setup>
import AppTabPane from '@/components/common/AppTabPane.vue'
import TripListContent from './import/TripListContent.vue'
import GroupListContent from './import/GroupListContent.vue'
import { ref } from 'vue'

import { useRouteStore } from '@/stores/Map/route'

const routeStore = useRouteStore()

const items = [
  { label: '내 여행', value: 'myTrip', content: TripListContent },
  { label: '내 그룹', value: 'myGroup', content: GroupListContent },
]
const tab = ref(items[0].value)

const tabChange = (value) => {
  tab.value = value
  items.forEach(item => {
    item.visible = item.value === value
  })
} 

</script>

<style scoped>
.dialog-card {
  width: 60vw;
  height: 60vh;
  max-width: 600px;
  max-height: 600px;
  align-self: center;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 40px rgba(80, 70, 120, 0.17);
  position: relative;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
</style>
