<template>
  <component :is="currentLayout" />
</template>

<script setup>
  import DefaultLayout from './layouts/DefaultLayout.vue'
  import FullScreenLayout from './layouts/FullScreenLayout.vue'
  import OverflowLayout from './layouts/OverflowLayout.vue'

  import { useRoute } from 'vue-router'
  import { computed } from 'vue'

  const route = useRoute()

  const currentLayout = computed(() => {
    const fullScreenPaths = ['/map']
    const overflowPaths = ['/member/find-password', '/myPage']

    if (fullScreenPaths.includes(route.path)) {
      return FullScreenLayout
    } else if (overflowPaths.includes(route.path)) {
      return OverflowLayout
    }
    // 기본 레이아웃을 반환
    return DefaultLayout
  })
</script>
