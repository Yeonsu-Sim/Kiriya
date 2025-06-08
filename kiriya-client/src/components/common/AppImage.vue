<template>
  <v-img
    :src="src"
    :lazy-src="lazySrc"
    :height="height"
    :width="width"
    :alt="alt"
    aspect-ratio="16/9"
    contain
    >
    <!-- 로딩 중 표시 -->
    <template #placeholder>
        <v-row class="fill-height ma-0" align="center" justify="center">
        <v-progress-circular indeterminate color="primary" />
        </v-row>
    </template>

    <!-- 로드 실패 시 -->
    <template #error>
        <v-row class="fill-height ma-0 bg-grey-lighten-3" align="center" justify="center">
        <v-icon size="48" color="error">mdi-alert-circle</v-icon>
        <span class="ml-2 text-error">이미지를 불러오지 못했습니다</span>
        </v-row>
    </template>
  </v-img>
</template>
<script setup>
defineProps({
  src: {
    type: String,
    required: true
  },
  lazySrc: {
    type: String,
    default: ''
  },
  height: {
    type: [String, Number],
    default: '200'
  },
  width: {
    type: [String, Number],
    default: '100%'
  },
  alt: {
    type: String,
    default: '이미지'
  }
})
</script>
