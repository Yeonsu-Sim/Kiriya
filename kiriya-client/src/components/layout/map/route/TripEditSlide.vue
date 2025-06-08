<template>
  <div v-show="show" class="relative-parent">
    <!-- 로컬 bottom sheet 스타일 팝업 -->
    <transition name="slide-up-down">
      <div v-if="show" class="local-sheet pa-16 pb-12">
        <v-text-field
          v-model="title"
          label="여행 이름"
          maxlength="40"
          dense
          clearable
          variant="solo-filled"
          color="primary"
        />
        <v-textarea
          v-model="description"
          label="여행 설명"
          maxlength="100"
          rows="3"
          clearable
          variant="solo-filled"
          color="primary"
          class="mt-2"
        />
        <v-row dense class="mt-2">
          <v-col cols="6">
            <v-text-field
              v-model="startDate"
              type="date"
              label="시작일"
              variant="solo-filled"
              color="primary"
              :max="endDate || undefined"
              dense
              clearable
            />
          </v-col>
          <v-col cols="6">
            <v-text-field
              v-model="endDate"
              type="date"
              label="종료일"
              variant="solo"
              color="primary"
              :min="startDate || undefined"
              dense
              clearable
            />
          </v-col>
        </v-row>

        <v-card-actions class="justify-end px-0">
          <v-btn variant="tonal" @click="close">취소</v-btn>
          <v-btn variant="tonal" color="primary" @click="edit" :disabled="!isModified || !title">
            수정</v-btn>
        </v-card-actions>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useTripStore } from '@/stores/trip'

const props = defineProps({
  trip: {
    type: Object,
    required: true,
  }
})

const show = defineModel('show')
const tripStore = useTripStore()

// 내부 폼 상태
const title = ref(null)
const description = ref(null)
const startDate = ref(null)
const endDate = ref(null)

const isModified = computed(() => {
  return (
    title.value !== props.trip.title ||
    description.value !== props.trip.description ||
    startDate.value !== props.trip.startDate ||
    endDate.value !== props.trip.endDate
  )
})

// show=true 되면 trip 내용을 로컬 폼에 복사
watch(show, (val) => {
  if (val && props.trip) {
    title.value = props.trip.title || undefined
    description.value = props.trip.description || undefined
    startDate.value = props.trip.startDate || undefined
    endDate.value = props.trip.endDate || undefined
  }
})

const edit = async () => {
  await tripStore.editTrip({
    tno: props.trip.tno,
    title: title.value,
    description: description.value,
    startDate: startDate.value,
    endDate: endDate.value,
  })
  show.value = false
  clearForm()
}

const close = () => {
  show.value = false
  clearForm()
}

const clearForm = () => {
  title.value = undefined
  description.value = undefined
  startDate.value = undefined
  endDate.value = undefined
}
</script>

<style scoped>
.relative-parent {
  position: relative;
  width: 100%;
  height: 100%;
}

/* 팝업 요소 */
.local-sheet {
  width: 100%;
  position: absolute;
  bottom: 0;
  backdrop-filter: blur(10px);
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  z-index: 10;
}

/* 커스텀 트랜지션 */
.slide-up-down-enter-active,
.slide-up-down-leave-active {
  transition: transform 0.3s ease, opacity 0.3s ease;
}
.slide-up-down-enter-from,
.slide-up-down-leave-to {
  transform: translateY(100%);
  opacity: 0;
}
.slide-up-down-enter-to,
.slide-up-down-leave-from {
  transform: translateY(0);
  opacity: 1;
}
</style>
