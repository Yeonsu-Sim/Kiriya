<template>
    <div class="feed-board-bg">
      <div class="feed-board-wrapper">
        <!-- 왼쪽 사이드바 -->
        <FeedSidebar class="sidebar" />
  
        <!-- 메인 피드(Masonry) -->
        <FeedMasonry :feeds="feeds" class="feed-main" />
  
        <!-- (선택) 오버레이 (댓글/상세) -->
        <FeedOverlay v-if="showOverlay" :feed="selectedFeed" @close="showOverlay = false"/>
      </div>
    </div>
  </template>
  
  <script setup>
  import FeedSidebar from '@/components/layout/feed/FeedSidebar.vue'
  import FeedMasonry from '@/components/layout/feed/FeedMasonry.vue'
//   import FeedOverlay from '@/components/layout/feed/FeedOverlay.vue' // (댓글/상세)
  import { ref } from 'vue'
  
  const showOverlay = ref(false)
  const selectedFeed = ref(null)
  </script>
  
  <style>
  .feed-board-bg {
    background: #f7f7fa;
    min-height: 100vh;
    padding: 0;
  }
  .feed-board-wrapper {
    display: flex;
    max-width: 1440px;
    margin: 0 auto;
    padding-top: 48px;
  }
  .sidebar {
    flex: 0 0 240px;
    margin-right: 32px;
  }
  .feed-main {
    flex: 1 1 0%;
    min-width: 0;
  }
  </style>
  