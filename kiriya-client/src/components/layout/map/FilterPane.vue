<template>
  <div class="d-flex flex-wrap ga-2 mb-4 align-start">
    <!-- Autocomplete 그룹 -->
    <div class="d-flex ga-2 flex-wrap">
      <v-autocomplete
        v-model="filterStore.selectedSido"
        :items="filterStore.sidoList"
        item-title="label"
        item-value="value"
        label="시/도 선택"
        variant="solo"
        color="primary"
        class="bg-blur color-primary"
        clearable
        :return-object="false"
        hide-details="true"
        @update:modelValue="onSidoUpdate"
      />

      <v-autocomplete
        v-model="filterStore.selectedGungu"
        :items="filterStore.gunguList"
        item-title="label"
        item-value="value"
        label="시/군/구 선택"
        variant="solo"
        color="primary"
        class="bg-blur color-primary"
        clearable
        :return-object="false"
        :disabled="!filterStore.selectedSido"
        hide-details="true"
      />

      <v-btn color="primary h-auto my-1 gradient-btn" @click="search">검색</v-btn>
    </div>

    <!-- Chip 그룹 -->
    <div class="content-type-chip-container d-flex flex-grow-1 flex-shrink-1">
      <v-sheet class="w-100 rounded position-relative bg-blur pa-1 mt-1" style="height:fit-content;">
        <!-- 버튼과 chip-group을 한 줄에 나란히 정렬 -->
        <div class="d-flex flex-nowrap align-center">
          
          <!-- 모두 선택 / 취소 버튼 -->
          <div class="flex-shrink-0 ms-1">
            <v-btn
              v-show="!filterStore.contentTypesAllSelected"
              size="small"
              variant="tonal"
              color="success"
              class="bg-blur"
              @click="filterStore.fillSelectedContentTypes"
            >
              모두 선택
            </v-btn>
            <v-btn
              v-show="filterStore.contentTypesAllSelected"
              size="small"
              variant="tonal"
              color="error"
              class="bg-blur"
              @click="filterStore.clearSelectedContentTypes"
            >
              모두 취소
            </v-btn>
          </div>

          <!-- chip-group이 너비를 유연하게 차지하도록 설정 -->
          <v-chip-group
            v-model="filterStore.selectedContentTypes"
            selected-class="text-surface"
            multiple
            show-arrows
            class="overflow-auto h-auto flex-grow-1"
          >
            <v-chip
              v-for="type in filterStore.contentTypeList"
              :key="type.label"
              :value="type.value"
              variant="elevated"
              size="small"
              :class="`ma-1 pa-2 bg-${filterStore.selectedContentTypes.includes(type.value) ? type.color : 'surface'}`"
            >
              <v-icon :icon="type.icon || 'mdi-map-marker'" start></v-icon>
              {{ type.label }}
            </v-chip>
          </v-chip-group>
        </div>

      </v-sheet>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useFilterStore } from '@/stores/Map/filter'

const route = useRoute()
const router = useRouter()
const filterStore = useFilterStore()

const updateFilterFromURL = async () => {
  const urlParams = new URLSearchParams(window.location.search)
  const sidoCode = urlParams.get("sidoCode")
  const gugunCode = urlParams.get("gunguCode")

  await filterStore.fetchSidoList()

  if (sidoCode) {
    filterStore.selectedSido = parseInt(sidoCode)
    await filterStore.fetchGunguList(sidoCode)
  }

  if (gugunCode) {
    filterStore.selectedGungu = parseInt(gugunCode)
  }
}

const onSidoUpdate = async (newVal) => {
  filterStore.clearGungu()
  if (newVal) {
    await filterStore.fetchGunguList(newVal)
  }
}

const search = async () => {
  const sidoCode = filterStore.selectedSido
  const gunguCode = filterStore.selectedGungu
  const newQuery = {}
  if (sidoCode) {
    newQuery.sidoCode = sidoCode
    if (gunguCode) {
      newQuery.gunguCode = gunguCode
    } else {
      alert('시/군/구를 선택하세요.')
      return
    }
  } else {
    alert('시/도를 선택하세요.')
    return
  }

  await router.push({
    query: {
      ...newQuery,
    }
  })
}

onMounted(async () => {
  updateFilterFromURL()
  window.addEventListener("popstate", updateFilterFromURL)
  await filterStore.fetchContentTypeList()
})

onUnmounted(() => {
  window.removeEventListener("popstate", updateFilterFromURL)
})
</script>

<style scoped>
.v-autocomplete {
  min-width: 200px;
}

.content-type-chip-container {
  z-index: 10;
  pointer-events: auto;
  flex: 1 1 0;
  min-width: 300px;
}
</style>
