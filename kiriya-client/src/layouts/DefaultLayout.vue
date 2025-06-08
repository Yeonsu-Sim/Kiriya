<template>
  <v-app>
    <AppHeader class="app-header" />
    <v-main class="main-content">
      <RouterView />
    </v-main>
    <AppFooter class="app-footer" />
  </v-app>
</template>

<script setup>
  import AppHeader from '@/components/layout/AppHeader.vue'
  import AppFooter from '@/components/layout/AppFooter.vue'
  import { RouterView } from 'vue-router'
</script>

<style scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  z-index: 100;
}

.main-content {
  /* /*margin-top: 64px; AppHeader의 높이만큼 여백 추가 */
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
}

.app-footer {
  position: relative;
  bottom: 0;
  left: 0;
  width: 100vw;
  z-index: 100;
}
</style>
