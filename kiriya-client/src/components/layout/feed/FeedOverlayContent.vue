<template>
    <div class="feed-card overlay-card" ref="overlayRef">
      <!-- 닫기 버튼 -->
      <button class="close-btn" @click="$emit('close')">×</button>
      
      <!-- 헤더: 프로필, 이름, 시간 -->
      <div class="feed-header">
        <img
          class="profile-img"
          :src="feed.profileUrl || defaultProfileImage"
          alt="프로필"
        />
        <div class="profile-meta">
          <span class="feed-user">{{ feed.author }}</span>
          <span class="feed-time">{{ feed.createdAt }}</span>
        </div>
      </div>

      <!-- 피드 이미지 -->
       <div class="feed-img-wrapper">
        <swiper
            :modules="[Navigation, Pagination]"
            :navigation="true"
            :pagination="{ clickable: true }"
            :slides-per-view="1"
            :space-between="8"
            class="feed-swiper"
            >
            <swiper-slide v-for="(url, idx) in feed.imageUrls" :key="idx">
                <img :src="url" class="feed-img" alt="피드 이미지" />
            </swiper-slide>
            </swiper>
        </div>
    <!-- <div class="feed-img-wrapper" v-if="feed.imageUrls?.length"> -->


      <!-- 본문 -->
      <div class="feed-body">
        <div class="feed-title">{{ feed.title }}</div>
        <div class="feed-content">{{ feed.content }}</div>
        <div class="feed-tags">
          <span v-for="tag in feed.tags" :key="tag" class="feed-tag">#{{ tag }}</span>
        </div>
        <!-- 좋아요/댓글 (필요시) -->
        <div class="feed-actions">
          <span class="icon-heart"
            :class="{ active: feed.liked }"
            @click.stop="$emit('toggleLike',feed)">♥</span>
          <span>{{ feed.likes }}</span>
          <span class="icon-comment" @click.stop="$emit('toggleComments')">💬</span>
          <span class="comment-count">{{ commentCount }}</span>
        </div>
      </div>

    </div>
</template>

<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation, Pagination } from 'swiper/modules'
import { ref, computed } from 'vue'
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import defaultProfileImage from '@/assets/default-profile.png'

const props = defineProps(['feed'])
const commentCount = computed(() => props.feed.commentCount ?? 0)
console.log(props)
</script>

<style scoped>


/* 카드 사이즈와 스크롤 제어 */
.overlay-card {
  max-width: 820px;
  min-width: 400px;
  width: auto;
  min-height: 480px;
  height: auto;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 40px rgba(80, 70, 120, 0.17);
  padding: 32px 28px;
  font-family: system-ui, 'Apple SD Gothic Neo', '맑은 고딕', Arial, sans-serif !important;
  /* 필요하면 추가 */
  display: flex;
  flex-direction: column;
  position: relative;
}

.close-btn {
  position: absolute;
  top: 18px; right: 23px;
  background: none; border: none;
  font-size: 32px; cursor: pointer;
  color: #b6a3e9;
  transition: color 0.18s;
}
.close-btn:hover { color: #835cc6; }

.feed-header {
  display: flex; align-items: center; gap: 10px;
  margin-bottom: 16px;
}
.profile-img {
  width: 44px; height: 44px;
  border-radius: 50%; object-fit: cover;
  background: #e5e0f5;
  border: 1px solid #ede5f8;
}
.profile-meta {
  display: flex; flex-direction: column; justify-content: center;
}
.feed-user {
  font-weight: 600;
  color: #353350;
  font-size: 15px;
}
.feed-time {
  font-size: 12px;
  color: #b5b5cc;
  margin-top: 2px;
}

.feed-img-wrapper,
.feed-swiper,
.feed-swiper .swiper-wrapper,
.feed-swiper .swiper-slide {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
  border-radius: 16px;
}

.feed-swiper {
  width: 100%;
  max-width: 600px;
}

.feed-img {
  display: block;
  max-width: 100%;
  max-height: 400px;
  height: auto;
  width: auto;
  object-fit: contain;
  margin: 0;
}

.feed-body {
  display: flex; flex-direction: column;
}
.feed-title {
  font-size: 21px;
  font-weight: bold;
  color: #353350;
  margin-bottom: 8px;
}
.feed-content {
  color: #23233b;
  font-size: 16px;
  margin-bottom: 11px;
  line-height: 1.6;
}
.feed-tags {
  margin-top: 5px; margin-bottom: 6px;
}
.feed-tag {
  background: #f4f0fe;
  color: #8569c6;
  border-radius: 11px;
  font-size: 12px;
  padding: 2px 10px;
  margin-right: 6px;
}
.feed-actions {
  margin-top: 8px;
  font-size: 15px;
  color: #b5b5cc;
  display: flex;
  align-items: center;
  gap: 12px;
  justify-content: flex-end;
}
.icon-heart {
  color: #BBA8F5;
  margin-right: 2px;
  cursor: pointer;
}
.icon-heart.active {
  color: #ff4488;
  font-weight: bold;
  text-shadow: 0 0 8px #ffd5ee;
  transition: color 0.2s;
  cursor: pointer;
}
.icon-comment {
  margin-left: 16px;
}

:deep(.swiper-button-next),
:deep(.swiper-button-prev) {
  color: #b6a3e9 !important;
  background: rgba(245, 243, 254, 0.85);
  border-radius: 50%;
  width: 44px; height: 44px;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s;
  box-shadow: 0 3px 12px rgba(140, 120, 200, 0.08);
  font-size: 21px;
}

:deep(.swiper-button-next:hover),
:deep(.swiper-button-prev:hover) {
  color: #7e37d8 !important;
  background: #ece2fa;
  box-shadow: 0 6px 28px rgba(120, 80, 170, 0.16);
  transform: scale(1.09);
}

:deep(.swiper-button-next)::after,
:deep(.swiper-button-prev)::after {
  font-size: 27px !important;
}

@media (max-width: 700px) {
    .overlay-card {
    max-width: 98vw;
    padding: 14px 8px;
  }
  .feed-card.overlay-card {
    padding: 14px 2vw;
    min-width: 0;
    max-width: 99vw;
    max-height: 99vh;
  }
  .feed-img {
    max-width: 96vw;
    max-height: 38vw;
  }

   :deep(.swiper-button-next),
  :deep(.swiper-button-prev) {
    width: 30px; height: 30px;
    font-size: 15px;
  }
  :deep(.swiper-button-next)::after,
  :deep(.swiper-button-prev)::after {
    font-size: 18px !important;
  }
}
</style>