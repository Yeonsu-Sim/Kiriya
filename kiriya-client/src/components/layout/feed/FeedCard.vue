<template>
  <div class="feed-card" @click="showDetail">
    <div class="feed-header">
      <img
        class="profile-img"
        :src="board.profileUrl || defaultProfileImage"
        alt="프로필"
      />
      <div class="profile-meta">
        <span class="feed-user">{{ board.author }}</span>
        <span class="feed-time">{{ timeAgo(board.createdAt) }}</span>
      </div>

      <div v-if="isMine" class="feed-menu-wrapper">
      <v-btn
        icon
        variant="text"
        size="small"
        class="dot-btn"
        @click.stop="toggleMenu"
      >
        <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
         <div v-if="menuOpen" class="feed-menu">
        <v-btn variant="text" @click.stop="editBoard">수정</v-btn>
        <v-btn variant="text" color="error" @click.stop="deleteBoard">삭제</v-btn>
      </div>
      </div>
    </div>

    <img
      v-if="board.imageUrls?.length"
      :src="board.imageUrls[0]"
      class="feed-img"
    />

    <div class="feed-body">
      <div class="feed-title">{{ board.title }}</div>
      <div class="feed-content">{{ board.content }}</div>
      <div class="feed-tags mt-2">
        <span v-for="tag in board.tags" :key="tag" class="feed-tag"
          >#{{ tag }}</span
        >
      </div>
      <div class="feed-actions mt-2">
        <span class="icon-heart"
          :class="{ active: board.liked }"
          @click.stop="$emit('toggleLike',board)">♥</span>
        <span>{{ board.likes }}</span>
        <span class="icon-comment">💬</span>
        <span>{{ board.commentCount }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/user';
import axios from 'axios'
import { timeAgo } from "@/utils/timeAgo";
import defaultProfileImage from "@/assets/default-profile.png";

const props = defineProps(["board"]);
const emit = defineEmits(["showDetail"], 'edit', 'delete');
const menuOpen = ref(false)

const userStore = useUserStore()
const isMine = props.board.mno === userStore.profile?.mno

function toggleMenu(event) {
  event.stopPropagation()
  menuOpen.value = !menuOpen.value
}
function editBoard(e){
  e.stopPropagation()
  emit('edit', props.board)
  menuOpen.value = false
}
function deleteBoard(e){
  e.stopPropagation()
  emit('delete', props.board)
  menuOpen.value = false
}
function showDetail() {
  emit("showDetail", props.board);
}

</script>

<style scoped>
.feed-img {
  width: 100%;
  display: block;
  object-fit: cover; /* 이미지를 꽉 채우되 넘치는 부분은 crop */
  object-position: center;
  background: #f3f3fa;
  /* 추가로 테두리나 그림자도 ok */
}

.feed-card,
.feed-card * {
  font-family: system-ui, "Apple SD Gothic Neo", "맑은 고딕", Arial, sans-serif !important;
}

.feed-card {
  padding: 10px;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 20px rgba(120, 120, 140, 0.09);
  border: 1.5px solid #ebe8f7; /* 부드러운 연보라색 테두리 */
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.18s, border-color 0.18s;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* 호버 효과로 강조 */
.feed-card:hover {
  box-shadow: 0 6px 32px rgba(100, 90, 170, 0.12);
  border-color: #bfaaff;
}

.feed-header {
  display: flex;
  align-items: center;
  padding-bottom: 10px;
  gap: 10px;
}

.feed-menu-wrapper {
  position: absolute;
  top: 10px; right: 16px;
  z-index: 5;
}
.dot-btn {
  min-width: unset !important;
  padding: 0 !important;
}
.feed-menu {
  position: absolute; top: 30px; right: 0;
  background: #fff; border-radius: 12px;
  box-shadow: 0 4px 20px rgba(120, 120, 140, 0.13);
  display: flex; flex-direction: column;
  min-width: 88px;
}
.feed-menu .v-btn {
  justify-content: center;
  color: #8569c6;
  border-radius: 0;
}
.feed-menu .v-btn:hover {
  background: #f3f3fa !important;
  color: #7b5ccc;
}

.profile-img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  background: #fafaff;
  border: 1.5px solid #eaeaea;
}
.profile-meta {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.feed-body {
  padding: 10px;
}

.feed-title {
  font-size: 1.2rem;
  font-weight: bold;
  color: #23233b;
  margin-top: 12px;
  margin-bottom: 6px;
  line-height: 1.25;
  letter-spacing: -0.5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.feed-user {
  font-weight: 600;
  font-size: 14px;
  color: #5d567e;
}
.feed-time {
  margin-left: auto;
  font-size: 13px;
  color: #b5b5cc;
}
.feed-content {
  font-size: 15px;
  color: #23233b;
  margin: 8px 0 3px 0;
  max-height: 45px;
  overflow: hidden;
  text-overflow: ellipsis;
}
.feed-tags {
  margin-bottom: 7px;
}
.feed-tag {
  background: #f4f0fe;
  color: #8569c6;
  border-radius: 11px;
  font-size: 13px;
  padding: 2px 10px;
  margin-right: 6px;
}
.feed-actions {
  font-size: 14px;
  color: #b5b5cc;
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 8px;
  justify-content: flex-end;
}
.icon-heart.active {
  color: #ff4488;
  font-weight: bold;
  text-shadow: 0 0 8px #ffd5ee;
  transition: color 0.2s;
  cursor: pointer;
}
.icon-heart { 
  color: #BBA8F5;
  cursor: pointer; 
}
.icon-comment {
  margin-left: 10px;
}
</style>
