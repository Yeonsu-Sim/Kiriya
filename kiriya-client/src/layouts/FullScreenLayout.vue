<template>
  <v-app>
    <AppHeader class="app-header" />
    <v-main class="pa-0 main-content">
      <RouterView />
    </v-main>
  </v-app>
</template>

<script setup>
  import AppHeader from '@/components/layout/AppHeader.vue'
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
  margin-top: 64px; /* AppHeader의 높이만큼 여백 추가 */
  flex-grow: 1;
}

v-app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>
